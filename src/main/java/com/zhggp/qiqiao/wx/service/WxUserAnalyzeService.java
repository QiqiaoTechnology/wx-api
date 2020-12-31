package com.zhggp.qiqiao.wx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhggp.qiqiao.wx.db.entity.WxDataCubeUserEntity;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.datacube.WxDataCubeUserCumulate;
import me.chanjar.weixin.mp.bean.datacube.WxDataCubeUserSummary;

import java.util.Date;
import java.util.List;

public interface WxUserAnalyzeService extends IService<WxDataCubeUserEntity> {
    List<WxDataCubeUserSummary> getUserSummary(Date beginDate, Date endDate) throws WxErrorException;

    List<WxDataCubeUserCumulate> getUserCumulate(Date beginDate, Date endDate) throws WxErrorException;
}
