package com.kitchee.app.helpeo.utils;

import android.widget.Toast;

import com.kitchee.app.helpeo.appCommon.HelpEOApplication;

/**
 * Created by kitchee on 2018/8/28.
 * desc:
 */

public class ToastUtil {

    private static Toast mToast = Toast.makeText(HelpEOApplication.helpEOApplication.getApplicationContext(),"",Toast.LENGTH_LONG);

    public static void showLongToast(String message){
        mToast.setText(message);
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.show();
    }

    public static void showShortToast(String message){
        mToast.setText(message);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }


}
