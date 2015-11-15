package com.property.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.property.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.annotation.event.OnClick;

public class CommonLanguageActivity extends BaseActivity {
    @InjectView(R.id.ib_reason_check1)
    ImageButton ibReasonCheck1;
    @InjectView(R.id.ib_reason_check2)
    ImageButton ibReasonCheck2;
    @InjectView(R.id.ib_reason_check3)
    ImageButton ibReasonCheck3;
    @InjectView(R.id.ib_reason_check4)
    ImageButton ibReasonCheck4;
    @InjectView(R.id.ib_reason_check5)
    ImageButton ibReasonCheck5;


    @Override
    public int onCreateViewLayouId() {
        return R.layout.commonlanguageactivity_main;
    }

    @Override
    public void initAllData() {
        setTitle("选择故障常用语");
    }

    @Override
    public void initListener() {

    }

    @OnClick({R.id.tv_complete_submit, R.id.ib_reason_check1, R.id.ib_reason_check2,
            R.id.ib_reason_check3, R.id.ib_reason_check4, R.id.ib_reason_check5})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_reason_check1:

                ibReasonCheck1.setSelected(true);
                ibReasonCheck2.setSelected(false);
                ibReasonCheck3.setSelected(false);
                ibReasonCheck4.setSelected(false);
                ibReasonCheck5.setSelected(false);
                break;
            case R.id.ib_reason_check2:
                ibReasonCheck1.setSelected(false);
                ibReasonCheck2.setSelected(true);
                ibReasonCheck3.setSelected(false);
                ibReasonCheck4.setSelected(false);
                ibReasonCheck5.setSelected(false);
                break;
            case R.id.ib_reason_check3:
                ibReasonCheck1.setSelected(false);
                ibReasonCheck2.setSelected(false);
                ibReasonCheck3.setSelected(true);
                ibReasonCheck4.setSelected(false);
                ibReasonCheck5.setSelected(false);
                break;
            case R.id.ib_reason_check4:
                ibReasonCheck1.setSelected(false);
                ibReasonCheck2.setSelected(false);
                ibReasonCheck3.setSelected(false);
                ibReasonCheck4.setSelected(true);
                ibReasonCheck5.setSelected(false);
                break;
            case R.id.ib_reason_check5:
                ibReasonCheck1.setSelected(false);
                ibReasonCheck2.setSelected(false);
                ibReasonCheck3.setSelected(false);
                ibReasonCheck4.setSelected(false);
                ibReasonCheck5.setSelected(true);
                break;
            case R.id.tv_complete_submit:
                String reason = null;
                if (ibReasonCheck1.isSelected()) {
                    reason = ((TextView) findViewById(R.id.ib_reason_text1)).getText().toString();
                } else if (ibReasonCheck2.isSelected()) {
                    reason = ((TextView) findViewById(R.id.ib_reason_text2)).getText().toString();
                } else if (ibReasonCheck3.isSelected()) {
                    reason = ((TextView) findViewById(R.id.ib_reason_text3)).getText().toString();
                } else if (ibReasonCheck4.isSelected()) {
                    reason = ((TextView) findViewById(R.id.ib_reason_text4)).getText().toString();
                } else if (ibReasonCheck5.isSelected()) {
                    reason = ((TextView) findViewById(R.id.ib_reason_text5)).getText().toString();
                }
                if (reason == null)
                    return;
                setResult(RESULT_OK, new Intent().putExtra("reason", reason));
                finish();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.inject(this);
    }
}
