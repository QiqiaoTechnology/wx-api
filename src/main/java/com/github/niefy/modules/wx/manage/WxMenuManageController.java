package com.github.niefy.modules.wx.manage;

import com.github.niefy.common.utils.R;
import com.github.niefy.modules.wx.dto.WxMpInfo;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.menu.WxMpMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 微信公众号菜单管理
 * 官方文档：https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Creating_Custom-Defined_Menu.html
 * WxJava开发文档：https://github.com/Wechat-Group/WxJava/wiki/MP_自定义菜单管理
 */
//@RestController
//@RequestMapping("/manage/wxMenu")
@RequiredArgsConstructor
//@Api(tags = {"公众号菜单-管理后台"})
public class WxMenuManageController {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    private final WxMpService wxService;
    @Autowired
    private WxMpService wxMpService;
    @Autowired
    WxMpInfo wxMpInfo;

    /**
     * 获取公众号菜单
     */
    @GetMapping("/getMenu")
    @ApiOperation(value = "获取公众号菜单")
    public R getMenu() throws WxErrorException {
        String appid = wxMpInfo.getAppId();
        wxMpService.switchoverTo(appid);
        WxMpMenu wxMpMenu = wxService.getMenuService().menuGet();
        return R.ok().put(wxMpMenu);
    }

    /**
     * 创建、更新菜单
     */
    @PostMapping("/updateMenu")
    //@RequiresPermissions("wx:menu:save")
    @ApiOperation(value = "创建、更新菜单")
    public R updateMenu(@RequestBody WxMenu wxMenu) throws WxErrorException {
        String appid = wxMpInfo.getAppId();
        wxMpService.switchoverTo(appid);
        wxService.getMenuService().menuCreate(wxMenu);
        return R.ok();
    }
}
