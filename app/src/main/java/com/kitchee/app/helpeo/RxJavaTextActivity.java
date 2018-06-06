package com.kitchee.app.helpeo;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.kitchee.app.helpeo.R;
import com.kitchee.app.helpeo.base.BaseActivity;
import com.kitchee.app.helpeo.databinding.ActivityRxjavaTestBinding;

/**
 * Created by kitchee on 2018/6/5.
 * desc : 测试RxJava+Retrofit
 */

public class RxJavaTextActivity extends BaseActivity {

    private ActivityRxjavaTestBinding dataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwipeBackEnable(true);
        dataBinding = DataBindingUtil.setContentView(this,R.layout.activity_rxjava_test);


    }
}
