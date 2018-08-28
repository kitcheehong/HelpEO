package com.kitchee.app.helpeo.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kitchee.app.helpeo.MainActivity;
import com.kitchee.app.helpeo.R;
import com.kitchee.app.helpeo.base.BaseActivity;
import com.kitchee.app.helpeo.customservicerobot.AutoChatActivity;
import com.kitchee.app.helpeo.utils.PerfectClickListener;
import com.kitchee.app.helpeo.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;



/**
 * Created by kitchee on 2018/8/27.
 * desc:
 */

public class LoginActivity extends BaseActivity implements ILoginView{

    @BindView(R.id.et_user_name)
    EditText etUserName;
    @BindView(R.id.iv_clear_input)
    ImageView ivClearInput;
    @BindView(R.id.et_user_pwd)
    EditText etUserPwd;
    @BindView(R.id.iv_pwd)
    ImageView ivPwd;
    @BindView(R.id.register)
    TextView register;
    @BindView(R.id.forget_psw)
    TextView forgetPsw;
    @BindView(R.id.login_btn)
    Button loginBtn;

    ILoginPresenter loginPresenter;
    boolean isShowPwd = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setSwipeBackEnable(false);

        loginPresenter = new LoginPresenter(this);

        setListener();

    }

    private void setListener() {
        ivPwd.setOnClickListener(listener);
        ivClearInput.setOnClickListener(listener);
        register.setOnClickListener(listener);
        forgetPsw.setOnClickListener(listener);
        loginBtn.setOnClickListener(listener);

        etUserName.addTextChangedListener(watcher);
        etUserPwd.addTextChangedListener(watcher);
    }


    //------------------------------------------更新view---------------------------------//
    @Override
    public void onLoginSuccessRefresh() {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLoginFailRefresh() {
        ToastUtil.showLongToast("登陆失败");
    }

    @Override
    public void showProgress() {

    }


    @Override
    public void hideProgress() {

    }


    private PerfectClickListener listener = new PerfectClickListener() {
        @Override
        protected void onNoDoubleClick(View v) {
            switch (v.getId()){
                case R.id.iv_clear_input:
                    etUserName.setText("");
                    break;
                case R.id.iv_pwd:
                    if(isShowPwd){
                        // 密码切换到隐藏状态，并且图片更换为闭眼
                        etUserPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        ivPwd.setImageResource(R.mipmap.show_psw_press);
                    }else{
                        etUserPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        ivPwd.setImageResource(R.mipmap.show_psw);
                    }
                    break;
                case R.id.register:

                    break;
                case R.id.forget_psw:

                    break;
                case R.id.login_btn:
                    String userName = etUserName.getText().toString().trim();
                    String userPwd = etUserPwd.getText().toString().trim();
                    if(checkInputFinish(userName,userPwd)){
                        loginPresenter.onValidateLoginInfo(userName,userPwd);
                    }
                    break;

            }
        }
    };

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(etUserPwd.isFocused()){
                if(s.length() > 0 || etUserName.getText().toString().trim().length() > 0){
                    loginBtn.setEnabled(true);
                }
            }
            if(etUserName.isFocused()){
                if(s.length() > 0 || etUserPwd.getText().toString().trim().length() > 0){
                    loginBtn.setEnabled(true);
                }
            }
        }
    };


    private boolean checkInputFinish(String userName, String userPwd) {
        if (TextUtils.isEmpty(userName)){
            ToastUtil.showLongToast("请输入用户名");
            return false;
        }
        if(TextUtils.isEmpty(userPwd)){
            ToastUtil.showLongToast("请输入登陆密码");
            return false;
        }

        return true;
    }
}
