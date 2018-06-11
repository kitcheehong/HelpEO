package com.kitchee.app.helpeo.base;

import android.os.Bundle;

import com.kitchee.app.helpeo.utils.StatusBarUtils;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by kitchee on 2018/5/31.
 * desc :
 */

public class BaseActivity extends SwipeBackActivity {
    private boolean defaultTranslucent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setTransluentStatusBar(defaultTranslucent);

    }

    public void setTransluentStatusBar(boolean isT){
        StatusBarUtils.setTranslucentStatusBar(this, isT);
    }
}
