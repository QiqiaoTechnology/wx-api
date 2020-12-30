package com.zhggp.qiqiao.wx.db.dto;

import lombok.Data;

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
