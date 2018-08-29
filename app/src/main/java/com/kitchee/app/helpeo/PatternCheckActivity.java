package com.kitchee.app.helpeo;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kitchee.app.helpeo.appCommon.HelpEOApplication;
import com.kitchee.app.helpeo.display.ScreenAdaption;
import com.kitchee.app.helpeo.utils.SecurityUtil;
import com.kitchee.app.helpeo.utils.SharePreferencesUtil;
import com.kitchee.app.helpeo.view.GestureLockView;

import java.util.logging.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kitchee on 2018/6/28.
 * desc : 验证手势密码页面
 */

public class PatternCheckActivity extends AppCompatActivity {

    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.gusture_lock_view)
    GestureLockView gestureLockView;
    @BindView(R.id.linearLayout_container)
    LinearLayout linearLayout;
    @BindView(R.id.forget_psw)
    TextView forgetPsw;
    @BindView(R.id.switch_login)
    TextView switchLogin;

    private String savePsw;
    private int count = 0;
    private static final int MAX_SIZE = 5;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern_setting);
        ButterKnife.bind(this);
        linearLayout.setVisibility(View.VISIBLE);
        ScreenAdaption.setCustomDensity(this, HelpEOApplication.helpEOApplication, 360f);
        text.setText("滑动解锁");
        savePsw = getPswFromStorage();
        count = getInputPswTimesFromStorage();
        gestureLockView.setOnLockListener(new GestureLockView.setLockListener() {
            @Override
            public void onSetLockSuccess(int type, String msg, StringBuilder psb) {
                if (type == 1) {
                    if (checkPswLegal(psb.toString())) {
                        // 验证成功,更新个人信息
                        text.setTextColor(Color.BLUE);
                        text.setText("解锁成功！");
                        updatePswInputTimes(0);
                        count = 0;
                        new Handler(new Handler.Callback() {
                            @Override
                            public boolean handleMessage(Message msg) {
                                onBackPressed();
                                return false;
                            }
                        }).sendEmptyMessageDelayed(0, 1000);
                    } else {
                        toastMessage();

                    }

                }
                gestureLockView.updateHitSettingState(true);
            }

            @Override
            public void onSetLockFail(String msg) {
                text.setTextColor(Color.parseColor("#f22417"));
                text.setText(msg);
                gestureLockView.updateHitSettingState(true);
                toastMessage();

            }
        });

        forgetPsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SharePreferencesUtil.getInstance().putGesturePsw(null);
                    SharePreferencesUtil.getInstance().putInputPswTimes(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void toastMessage() {
        count++;
        Log.d("kitcheehong","count = "+count);
        text.setTextColor(Color.RED);
        if (count >= MAX_SIZE) {
            text.setText("你已经连续输入5次错误图案，将锁定");
        } else {
            String mesg = String.format("密码错误，还剩%d次机会", MAX_SIZE - count);
            text.setText(mesg);
        }

    }

    @Override
    public void onBackPressed() {
        updatePswInputTimes(count);
        super.onBackPressed();
    }

    private int getInputPswTimesFromStorage() {
        final int times = SharePreferencesUtil.getInstance().getInputPswTimes();
        return times;
    }

    private String getPswFromStorage() {
        final String result = SharePreferencesUtil.getInstance().getSaveGesturePassword();
        return SecurityUtil.decrypt(result);
    }

    private boolean checkPswLegal(String inputPsw) {
        Log.d("kitchee","inputPsw = "+ inputPsw+",savePsw = "+savePsw);
        return inputPsw.equals(savePsw);
    }

    private void updatePswInputTimes(int count) {
        SharePreferencesUtil.getInstance().putInputPswTimes(count);
    }
}
