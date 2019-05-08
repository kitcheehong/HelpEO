package com.kitchee.app.helpeo.appCommon;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.iflytek.cloud.SpeechUtility;
import com.kitchee.app.helpeo.R;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.LogcatLogStrategy;
import com.orhanobut.logger.PrettyFormatStrategy;

import java.util.logging.Logger;

/**
 * Created by kitchee on 2018/5/30.
 * desc :
 */

public class HelpEOApplication extends Application {
    public static HelpEOApplication helpEOApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        helpEOApplication = this;
        MultiDex.install(this);
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(true)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(3)         // (Optional) How many method line to show. Default 2
                .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
                .tag("kitcheehong")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();
        com.orhanobut.logger.Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
        // 注意： appid 必须和下载的SDK保持一致，否则会出现10407错误
        SpeechUtility.createUtility(HelpEOApplication.this, "appid=" + getString(R.string.app_id));

        //异常处理器
        MyCrashHandler.getInstance().init(this);

    }

    public static HelpEOApplication getInstance(){
        return helpEOApplication;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
