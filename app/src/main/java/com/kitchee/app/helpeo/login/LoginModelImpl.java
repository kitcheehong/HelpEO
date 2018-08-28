package com.kitchee.app.helpeo.login;

import android.app.Activity;
import android.os.Handler;

/**
 * Created by kitchee on 2018/8/27.
 * desc:
 */

public class LoginModelImpl implements ILoginModel {

    @Override
    public void login(String userNme, String Pwd, final LoginListener listener) {
        // 在这里暂时模拟
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (listener != null){
                    listener.onLoginSuccess();
                }
            }
        }, 1 * 1000);
    }


}
