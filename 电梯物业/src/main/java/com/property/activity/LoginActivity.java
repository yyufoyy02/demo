package com.property.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.property.base.BaseActivity;
import com.property.model.UserModel;
import com.property.utils.UserDataUtil;
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
        UserDataUtil.getInstance().loginOut();
    }

    @OnClick(R.id.iv_login_login)
    void login(View v) {
        UserModel userModel=new UserModel();
        userModel.setDepartment("惠州维保公司");
        userModel.setCompany("惠州维保公司");
        userModel.setSex(0);
        userModel.setJob("高级维护员");
        userModel.setName("张三");
        userModel.setStaff_id("1");
        userModel.setPhone("13380123456");
        UserDataUtil.getInstance().login(userModel);
        startActivity(new Intent(mContext, IndexActivity.class));
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
