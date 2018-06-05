package com.kitchee.app.helpeo;

import android.os.Bundle;

import com.kitchee.app.helpeo.base.BaseActivity;

/**
 * Created by kitchee on 2018/6/5.
 * desc : 搜索页
 */

public class SearchActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwipeBackEnable(true);
        setContentView(R.layout.activity_search);
    }


}
