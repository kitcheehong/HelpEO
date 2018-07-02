package com.kitchee.app.helpeo.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.kitchee.app.helpeo.appCommon.HelpEOApplication;

/**
 * Created by kitchee on 2018/7/2.
 * desc : 偏好设置
 */

public class SharePreferencesUtil {

    private SharedPreferences.Editor mEditor;
    private SharedPreferences mSharePreferences;

    private static SharePreferencesUtil instance;

    public static final String SAVE_GESTURE_PASSWORD = "save_gusture_password";
    public static final String INPUT_GESTURE_PASSWORD_TIMES = "INPUT_GESTURE_PASSWORD_TIMES";

    private SharePreferencesUtil(){
        mSharePreferences = PreferenceManager.getDefaultSharedPreferences(HelpEOApplication.getInstance().getApplicationContext());
        mEditor = mSharePreferences.edit();
    }

    public static SharePreferencesUtil getInstance(){
        if (instance == null){
            synchronized (SharePreferencesUtil.class){
                instance = new SharePreferencesUtil();
            }
        }
        return instance;
    }

    public void putGesturePsw(String strPSW) throws Exception {
        if (mEditor == null){
            throw new Exception("先获取实例,调用getInstance()");
        }
        mEditor.putString(SAVE_GESTURE_PASSWORD,strPSW);
        mEditor.commit();
    }

    public String getSaveGesturePassword(){
        return mSharePreferences.getString(SAVE_GESTURE_PASSWORD,null);
    }

    public void putInputPswTimes(int count){
        mEditor.putInt(INPUT_GESTURE_PASSWORD_TIMES,count);
        mEditor.commit();
    }

    public int getInputPswTimes(){
        return mSharePreferences.getInt(INPUT_GESTURE_PASSWORD_TIMES,0);
    }


}

