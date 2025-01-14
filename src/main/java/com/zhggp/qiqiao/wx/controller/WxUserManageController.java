package com.zhggp.qiqiao.wx.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhggp.qiqiao.wx.common.utils.PageUtils;
import com.zhggp.qiqiao.wx.common.utils.R;
import com.zhggp.qiqiao.wx.db.dto.WxMpInfo;
import com.zhggp.qiqiao.wx.db.entity.WxUser;
import com.zhggp.qiqiao.wx.service.WxUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 用户表
 *
 * @author niefy
 * @email niefy@qq.com
 * @date 2020-03-07 13:55:23
 */
@RestController
@RequestMapping("/manage/wxUser")
@Api(tags = {"a岑璐要的接口-公众号粉丝-管理后台"})
public class WxUserManageController {
    @Autowired
    private WxUserService userService;
    @Autowired
    WxMpInfo wxMpInfo;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 列表
     */
    @GetMapping("/list")
    //@RequiresPermissions("wx:wxuser:list")
    @ApiOperation(value = "列表")
//    public R list(@CookieValue String appid, @RequestParam Map<String, Object> params) {
    public R list(
//            @RequestParam("appid") String appid
//            ,@RequestParam("totalCount") int totalCount
//            ,@RequestParam("totalPage") int totalPage
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize
            , @RequestParam(value = "currPage", required = false, defaultValue = "1") int currPage) {
        Map<String, Object> params = new HashMap<>();
//        params.put("totalCount", totalCount);
//        params.put("totalPage", totalPage);
        String appid = wxMpInfo.getAppId();
        params.put("pageSize", pageSize);
        params.put("currPage", currPage);
        params.put("appid", appid);
        IPage<WxUser> pages = userService.queryPage(params);
        PageUtils page = new PageUtils(pages);

        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @PostMapping("/listByIds")
    //@RequiresPermissions("wx:wxuser:list")
    @ApiOperation(value = "列表-ID查询")
    public R listByIds(@RequestBody String[] openids) {

        List<WxUser> users = userService.listByIds(Arrays.asList(openids));
        return R.ok().put(users);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{openid}")
    //@RequiresPermissions("wx:wxuser:info")
    @ApiOperation(value = "详情")
    public R info(@PathVariable("openid") String openid) {
        WxUser wxUser = userService.getById(openid);

        return R.ok().put("wxUser", wxUser);
    }

    /**
     * 同步用户列表
     */
    @PostMapping("/syncWxUsers")
    //@RequiresPermissions("wx:wxuser:save")
    @ApiOperation(value = "同步用户列表到数据库")
    public R syncWxUsers() {
        String appid = wxMpInfo.getAppId();
        userService.syncWxUsers(appid);

        return R.ok("任务已建立");
    }


//    /**
//     * 删除
//     */
//    @PostMapping("/delete")
//    //@RequiresPermissions("wx:wxuser:delete")
//    @ApiOperation(value = "删除")
//    public R delete(@RequestBody String[] ids) {
//        userService.removeByIds(Arrays.asList(ids));
//
//        return R.ok();
//    }

}
