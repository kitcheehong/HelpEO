package com.kitchee.app.helpeo.appCommon;

import android.app.Application;

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
    }

    public static HelpEOApplication getInstance(){
        return helpEOApplication;
    }
}
