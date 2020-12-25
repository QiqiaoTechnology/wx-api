package com.github.niefy.modules.wx.manage;

import com.github.niefy.common.utils.R;
import com.github.niefy.common.utils.StringUtil;
import com.github.niefy.modules.wx.dto.WxMpInfo;
import com.github.niefy.modules.wx.entity.WxAccount;
import com.github.niefy.modules.wx.service.WxAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.mp.api.WxMpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


/**
 * 公众号账号
 *
 * @author niefy
 * @date 2020-06-17 13:56:51
 */
@RestController
@RequestMapping("/manage/wxAccount")
@Api(tags = {"a岑璐要的接口-公众号账号管理后台"})
public class WxAccountManageController {
    @Autowired
    WxMpService wxMpService;
    @Autowired
    private WxAccountService wxAccountService;
    @Autowired
    WxMpInfo wxMpInfo;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 列表
     */
    @GetMapping("/list")
    //@RequiresPermissions("wx:wxaccount:list")
    @ApiOperation(value = "列表")
    public R list() {
        List<WxAccount> list = wxAccountService.list();

        return R.ok().put("list", list);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{appid}")
    //@RequiresPermissions("wx:wxaccount:info")
    @ApiOperation(value = "详情")
    public R info(@PathVariable("id") String appid) {
        WxAccount wxAccount = wxAccountService.getById(appid);

        return R.ok().put("wxAccount", wxAccount);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    //@RequiresPermissions("wx:wxaccount:save")
    @ApiOperation(value = "保存")
    public R save(@RequestBody WxAccount wxAccount) {
        wxAccountService.save(wxAccount);

//        String appid = wxMpInfo.getAppId();
//        String newAppid = wxAccount.getAppid();
//
//        logger.info("WxAccountManageController.save appid={},newAppid={}", appid, newAppid);
//        if (StringUtil.isNotBlank(newAppid) && !appid.equals(newAppid)) {
//            wxMpService.removeConfigStorage(appid);
//            wxMpInfo.setAppId(newAppid);
//        }
//        logger.info("WxAccountManageController.save newAppid={}", wxMpInfo.getAppId());
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    //@RequiresPermissions("wx:wxaccount:delete")
    @ApiOperation(value = "删除")
    public R delete(@RequestBody String[] appids) {
        wxAccountService.removeByIds(Arrays.asList(appids));

        return R.ok();
    }

}
