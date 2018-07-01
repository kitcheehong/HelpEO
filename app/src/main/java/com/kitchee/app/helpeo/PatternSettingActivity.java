package com.kitchee.app.helpeo;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.kitchee.app.helpeo.appCommon.HelpEOApplication;
import com.kitchee.app.helpeo.display.ScreenAdaption;
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
        ScreenAdaption.setCustomDensity(this, HelpEOApplication.helpEOApplication, 360f);
        gustureLockView.setOnLockListener(new GustureLockView.setLockListener() {
            @Override
            public void onSetLockSuccess(int type) {
                textView.setTextColor(Color.parseColor("#2196f3"));
                if(type == 1){
                    textView.setText("请再次绘制解锁图案");
                }else{
                    textView.setText(("手势图案设置成功！"));
                    new Handler(new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            onBackPressed();
                            return false;
                        }
                    }).sendEmptyMessageDelayed(0,1500);

                }
            }

            @Override
            public void onSetLockFail(String msg) {

                textView.setTextColor(Color.parseColor("#f22417"));
                textView.setText(msg);
            }
        });
    }

}
