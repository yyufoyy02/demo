package com.property.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.property.base.BaseActivity;
import com.property.utils.UserDataUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class UserActivity extends BaseActivity {
    @InjectView(R.id.edt_user_name)
    TextView edtUserName;
    @InjectView(R.id.edt_user_job)
    TextView edtUserJob;
    @InjectView(R.id.edt_user_sex)
    TextView edtUserSex;
    @InjectView(R.id.edt_user_company)
    TextView edtUserCompany;
    @InjectView(R.id.edt_user_phone)
    TextView edtUserPhone;

    @Override
    public int onCreateViewLayouId() {
        return R.layout.user_activity;
    }

    @Override
    public void initAllData() {
        setTitle("个人信息");
        edtUserName.setText(UserDataUtil.getInstance().getUserData().getName());
        edtUserCompany.setText(UserDataUtil.getInstance().getUserData().getCompany());
        edtUserPhone.setText(UserDataUtil.getInstance().getUserData().getPhone());
        edtUserJob.setText(UserDataUtil.getInstance().getUserData().getJob());
        if (UserDataUtil.getInstance().getUserData().getSex() == 0) {
            edtUserSex.setText("男");
        } else {
            edtUserSex.setText("女");
        }
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
