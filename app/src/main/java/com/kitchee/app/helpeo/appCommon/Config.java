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
}
