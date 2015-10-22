package com.property.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.property.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.annotation.event.OnClick;

public class IndexActivity extends BaseActivity {

    @InjectView(R.id.iv_index_avatar)
    ImageView ivIndexAvatar;
    @InjectView(R.id.tv_index_username)
    TextView tvIndexUsername;
    @InjectView(R.id.tv_index_desc)
    TextView tvIndexDesc;


    @Override
    public int onCreateViewLayouId() {
        return R.layout.index_activity;
    }

    @Override
    public void initAllData() {

    }

    @OnClick({R.id.iv_index_settring, R.id.iv_index_message, R.id.iv_index_maintenance, R.id.iv_index_repair, R.id.iv_index_statistics, R.id.iv_index_user})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_index_settring:
                startActivity(new Intent(mContext, SettingActivity.class));
                break;
            case R.id.iv_index_message:
                break;
            case R.id.iv_index_maintenance:
                break;
            case R.id.iv_index_repair:
                break;
            case R.id.iv_index_statistics:
                break;
            case R.id.iv_index_user:
                startActivity(new Intent(mContext, UserActivity.class));
                break;
            default:
                break;
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
