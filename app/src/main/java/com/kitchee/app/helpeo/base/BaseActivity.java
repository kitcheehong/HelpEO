package com.kitchee.app.helpeo.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.kitchee.app.helpeo.utils.StatusBarUtils;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by kitchee on 2018/5/31.
 * desc : 继承该Activity可以设置滑动返回
 */

public class BaseActivity extends SwipeBackActivity {
    private boolean defaultTranslucent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setTransluentStatusBar(defaultTranslucent);
        AppManager.addActivity(this);

    }

    public void setTransluentStatusBar(boolean isT){
        StatusBarUtils.setTranslucentStatusBar(this, isT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.removeActivity(this);
    }
}
