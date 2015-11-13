package com.property.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.property.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.annotation.event.OnClick;

public class SettingActivity extends BaseActivity {


    @OnClick(R.id.rl_setting_modify)
    void mpdify(View v) {
        startActivity(new Intent(mContext, SettingModifyActivity.class));
    }

    @OnClick(R.id.tv_setting_submit)
    void submit(View v) {
        finish();
    }

    @Override
    public int onCreateViewLayouId() {
        return R.layout.setting_activity;
    }

    @Override
    public void initAllData() {
        setTitle("设置");
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
