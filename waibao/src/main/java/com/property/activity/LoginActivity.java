package com.property.activity;


import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.property.api.UserApi;
import com.property.base.BaseActivity;
import com.property.http.MyJsonDataResponseCacheHandler;
import com.property.model.UserModel;
import com.property.utils.UserDataUtil;
import com.vk.simpleutil.library.XSimpleImage;

import butterknife.InjectView;
import butterknife.annotation.event.OnClick;
import cn.jpush.android.api.JPushInterface;

public class LoginActivity extends BaseActivity {

    @InjectView(R.id.iv_login_bg)
    ImageView ivLoginBg;
    @InjectView(R.id.edt_login_username)
    EditText edtLoginUsername;
    @InjectView(R.id.edt_login_password)
    EditText edtLoginPassword;


    @Override
    public int onCreateViewLayouId() {
        return R.layout.login_activity;
    }

    @Override
    public void initAllData() {
        ivLoginBg.setImageBitmap(XSimpleImage.getResourceToBitmap(R.drawable.login_bg));
        UserDataUtil.getInstance().loginOut();
        edtLoginUsername.setText("");
        edtLoginPassword.setText("");
    }

    @OnClick(R.id.iv_login_login)
    void login(View v) {

        login();
    }

    @Override
    public void initListener() {

    }

    void login() {
        showProgressDialog(mContext, "登录中...");
        UserApi.getInstance().login(mContext, edtLoginUsername.getText().toString(), edtLoginPassword.getText().toString(), new MyJsonDataResponseCacheHandler<UserModel>(UserModel.class, false) {
            @Override
            public void onJsonDataSuccess(UserModel userModel) {
                UserDataUtil.getInstance().login(userModel);
                JPushInterface.setAlias(mContext, userModel.getPhone(), null);
                startActivity(new Intent(mContext, IndexActivity.class));
                finish();
            }

            @Override
            public boolean onJsonCacheData(boolean has) {
                return false;
            }

            @Override
            public void onHttpComplete() {
                super.onHttpComplete();
                dismissProgressDialog();
            }
        });
    }
}
