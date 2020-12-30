package com.github.niefy.modules.wx.manage;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.niefy.common.utils.PageUtils;
import com.github.niefy.common.utils.R;
import com.github.niefy.modules.wx.dto.WxMpInfo;
import com.github.niefy.modules.wx.entity.WxUser;
import com.github.niefy.modules.wx.service.WxBlacklistService;
import com.github.niefy.modules.wx.service.WxUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.error.WxErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

//todo 修改消息的接口 在黑名单里就不显示消息
//todo 修改粉丝的接口 粉丝页面里多加一个粉丝状态
// 黑名单接口可以通过微信的服务器修改，所以应该以微信服务器的消息为基准
@RestController
@RequestMapping("/manage/wxBlacklist")
@Api(tags = {"a岑璐要的接口-公众号黑名单-管理后台"})
public class WxBlacklistController {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    WxMpInfo wxMpInfo;
    @Autowired
    private WxBlacklistService wxBlacklistService;
    @Autowired
    private WxUserService userService;

    /**
     * 列表
     */
    @GetMapping("/list")
    //@RequiresPermissions("wx:wxuser:list")
    @ApiOperation(value = "列表")
    public R list(@RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize
            , @RequestParam(value = "currPage", required = false, defaultValue = "1") int currPage) {
        Map<String, Object> params = new HashMap<>();
        String appid = wxMpInfo.getAppId();
        params.put("pageSize", pageSize);
        params.put("currPage", currPage);
        params.put("appid", appid);
        IPage<WxUser> pages = userService.queryBlacklistPage(params);
        PageUtils page = new PageUtils(pages);

        return R.ok().put("page", page);
    }

    //同步公众号的黑名单列表 一次拉10000个 一次拉完全部
    @GetMapping("/syncBlacklist")
    @ApiOperation(value = "同步公众号的黑名单列表")
    public R syncBlacklist() throws WxErrorException {
        wxBlacklistService.syncBlacklist();
        return R.ok();
    }

    @PostMapping("/pushToBlacklist")
    @ApiOperation(value = " 拉黑用户")
    public R pushToBlacklist(@RequestBody String[] openidList) throws WxErrorException {
        wxBlacklistService.pushToBlacklist(Arrays.asList(openidList));
        return R.ok();
    }

    @PostMapping("/pullFromBlacklist")
    @ApiOperation(value = " 取消拉黑用户")
    public R pullFromBlacklist(@RequestBody String[] openidList) throws WxErrorException {
        wxBlacklistService.pullFromBlacklist(Arrays.asList(openidList));
        return R.ok();
    }
}
