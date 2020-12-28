package com.github.niefy.modules.wx.dto;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.niefy.modules.wx.entity.WxMsg;
import lombok.Data;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.util.WxMpConfigStorageHolder;

import java.io.Serializable;
import java.util.Date;

/**
 * 微信消息
 *
 * @author niefy
 * @date 2020-05-14 17:28:34
 */
@Data
@TableName("wx_msg")
public class WxMsgDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long id;
    private String appid;
    /**
     * 微信用户ID
     */
    private String openid;
    /**
     * 消息方向
     */
    private byte inOut;
    /**
     * 消息类型
     */
    private String msgType;
    /**
     * 账户昵称
     */
    private String nickName;
    /**
     * 账户头像
     */
    private String headImgurl;
    /**
     * 消息详情
     */
    private JSONObject detail;
    /**
     * 创建时间
     */
    private Date createTime;

    public static class WxMsgInOut {
        static final byte IN = 0;
        static final byte OUT = 1;
    }

    public WxMsgDto() {
    }

    public WxMsgDto(WxMsg wxMsg) {
        this.setAppid(wxMsg.getAppid());
        this.setCreateTime(wxMsg.getCreateTime());
        this.setDetail(wxMsg.getDetail());
        this.setId(wxMsg.getId());
        this.setInOut(wxMsg.getInOut());
        this.setMsgType(wxMsg.getMsgType());
        this.setOpenid(wxMsg.getOpenid());
    }

    public WxMsgDto(WxMpXmlMessage wxMessage) {
        this.openid = wxMessage.getFromUser();
        this.appid = WxMpConfigStorageHolder.get();
        this.inOut = WxMsgInOut.IN;
        this.msgType = wxMessage.getMsgType();
        this.detail = new JSONObject();
        Long createTime = wxMessage.getCreateTime();
        this.createTime = createTime == null ? new Date() : new Date(createTime * 1000);
        if (WxConsts.XmlMsgType.TEXT.equals(this.msgType)) {
            this.detail.put("content", wxMessage.getContent());
        } else if (WxConsts.XmlMsgType.IMAGE.equals(this.msgType)) {
            this.detail.put("picUrl", wxMessage.getPicUrl());
            this.detail.put("mediaId", wxMessage.getMediaId());
        } else if (WxConsts.XmlMsgType.VOICE.equals(this.msgType)) {
            this.detail.put("format", wxMessage.getFormat());
            this.detail.put("mediaId", wxMessage.getMediaId());
        } else if (WxConsts.XmlMsgType.VIDEO.equals(this.msgType) ||
                WxConsts.XmlMsgType.SHORTVIDEO.equals(this.msgType)) {
            this.detail.put("thumbMediaId", wxMessage.getThumbMediaId());
            this.detail.put("mediaId", wxMessage.getMediaId());
        } else if (WxConsts.XmlMsgType.LOCATION.equals(this.msgType)) {
            this.detail.put("locationX", wxMessage.getLocationX());
            this.detail.put("locationY", wxMessage.getLocationY());
            this.detail.put("scale", wxMessage.getScale());
            this.detail.put("label", wxMessage.getLabel());
        } else if (WxConsts.XmlMsgType.LINK.equals(this.msgType)) {
            this.detail.put("title", wxMessage.getTitle());
            this.detail.put("description", wxMessage.getDescription());
            this.detail.put("url", wxMessage.getUrl());
        } else if (WxConsts.XmlMsgType.EVENT.equals(this.msgType)) {
            this.detail.put("event", wxMessage.getEvent());
            this.detail.put("eventKey", wxMessage.getEventKey());
        }
    }

    public static WxMsgDto buildOutMsg(String msgType, String openid, JSONObject detail) {
        WxMsgDto wxMsg = new WxMsgDto();
        wxMsg.appid = WxMpConfigStorageHolder.get();
        wxMsg.msgType = msgType;
        wxMsg.openid = openid;
        wxMsg.detail = detail;
        wxMsg.createTime = new Date();
        wxMsg.inOut = WxMsgInOut.OUT;
        return wxMsg;
    }
}
