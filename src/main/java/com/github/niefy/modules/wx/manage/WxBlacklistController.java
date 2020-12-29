package com.github.niefy.modules.wx.manage;

import com.github.niefy.common.utils.R;
import com.github.niefy.modules.wx.service.impl.WxBlacklistService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.result.WxMpUserBlacklistGetResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/manage/wxBlacklist")
@Api(tags = {"a岑璐要的接口-公众号黑名单-管理后台"})
public class WxBlacklistController {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private WxBlacklistService wxBlacklistService;

    //todo 获取公众号的黑名单列表
    @GetMapping("/getBlacklist")
    @ApiOperation(value = " 获取公众号的黑名单列表")
    public R getBlacklist(@RequestParam(value = "openid", required = false, defaultValue = "") String openid) throws WxErrorException {
        WxMpUserBlacklistGetResult result = wxBlacklistService.getBlacklist(openid);
        return R.ok().put(result);
    }

    //todo 拉黑用户
    @PostMapping("/pushToBlacklist")
    @ApiOperation(value = " 拉黑用户")
    public R pushToBlacklist(@RequestBody String[] openidList) throws WxErrorException {
        wxBlacklistService.pushToBlacklist(Arrays.asList(openidList));
        return R.ok();
    }

    //todo 取消拉黑用户
    @PostMapping("/pullFromBlacklist")
    @ApiOperation(value = " 取消拉黑用户")
    public R pullFromBlacklist(@RequestBody String[] openidList) throws WxErrorException {
        wxBlacklistService.pullFromBlacklist(Arrays.asList(openidList));
        return R.ok();
    }
}
