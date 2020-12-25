package com.github.niefy.modules.wx.dto;

import lombok.Data;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialNewsBatchGetResult;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

@Data
public class MaterialNewsDto implements Serializable {
    private static final long serialVersionUID = -1617952797921001666L;
    private int totalCount;
    private int itemCount;
    private List<HashMap> items;
}
