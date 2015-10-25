package com.property.activity;


import android.content.Intent;
import android.view.View;

import com.property.base.BaseActivity;

import butterknife.annotation.event.OnClick;

public class DetailActivity extends BaseActivity {
    @Override
    public int onCreateViewLayouId() {
        return R.layout.detail_activity;
    }

    @Override
    public void initAllData() {
        setTitle("电梯信息");
    }

    @Override
    public void initListener() {

    }



    @OnClick(R.id.tv_detail_submit)
    void submit(View view) {
        startActivity(new Intent(mContext, DetailEditActivity.class));
    }
}
