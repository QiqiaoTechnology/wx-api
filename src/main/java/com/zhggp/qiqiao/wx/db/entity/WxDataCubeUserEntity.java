package com.zhggp.qiqiao.wx.db.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import me.chanjar.weixin.mp.bean.datacube.WxDataCubeUserCumulate;
import me.chanjar.weixin.mp.bean.datacube.WxDataCubeUserSummary;

import java.io.Serializable;
import java.util.Date;

/**
 * 公众号用户数据分析
 */
@Data
@TableName("wx_date_cube_user")
public class WxDataCubeUserEntity implements Serializable {
    private static final long serialVersionUID = -2336654489906694173L;

    private String appid;
    /**
     * 数据记录时间
     */
    @TableId(type = IdType.INPUT)
    private Date refDate;
    /**
     * 用户的渠道，数值代表的含义如下：
     * 0代表其他合计
     * 1代表公众号搜索
     * 17代表名片分享
     * 30代表扫描二维码
     * 51代表支付后关注（在支付完成页）
     * 57代表文章内账号名称
     * 100微信广告
     * 161他人转载
     * 176 专辑页内账号名称
     */
    private Integer userSource;
    /**
     * 新增的用户数量
     */
    private Integer newUser;
    /**
     * 取消关注的用户数量，new_user减去cancel_user即为净增用户数量
     */
    private Integer cancelUser;
    /**
     * 总用户量
     */
    private Integer cumulateUser;
    /**
     * 是否删除（1是；0否）
     */
    private Integer isDeleted;
    /**
     * '创建时间'
     */
    private Date createTime;
    /**
     * 更新时间'
     */
    private Date updateTime;

    public WxDataCubeUserEntity() {

    }

    public WxDataCubeUserEntity(WxDataCubeUserSummary dataCubeUserSummary) {
        this.refDate = dataCubeUserSummary.getRefDate();
        this.newUser = dataCubeUserSummary.getNewUser();
        this.cancelUser = dataCubeUserSummary.getCancelUser();
        this.userSource = dataCubeUserSummary.getUserSource();
    }


    public WxDataCubeUserEntity(WxDataCubeUserCumulate dataCubeUserSummary) {
        this.refDate = dataCubeUserSummary.getRefDate();
        this.cumulateUser = dataCubeUserSummary.getCumulateUser();
    }

}
