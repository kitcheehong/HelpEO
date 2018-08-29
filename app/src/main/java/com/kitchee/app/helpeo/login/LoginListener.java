package com.kitchee.app.helpeo.login;

import com.kitchee.app.helpeo.bean.ErrorMessage;

/**
 * Created by kitchee on 2018/8/27.
 * desc:
 */

public interface LoginListener {
    /**
     * 登陆成功
     */
    void onLoginSuccess();
    /**
     * 登陆失败
     */
    void onLoginFail(ErrorMessage errorMessage);
}
