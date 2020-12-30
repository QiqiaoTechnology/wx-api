package com.zhggp.qiqiao.wx.db.form;

import com.zhggp.qiqiao.wx.common.utils.Json;
import lombok.Data;

@Data
public class MaterialFileDeleteForm {
    String mediaId;

    @Override
    public String toString() {
        return Json.toJsonString(this);
    }
}
