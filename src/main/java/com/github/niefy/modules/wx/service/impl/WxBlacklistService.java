package com.github.niefy.modules.wx.service.impl;

import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpUserBlacklistService;
import me.chanjar.weixin.mp.bean.result.WxMpUserBlacklistGetResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WxBlacklistService {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    WxMpService wxMpService;


    //todo 获取公众号的黑名单列表
    public WxMpUserBlacklistGetResult getBlacklist(String openid) throws WxErrorException {
//为空时，默认从开头拉取。
        return wxMpService.getBlackListService().getBlacklist("");
    }

    //todo 拉黑用户
    public void pushToBlacklist(List<String> openidList) throws WxErrorException {
        wxMpService.getBlackListService().pushToBlacklist(openidList);
    }

    //todo 取消拉黑用户
    public void pullFromBlacklist(List<String> openidList) throws WxErrorException {
        wxMpService.getBlackListService().pullFromBlacklist(openidList);
    }
}
