package com.kitchee.app.helpeo.utils;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.kitchee.app.helpeo.appCommon.HelpEOApplication;

/**
 * Created by kitchee on 2018/5/31.
 * desc : 公用的util类
 */

public class CommonUtil {

    /**
     *
     * @param resId
     * @return drawable
     */
    public static Drawable getDrawable(int resId){
        return ContextCompat.getDrawable(HelpEOApplication.getInstance().getApplicationContext(),resId);
    }
}
