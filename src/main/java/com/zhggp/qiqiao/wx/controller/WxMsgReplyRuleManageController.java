package com.zhggp.qiqiao.wx.controller;

import com.zhggp.qiqiao.wx.common.utils.PageUtils;
import com.zhggp.qiqiao.wx.common.utils.R;
import com.zhggp.qiqiao.wx.db.dto.WxMpInfo;
import com.zhggp.qiqiao.wx.db.entity.MsgReplyRule;
import com.zhggp.qiqiao.wx.service.MsgReplyRuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 自动回复规则
 *
 * @author niefy
 * @email niefy@qq.com
 * @date 2019-11-12 18:30:15
 */
@RestController
@RequestMapping("/manage/msgReplyRule")
@Api(tags = {"a岑璐要的接口-公众号自动回复规则-管理后台"})
public class WxMsgReplyRuleManageController {
    @Autowired
    private MsgReplyRuleService msgReplyRuleService;
    @Autowired
    private WxMpService wxMpService;
    @Autowired
    WxMpInfo wxMpInfo;

    /**
     * 列表
     * map{
     * matchValue string
     * currPage int
     * pageSize int
     * }
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表")
    public R list(@RequestParam Map<String, Object> params) {
        String appid = wxMpInfo.getAppId();

        params.put("appid", appid);
        PageUtils page = msgReplyRuleService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{ruleId}")
    //@RequiresPermissions("wx:msgreplyrule:info")
    @ApiOperation(value = "详情")
    public R info(@PathVariable("ruleId") Integer ruleId) {
        MsgReplyRule msgReplyRule = msgReplyRuleService.getById(ruleId);

        return R.ok().put("msgReplyRule", msgReplyRule);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    //@RequiresPermissions("wx:msgreplyrule:save")
    @ApiOperation(value = "保存")
    public R save(@RequestBody MsgReplyRule msgReplyRule) {
        String appid = wxMpInfo.getAppId();
        msgReplyRule.setAppid(appid);

        msgReplyRuleService.save(msgReplyRule);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    //@RequiresPermissions("wx:msgreplyrule:update")
    @ApiOperation(value = "修改")
    public R update(@RequestBody MsgReplyRule msgReplyRule) {
        msgReplyRuleService.updateById(msgReplyRule);

        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    //@RequiresPermissions("wx:msgreplyrule:delete")
    @ApiOperation(value = "删除")
    public R delete(@RequestBody Integer[] ruleIds) {
        msgReplyRuleService.removeByIds(Arrays.asList(ruleIds));

        return R.ok();
    }

}
