package com.zhggp.qiqiao.wx.controller;

import com.zhggp.qiqiao.wx.common.utils.R;
import com.zhggp.qiqiao.wx.db.dto.WxMpInfo;
import com.zhggp.qiqiao.wx.db.form.WxUserBatchTaggingForm;
import com.zhggp.qiqiao.wx.db.form.WxUserTagForm;
import com.zhggp.qiqiao.wx.service.WxUserService;
import com.zhggp.qiqiao.wx.service.WxUserTagsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.tag.WxUserTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 公众号用户标签
 */
@RestController
@RequestMapping("/manage/wxUserTags")
@Api(tags = {"a岑璐要的接口-公众号用户标签-管理后台"})
public class WxUserTagsManageController {
    @Autowired
    private WxUserService wxUserService;
    @Autowired
    private WxUserTagsService wxUserTagsService;
    @Autowired
    WxMpInfo wxMpInfo;

    /**
     * 查询用户标签
     */
    @GetMapping("/list")
    //@RequiresPermissions("wx:wxuser:info")
    @ApiOperation(value = "列表")
    public R list() throws WxErrorException {
        String appid = wxMpInfo.getAppId();

        List<WxUserTag> wxUserTags = wxUserTagsService.getWxTags(appid);
        return R.ok().put("list", wxUserTags);
    }

    /**
     * 修改或新增标签
     */
    @PostMapping("/save")
    //@RequiresPermissions("wx:wxuser:save")
    @ApiOperation(value = "保存")
    public R save(@RequestBody WxUserTagForm form) throws WxErrorException {
        Long tagid = form.getId();
        String appid = wxMpInfo.getAppId();

        if (tagid == null || tagid <= 0) {
            wxUserTagsService.creatTag(appid, form.getName());
        } else {
            wxUserTagsService.updateTag(appid, tagid, form.getName());
        }
        return R.ok();
    }

    /**
     * 删除标签
     */
    @PostMapping("/delete/{tagid}")
    //@RequiresPermissions("wx:wxuser:save")
    @ApiOperation(value = "删除标签")
    public R delete(@PathVariable("tagid") Long tagid) throws WxErrorException {
        String appid = wxMpInfo.getAppId();

        if (tagid == null || tagid <= 0) {
            return R.error("标签ID不得为空");
        }
        wxUserTagsService.deleteTag(appid, tagid);
        return R.ok();
    }

    /**
     * 批量给用户打标签
     */
    @PostMapping("/batchTagging")
    //@RequiresPermissions("wx:wxuser:save")
    @ApiOperation(value = "批量给用户打标签")
    public R batchTagging(@RequestBody WxUserBatchTaggingForm form) throws WxErrorException {
        String appid = wxMpInfo.getAppId();

        wxUserTagsService.batchTagging(appid, form.getTagid(), form.getOpenidList());
        return R.ok();
    }

    /**
     * 批量移除用户标签
     */
    @PostMapping("/batchUnTagging")
    //@RequiresPermissions("wx:wxuser:save")
    @ApiOperation(value = "批量移除用户标签")
    public R batchUnTagging(@RequestBody WxUserBatchTaggingForm form) throws WxErrorException {
        String appid = wxMpInfo.getAppId();

        wxUserTagsService.batchUnTagging(appid, form.getTagid(), form.getOpenidList());
        return R.ok();
    }
}
