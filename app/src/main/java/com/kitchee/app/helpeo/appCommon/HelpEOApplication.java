package com.kitchee.app.helpeo.appCommon;

import android.app.Application;
import android.support.multidex.MultiDex;

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
    }

    public static HelpEOApplication getInstance(){
        return helpEOApplication;
    }
}
