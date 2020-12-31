package com.zhggp.qiqiao.wx.controller;

import com.zhggp.qiqiao.wx.common.utils.R;
import com.zhggp.qiqiao.wx.service.WxUserAnalyzeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.datacube.WxDataCubeUserCumulate;
import me.chanjar.weixin.mp.bean.datacube.WxDataCubeUserSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

//todo 微信只能查七天的 不分页

/**
 * 有数据的几个数据
 * Tue Dec 29 00:00:00 CST 2020
 * Fri Dec 25 00:00:00 CST 2020
 * Mon Dec 21 00:00:00 CST 2020
 */
@RestController
@RequestMapping("/wxUser")
@Api(tags = {"a岑璐要的接口-微信粉丝分析"})
public class WxUserAnalyzeController {
    @Autowired
    WxUserAnalyzeService wxUserAnalyzeService;

    @PostMapping("/getWxUserSummary")
    @ApiOperation(value = "获取用户增减数据")
    public R getWxUserSummary(@RequestParam(name = "beginDate", required = false) Date beginDate
            , @RequestParam(name = "endDate", required = false) Date endDate) throws WxErrorException {
        List<WxDataCubeUserSummary> list = wxUserAnalyzeService.getUserSummary(beginDate, endDate);
        return R.ok().put(list);
    }

    @PostMapping("/getWxUserCumulate")
    @ApiOperation(value = "获取累计用户数据")
    public R getWxUserCumulate(@RequestParam(name = "beginDate", required = false) Date beginDate
            , @RequestParam(name = "endDate", required = false) Date endDate) throws WxErrorException {
        List<WxDataCubeUserCumulate> list = wxUserAnalyzeService.getUserCumulate(beginDate, endDate);

        return R.ok().put(list);
    }
}
