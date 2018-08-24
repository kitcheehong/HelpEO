package com.kitchee.app.helpeo.appCommon;

import android.graphics.Color;

/**
 * Created by kitchee on 2018/6/28.
 * desc :
 */

public class Config {

    public static int defaultNormalColor = Color.GRAY;
    public static int defaultSelectColor = Color.BLUE;
    public static int defaultCorrectColor = Color.GREEN;
    public static int defaultWrongColor = Color.RED;
    public static int defaultLineWidth = 2;
    public static long defaultDelayTime = 1000;
    /** 手势密码点的状态*/
    public static final int POINT_STATE_NORMAL = 0; // 正常状态

    public static final int POINT_STATE_SELECTED = 1; // 按下状态

    public static final int POINT_STATE_SUCCESS = 2; // 正确状态

    public static final int POINT_STATE_WRONG = 3; // 错误状态

    /** 图灵机器人API */

    public static final String URL_KEY = "http://www.tuling123.com/openapi/api";
//http://openapi.tuling123.com/openapi/api/v2
    public static final String APP_KEY = "04e2d6478e9b4f5da308349c162b277b";//此处是你申请的Apikey

    public static final int MESSAGE_TYPE_SEND = 1;

    public static final int MESSAGE_TYPE_RECEIVE = 2;
}
