package com.zhggp.qiqiao.wx.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WxUserAnalyzeServiceImpl extends ServiceImpl<WxDataCubeUserMapper, WxDataCubeUserEntity> implements WxUserAnalyzeService {
    @Autowired
    WxMpService wxMpService;
    @Autowired
    WxMpInfo wxMpInfo;

    @Override
    public List<WxDataCubeUserEntity> getUserSummary(Date beginDate, Date endDate) throws WxErrorException {
        String appid = wxMpInfo.getAppId();
        this.wxMpService.switchoverTo(appid);
        List<WxDataCubeUserEntity> result = new ArrayList<>();
        //todo crud 查询有没有数据
        result = this.list(new QueryWrapper<WxDataCubeUserEntity>()
                .ge("ref_date", beginDate)
                .lt("ref_date", endDate)
        );
        WxMpDataCubeService wxMpDataCubeService = wxMpService.getDataCubeService();
        List<WxDataCubeUserSummary> list = wxMpDataCubeService.getUserSummary(beginDate, endDate);
        if (list.isEmpty()) {
            return result;
        }
        log.info("WxUserAnalyzeServiceImpl.getUserSummary list={}", JSON.toJSONString(list));

        // crud 批量增加或更新
        result = list.stream()
                .map(item -> new WxDataCubeUserEntity(item)).collect(Collectors.toList());
        this.saveOrUpdateBatch(result);
        return result;
    }

    @Override
    public List<WxDataCubeUserEntity> getUserCumulate(Date beginDate, Date endDate) throws WxErrorException {
        String appid = wxMpInfo.getAppId();
        this.wxMpService.switchoverTo(appid);
        List<WxDataCubeUserEntity> result = new ArrayList<>();
        //todo crud 查询有没有数据
        WxMpDataCubeService wxMpDataCubeService = wxMpService.getDataCubeService();
        List<WxDataCubeUserCumulate> list = wxMpDataCubeService.getUserCumulate(beginDate, endDate);
        if (list.isEmpty()) {
            return result;
        }
        log.info("WxUserAnalyzeServiceImpl.getUserCumulate list={}", JSON.toJSONString(list));

        // crud 批量增加或更新
        result = list.stream()
                .map(item -> new WxDataCubeUserEntity(item)).collect(Collectors.toList());
        this.saveOrUpdateBatch(result);
        return result;
    }
}
