package com.kitchee.app.helpeo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.kitchee.app.helpeo.view.GustureLockView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kitchee on 2018/6/28.
 * desc : 手势图案设置页面
 */

public class PatternSettingActivity extends AppCompatActivity {
    @BindView(R.id.gusture_lock_view)
    GustureLockView gustureLockView;
    @BindView(R.id.text)
    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern_setting);
        ButterKnife.bind(this);
    }
}
