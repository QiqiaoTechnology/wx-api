package com.github.niefy.modules.wx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.niefy.common.utils.DateUtils;
import com.github.niefy.common.utils.PageUtils;
import com.github.niefy.common.utils.Query;
import com.github.niefy.modules.wx.dao.WxMsgMapper;
import com.github.niefy.modules.wx.entity.WxMsg;
import com.github.niefy.modules.wx.service.WxMsgService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import static com.github.niefy.common.utils.DateUtils.DATE_TIME_PATTERN2;


@Service("wxMsgService")
public class WxMsgServiceImpl extends ServiceImpl<WxMsgMapper, WxMsg> implements WxMsgService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) throws ParseException {
        String msgTypes = (String) params.get("msgTypes");
        String startTime = (String) params.get("startTime");

        Date endTimeDate = DateUtils.parse(startTime, DATE_TIME_PATTERN2);
        endTimeDate = DateUtils.addDateDays(endTimeDate, 1);
        String endTime = DateUtils.format(endTimeDate, DATE_TIME_PATTERN2);

        String openid = (String) params.get("openid");
        String appid = (String) params.get("appid");
        int pageSize = 10;
        if (null != params.get("pageSize")) {
            pageSize = Integer.parseInt((String) params.get("pageSize"));
        }

        int currPage = 1;
        if (null != params.get("currPage")) {
            currPage = Integer.parseInt((String) params.get("currPage"));

        }
        IPage<WxMsg> page = this.page(
                new Query<WxMsg>().getPage(params).setCurrent(currPage).setSize(pageSize),
                new QueryWrapper<WxMsg>()
                        .eq(StringUtils.hasText(appid), "appid", appid)
                        .in(StringUtils.hasText(msgTypes), "msg_type", Arrays.asList(msgTypes.split(",")))
                        .eq(StringUtils.hasText(openid), "openid", openid)
                        .ge(StringUtils.hasText(startTime), "create_time", startTime)
                        .lt(StringUtils.hasText(startTime), "create_time", endTime)
        );

        return new PageUtils(page);
    }

    /**
     * 记录msg，异步入库
     *
     * @param msg
     */
    @Override
    @Async
    public void addWxMsg(WxMsg msg) {
        this.baseMapper.insert(msg);
    }

}