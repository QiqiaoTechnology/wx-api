package com.zhggp.qiqiao.wx.controller;

import com.zhggp.qiqiao.wx.common.utils.R;
import com.zhggp.qiqiao.wx.db.dto.WxMpInfo;
import com.zhggp.qiqiao.wx.db.entity.WxUser;
import com.zhggp.qiqiao.wx.db.form.WxUserTaggingForm;
import com.zhggp.qiqiao.wx.service.WxUserService;
import com.zhggp.qiqiao.wx.service.WxUserTagsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.common.error.WxError;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 粉丝标签
 */
@RestController
@RequestMapping("/wxUserTags")
@RequiredArgsConstructor
@Api(tags = {"a岑璐要的接口-微信粉丝标签"})
public class WxUserTagsController {
    @Autowired
    WxUserTagsService wxUserTagsService;
    @Autowired
    WxUserService wxUserService;
    private final WxMpService wxMpService;
    @Autowired
    WxMpInfo wxMpInfo;

    @GetMapping("/userTags")
    @ApiOperation(value = "当前用户的标签")
    public R userTags(@RequestParam("openid") String openid) {
        String appid = wxMpInfo.getAppId();

        if (openid == null) {
            return R.error("none_openid");
        }
        this.wxMpService.switchoverTo(appid);
        WxUser wxUser = wxUserService.getById(openid);
        if (wxUser == null) {
            wxUser = wxUserService.refreshUserInfo(openid, appid);
            if (wxUser == null) {
                return R.error("not_subscribed");
            }
        }
        return R.ok().put(wxUser.getTagidList());
    }

    @PostMapping("/tagging")
    @ApiOperation(value = "给用户绑定标签")
    public R tagging(@RequestParam("openid") String openid, @RequestBody WxUserTaggingForm form) {
        String appid = wxMpInfo.getAppId();
        this.wxMpService.switchoverTo(appid);
        try {
            wxUserTagsService.tagging(form.getTagid(), openid);
        } catch (WxErrorException e) {
            WxError error = e.getError();
            if (50005 == error.getErrorCode()) {//未关注公众号
                return R.error("not_subscribed");
            } else {
                return R.error(error.getErrorMsg());
            }
        }
        return R.ok();
    }

    @PostMapping("/untagging")
    @ApiOperation(value = "解绑标签")
    public R untagging(@RequestParam("openid") String openid, @RequestBody WxUserTaggingForm form) throws WxErrorException {
        String appid = wxMpInfo.getAppId();

        this.wxMpService.switchoverTo(appid);
        wxUserTagsService.untagging(form.getTagid(), openid);
        return R.ok();
    }
}
