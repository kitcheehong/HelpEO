package com.kitchee.app.helpeo.login;

import android.os.Bundle;

import com.kitchee.app.helpeo.R;
import com.kitchee.app.helpeo.base.BaseActivity;

/**
 * Created by kitchee on 2018/8/27.
 * desc:
 */

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setSwipeBackEnable(false);
    }


}
