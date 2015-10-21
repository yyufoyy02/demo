package com.property.activity;


import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.property.base.BaseActivity;
import com.vk.simpleutil.library.XSimpleImage;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.annotation.event.OnClick;

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
    }

    @OnClick(R.id.iv_login_login)
    void login(View v) {

    }

    @Override
    public void initListener() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.inject(this);
    }
}
