package com.property.activity;

import android.content.Intent;
import android.view.View;

import com.property.base.BaseActivity;

public class MessageActivity extends BaseActivity {
    public enum MessageType {
        maintenance, repair, all
    }

    MessageType type;

    @Override
    public int onCreateViewLayouId() {
        return R.layout.message_activity;
    }

    @Override
    public void initAllData() {
        type = (MessageType) getIntent().getSerializableExtra("type");
        switch (type) {
            case all:
                setTitle("消息");
                break;
            case maintenance:
                setTitle("维保单");
                break;
            case repair:
                setTitle("保修单");
                break;
        }

    }

    @Override
    public void initListener() {
        findViewById(R.id.toolbar).findViewById(R.id.search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, DetailActivity.class));
            }
        });
    }
}
