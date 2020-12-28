package com.github.niefy.modules.wx.manage;

import com.alibaba.fastjson.JSON;
import com.github.niefy.common.utils.PageUtils;
import com.github.niefy.common.utils.R;
import com.github.niefy.modules.wx.dto.WxMpInfo;
import com.github.niefy.modules.wx.dto.WxMsgDto;
import com.github.niefy.modules.wx.entity.WxMsg;
import com.github.niefy.modules.wx.entity.WxUser;
import com.github.niefy.modules.wx.form.WxMsgReplyForm;
import com.github.niefy.modules.wx.service.MsgReplyService;
import com.github.niefy.modules.wx.service.WxMsgService;
import com.github.niefy.modules.wx.service.WxUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;


/**
 * 微信消息
 *
 * @author niefy
 * @date 2020-05-14 17:28:34
 */
@Slf4j
@RestController
@RequestMapping("/manage/wxMsg")
@Api(tags = {"a岑璐要的接口-公众号消息记录-管理后台"})
public class WxMsgManageController {
    @Autowired
    private WxMsgService wxMsgService;
    @Autowired
    private MsgReplyService msgReplyService;
    @Autowired
    private WxUserService userService;
    @Autowired
    WxMpInfo wxMpInfo;

    /**
     * 列表
     */
    @GetMapping("/list")
    //@RequiresPermissions("wx:wxmsg:list")
    @ApiOperation(value = "列表")
    public R list(@RequestParam Map<String, Object> params) {
        log.info("WxMsgManageController.list.params={}", JSON.toJSONString(params));

        String appid = wxMpInfo.getAppId();
        params.put("appid", appid);
        PageUtils page = wxMsgService.queryPage(params);
        ArrayList<WxMsg> list = (ArrayList<WxMsg>) page.getList();
        log.info("WxMsgManageController.list.list={}", JSON.toJSONString(list));

        Set<String> openidSet = new HashSet<>();
        for (WxMsg wx : list) {
            openidSet.add(wx.getOpenid());
        }
        log.info("WxMsgManageController.list.openidSet={}", JSON.toJSONString(openidSet));

        List<WxUser> users = userService.listByIds(openidSet);
        log.info("WxMsgManageController.list.users={}", JSON.toJSONString(users));

        HashMap<String, WxUser> map = new HashMap();
        for (WxUser user : users) {
            map.put(user.getOpenid(), user);
        }
        log.info("WxMsgManageController.list.map={}", JSON.toJSONString(map));

        ArrayList<WxMsgDto> tempList = new ArrayList<>();
        for (WxMsg wx : list) {
//            log.info("WxMsgManageController.list.wx={}", JSON.toJSONString(wx));

            WxUser user = map.get(wx.getOpenid());
//            log.info("WxMsgManageController.list.user={}", JSON.toJSONString(user));

            WxMsgDto dto = new WxMsgDto(wx);
            dto.setHeadImgurl(user.getHeadimgurl());
            dto.setNickName(user.getNickname());
            tempList.add(dto);
        }
        log.info("WxMsgManageController.list.tempList={}", JSON.toJSONString(tempList));

        page.setList(tempList);
        log.info("WxMsgManageController.list.page={}", JSON.toJSONString(page));

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    //@RequiresPermissions("wx:wxmsg:info")
    @ApiOperation(value = "详情")
    public R info(@PathVariable("id") Long id) {
        WxMsg wxMsg = wxMsgService.getById(id);

        return R.ok().put("wxMsg", wxMsg);
    }

    /**
     * 回复
     */
    @PostMapping("/reply")
    //@RequiresPermissions("wx:wxmsg:save")
    @ApiOperation(value = "回复")
    public R reply(@RequestBody WxMsgReplyForm form) {

        msgReplyService.reply(form.getOpenid(), form.getReplyType(), form.getReplyContent());
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    //@RequiresPermissions("wx:wxmsg:delete")
    @ApiOperation(value = "删除")
    public R delete(@RequestBody Long[] ids) {
        wxMsgService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
