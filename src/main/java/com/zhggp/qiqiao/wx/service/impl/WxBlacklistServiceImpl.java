package com.zhggp.qiqiao.wx.service.impl;

import com.zhggp.qiqiao.wx.common.config.TaskExcutor;
import com.zhggp.qiqiao.wx.db.dto.WxMpInfo;
import com.zhggp.qiqiao.wx.db.entity.WxUser;
import com.zhggp.qiqiao.wx.service.WxBlacklistService;
import com.zhggp.qiqiao.wx.service.WxUserService;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpUserBlacklistService;
import me.chanjar.weixin.mp.api.WxMpUserService;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.result.WxMpUserBlacklistGetResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WxBlacklistServiceImpl  implements WxBlacklistService {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    WxMpService wxMpService;
    @Autowired
    WxMpInfo wxMpInfo;
    @Autowired
    @Lazy
    WxUserService wxUserService;
    /**
     * 单机就不考虑分布式锁了
     */
    private volatile static boolean syncWxUserTaskRunning = false;


    // 获取公众号的黑名单列表  一次拉10000个 一次拉完全部
    //openid为空时，默认从开头拉取。
    @Async
    public void syncBlacklist() throws WxErrorException {
        Assert.isTrue(!syncWxUserTaskRunning, "后台有同步公众号的黑名单列表的任务正在进行中，请稍后重试");
        logger.info("WxBlacklistService.getBlacklist start ");

        syncWxUserTaskRunning = true;
        String appid = wxMpInfo.getAppId();
        wxMpService.switchoverTo(appid);

        boolean hasMore = true;
        String nextOpenid = null;
        WxMpUserBlacklistService blacklistService = wxMpService.getBlackListService();
        try {
            int page = 1;
            while (hasMore) {
                WxMpUserBlacklistGetResult blacklist = blacklistService.getBlacklist(nextOpenid);//拉取openid列表，每次最多1万个
                logger.info("WxBlacklistService.getBlacklist拉取黑名单openid列表：第{}页，数量：{}", page++, blacklist.getCount());
                List<String> openids = blacklist.getOpenidList();
                this.syncWxUsersBlacklist(openids, appid);
                nextOpenid = blacklist.getNextOpenid();
                hasMore = StringUtils.hasText(nextOpenid) && blacklist.getCount() >= 10000;
            }
        } catch (WxErrorException e) {
            logger.info("WxBlacklistService.getBlacklist error e={} ", e.getMessage());
        } finally {
            syncWxUserTaskRunning = false;
        }
        logger.info("WxBlacklistService.getBlacklist end ");
    }

    //     同步微信用户的黑名单数据
    private void syncWxUsersBlacklist(List<String> openids, String appid) {
        if (openids.size() < 1) {
            return;
        }
        //截取首个openid的一部分做批次号（打印日志时使用，无实际意义）
        final String batchId = openids.get(0).substring(20);
        int start = 0;
        int batchSize = openids.size();
        int end = Math.min(100, batchSize);
        WxMpUserService wxMpUserService = wxMpService.getUserService();
//分批处理,每次最多拉取100个用户信息
        logger.info("WxBlacklistService.syncWxUsersBlacklist 开始处理批次：{}，批次数量：{}", batchId, batchSize);
        while (start < end && end <= batchSize) {
            final int finalStart = start, finalEnd = end;
            final List<String> subOpenids = openids.subList(finalStart, finalEnd);
            //使用线程池同步数据
            TaskExcutor.submit(() -> {
                logger.info("WxBlacklistService.syncWxUsersBlacklist 同步批次:【{}--{}-{}】，数量：{}"
                        , batchId, finalStart, finalEnd, subOpenids.size());
                wxMpService.switchover(appid);
                List<WxMpUser> wxMpUsers = null;//批量获取用户信息，每次最多100个

                try {
                    wxMpUsers = wxMpUserService.userInfoList(subOpenids);
                } catch (WxErrorException e) {
                    e.printStackTrace();
                }

                if (wxMpUsers != null && !wxMpUsers.isEmpty()) {
                    List<WxUser> wxUsers = wxMpUsers.parallelStream().map(item -> new WxUser(item, appid, 1)).collect(Collectors.toList());
//                    更新黑名单的信息
                    wxUserService.saveOrUpdateBatch(wxUsers);
                }
            });
            start = end;
            end = Math.min(end + 100, openids.size());
        }
        logger.info("WxBlacklistService.syncWxUsersBlacklist 批次：{}处理完成", batchId);
    }
//                    更新黑名单的信息

    public void pushToBlacklist(List<String> openidList) throws WxErrorException {
        wxMpService.getBlackListService().pushToBlacklist(openidList);
        WxMpUserService wxMpUserService = wxMpService.getUserService();
        List<WxMpUser> wxMpUsers = wxMpUserService.userInfoList(openidList);
        String appid = wxMpInfo.getAppId();
        if (wxMpUsers != null && !wxMpUsers.isEmpty()) {
            List<WxUser> wxUsers = wxMpUsers.parallelStream().map(item -> new WxUser(item, appid, 1)).collect(Collectors.toList());
//                    数据库更新黑名单的信息
            wxUserService.saveOrUpdateBatch(wxUsers);
        }
    }
//                    取消黑名单的信息

    public void pullFromBlacklist(List<String> openidList) throws WxErrorException {
        wxMpService.getBlackListService().pullFromBlacklist(openidList);
        WxMpUserService wxMpUserService = wxMpService.getUserService();
        List<WxMpUser> wxMpUsers = wxMpUserService.userInfoList(openidList);
        String appid = wxMpInfo.getAppId();
        if (wxMpUsers != null && !wxMpUsers.isEmpty()) {
            List<WxUser> wxUsers = wxMpUsers.parallelStream().map(item -> new WxUser(item, appid, 0)).collect(Collectors.toList());
//                    数据库取消黑名单的信息
            wxUserService.saveOrUpdateBatch(wxUsers);
        }
    }
}
