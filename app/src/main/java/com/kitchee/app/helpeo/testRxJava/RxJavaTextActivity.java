package com.kitchee.app.helpeo.testRxJava;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kitchee.app.helpeo.R;
import com.kitchee.app.helpeo.base.BaseActivity;
import com.kitchee.app.helpeo.databinding.ActivityRxjavaTestBinding;

/**
 * Created by kitchee on 2018/6/5.
 * desc : 测试RxJava+Retrofit
 */

public class RxJavaTextActivity extends BaseActivity {

    private ActivityRxjavaTestBinding dataBinding;
    private RecyclerView recyclerView;
    private ZhuangbiAdapter adapter;
    private MyHandler myHandler;//VM


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwipeBackEnable(true);
        dataBinding = DataBindingUtil.setContentView(this,R.layout.activity_rxjava_test);

        myHandler = new MyHandler(this);
        dataBinding.setMyHandler(myHandler);

        recyclerView = dataBinding.recycleView;
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        adapter = new ZhuangbiAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        myHandler.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 移除订阅
        unsubscribe();
    }

    private void unsubscribe() {
        if (myHandler != null){
            myHandler.removeDisposble();
        }
    }
}
