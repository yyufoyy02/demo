package com.property.activity;

import android.content.Intent;
import android.view.View;

import com.property.base.BaseActivity;
import com.property.ui.codeScan.CaptureActivity;
import com.vk.simpleutil.library.XSimpleLogger;

public class MessageActivity extends BaseActivity {
    private final int REQUEST_CODE_SCANLE = 99;
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
                CaptureActivity.launchActivity(mActivity, REQUEST_CODE_SCANLE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SCANLE && resultCode == RESULT_OK) {
            XSimpleLogger.Log().e("code:" + data.getStringExtra("code"));
            startActivity(new Intent(mContext, DetailActivity.class));
        }

    }

    public enum MessageType {
        maintenance, repair, all
    }
}
