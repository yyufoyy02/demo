package com.property.activity;


import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.property.api.UserApi;
import com.property.base.BaseActivity;
import com.property.http.MySimpleJsonDataResponseCacheHandler;
import com.vk.simpleutil.library.XSimpleToast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.annotation.event.OnClick;

public class SettingModifyActivity extends BaseActivity {
    @InjectView(R.id.edt_settingmodify_oldpassword)
    EditText edtSettingmodifyOldpassword;
    @InjectView(R.id.edt_settingmodify_newpassword)
    EditText edtSettingmodifyNewpassword;
    @InjectView(R.id.edt_settingmodify_newpassword2)
    EditText edtSettingmodifyNewpassword2;


    @Override
    public int onCreateViewLayouId() {
        return R.layout.settingmodify_activity;
    }

    @OnClick(R.id.tv_settingmodify_submit)
    void submit(View v) {
        showProgressDialog(mContext, "请稍候...");
        UserApi.getInstance().password(mContext, edtSettingmodifyOldpassword.getText().toString(),
                edtSettingmodifyNewpassword.getText().toString(), edtSettingmodifyNewpassword2.getText().toString(),
                new MySimpleJsonDataResponseCacheHandler(new MySimpleJsonDataResponseCacheHandler.OnJsonCallBack() {
                    @Override
                    public void success() {
                        XSimpleToast.showToast("密码修改成功!");
                        dismissProgressDialog();
                        finish();

                    }

                    @Override
                    public void fail() {
                        dismissProgressDialog();
                    }

                }));
    }

    @Override
    public void initAllData() {
        setTitle("密码修改");
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
