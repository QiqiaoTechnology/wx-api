package com.zhggp.qiqiao.wx.common.utils;

/**
 * @AUTHOR wx
 * @DATE 2020/12/18
 * @DESCRIPTION 字符串操作工具类
 **/
public class StringUtil {

    /**
     * 判断为空
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * 判断非空
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

}
