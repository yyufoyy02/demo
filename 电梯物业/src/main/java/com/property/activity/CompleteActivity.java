package com.property.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.property.base.BaseActivity;

import butterknife.InjectView;
import butterknife.annotation.event.OnClick;

/**
 * Created by Administrator on 2015/11/7.
 */
public class CompleteActivity extends BaseActivity {
    @InjectView(R.id.edt_detail_say)
    EditText edtDetailSay;
    @InjectView(R.id.tv_complete_yes)
    TextView tvCompleteYes;
    @InjectView(R.id.tv_complete_no)
    TextView tvCompleteNo;
    @InjectView(R.id.edt_complete_name)
    EditText edtCompleteName;
    @InjectView(R.id.edt_complete_price)
    EditText edtCompletePrice;


    @Override
    public int onCreateViewLayouId() {
        return R.layout.complete_activity;
    }

    @Override
    public void initAllData() {
        setTitle("确认完成");
        tvCompleteNo.setSelected(true);
    }

    @Override
    public void initListener() {

    }

    @OnClick(R.id.tv_complete_submit)
    void submit(View v) {
        finish();
    }

    @OnClick({R.id.tv_complete_no, R.id.tv_complete_yes})
    void select(View v) {
        switch (v.getId()) {
            case R.id.tv_complete_yes:
                tvCompleteYes.setSelected(true);
                tvCompleteNo.setSelected(false);
                break;
            case R.id.tv_complete_no:
                tvCompleteYes.setSelected(false);
                tvCompleteNo.setSelected(true);
                break;
        }
    }
}
