package com.kitchee.app.helpeo.login;

/**
 * Created by kitchee on 2018/8/27.
 * desc:
 */

public interface ILoginView {
    /**
     * 登录成功刷新
     */
    void onLoginSuccessRefresh();

    /**
     * 登陆失败刷新
     */
    void onLoginFailRefresh();

}
