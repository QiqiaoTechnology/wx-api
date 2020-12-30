package com.zhggp.qiqiao.wx.service;

import me.chanjar.weixin.common.error.WxErrorException;

import java.util.List;

public interface WxBlacklistService {
    void syncBlacklist() throws WxErrorException;

    void pushToBlacklist(List<String> openidList) throws WxErrorException;

    void pullFromBlacklist(List<String> openidList) throws WxErrorException;

}
