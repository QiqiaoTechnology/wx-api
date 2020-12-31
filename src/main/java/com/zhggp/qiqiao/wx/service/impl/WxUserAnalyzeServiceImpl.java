package com.zhggp.qiqiao.wx.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhggp.qiqiao.wx.db.dao.WxDataCubeUserMapper;
import com.zhggp.qiqiao.wx.db.dto.WxMpInfo;
import com.zhggp.qiqiao.wx.db.entity.WxDataCubeUserEntity;
import com.zhggp.qiqiao.wx.service.WxUserAnalyzeService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpDataCubeService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.datacube.WxDataCubeUserCumulate;
import me.chanjar.weixin.mp.bean.datacube.WxDataCubeUserSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class WxUserAnalyzeServiceImpl extends ServiceImpl<WxDataCubeUserMapper, WxDataCubeUserEntity> implements WxUserAnalyzeService {
    @Autowired
    WxMpService wxMpService;
    @Autowired
    WxMpInfo wxMpInfo;

    @Override
    public List<WxDataCubeUserSummary> getUserSummary(Date beginDate, Date endDate) throws WxErrorException {
        String appid = wxMpInfo.getAppId();
        this.wxMpService.switchoverTo(appid);

        WxMpDataCubeService wxMpDataCubeService = wxMpService.getDataCubeService();
        List<WxDataCubeUserSummary> list = wxMpDataCubeService.getUserSummary(beginDate, endDate);
        log.info("WxUserAnalyzeServiceImpl.getUserSummary list={}", JSON.toJSONString(list));
        //todo crud

        return list;
    }

    @Override
    public List<WxDataCubeUserCumulate> getUserCumulate(Date beginDate, Date endDate) throws WxErrorException {
        String appid = wxMpInfo.getAppId();
        this.wxMpService.switchoverTo(appid);

        WxMpDataCubeService wxMpDataCubeService = wxMpService.getDataCubeService();
        List<WxDataCubeUserCumulate> list = wxMpDataCubeService.getUserCumulate(beginDate, endDate);
        log.info("WxUserAnalyzeServiceImpl.getUserCumulate list={}", JSON.toJSONString(list));

        //todo crud

        return list;
    }
}
