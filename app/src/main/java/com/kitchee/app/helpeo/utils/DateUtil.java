package com.kitchee.app.helpeo.utils;

import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2018/8/24.
 * desc: 格式化时间工具
 */

public class DateUtil {

    public static final String DATE_FORMAT_YMD_E = "yyyy-MM-dd";
    public static final String DATE_FORMAT_YMD_HMS = "yyyy-MM-dd HH:mm:ss";

    public static String format(long time, String format) {
        format = format == null || "".equals(format) ? DATE_FORMAT_YMD_E : format;
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        if (time > 0) {
            return formatter.format(time);
        }
        return null;
    }


}
