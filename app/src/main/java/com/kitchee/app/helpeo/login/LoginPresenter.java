package com.kitchee.app.helpeo.login;

import com.kitchee.app.helpeo.bean.ErrorMessage;

/**
 * Created by kitchee on 2018/8/27.
 * desc:
 */

public class LoginPresenter implements ILoginPresenter ,LoginListener{

    private ILoginView loginView;
    private ILoginModel loginModel;

    public LoginPresenter(ILoginView loginView) {
        this.loginView = loginView;
        loginModel = new LoginModelImpl();
    }

    @Override
    public void onValidateLoginInfo(String userName, String pwd) {
        loginView.showProgress();
        loginModel.login(userName,pwd,this);
    }

    //------------------------登陆的回调----------------------------//
    @Override
    public void onLoginSuccess() {
        // 登陆成功,隐藏进度条，更新页面
        loginView.hideProgress();
        loginView.onLoginSuccessRefresh();

    }

    @Override
    public void onLoginFail(ErrorMessage errorMessage) {
        // 登陆失败，隐藏进度条，更新页面
        loginView.hideProgress();
        loginView.onLoginFailRefresh();
    }
}
