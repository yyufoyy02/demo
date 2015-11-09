package com.property.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.property.api.FaultApi;
import com.property.base.BaseActivity;
import com.property.http.MySimpleJsonDataResponseCacheHandler;
import com.vk.simpleutil.library.XSimpleToast;

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
    String id;

    @Override
    public int onCreateViewLayouId() {
        return R.layout.complete_activity;
    }

    @Override
    public void initAllData() {
        setTitle("确认完成");
        id = getIntent().getStringExtra("id");
        tvCompleteNo.setSelected(true);
    }

    @Override
    public void initListener() {

    }

    @OnClick(R.id.tv_complete_submit)
    void submit(View v) {
        finish();
    }

    void complete() {
        MySimpleJsonDataResponseCacheHandler mySimpleJsonDataResponseCacheHandler = new MySimpleJsonDataResponseCacheHandler(new MySimpleJsonDataResponseCacheHandler.OnJsonCallBack() {
            @Override
            public void success() {
                XSimpleToast.showToast("上传成功");
                finish();
            }

            @Override
            public void fail() {

            }
        });
        int fault_parts = 0;
        if (tvCompleteYes.isSelected()) {
            fault_parts = 1;
        }
        FaultApi.getInstance().putDeal(mContext, id, edtDetailSay.getText().toString(), fault_parts,
                edtCompleteName.getText().toString(), edtCompletePrice.getText().toString(), mySimpleJsonDataResponseCacheHandler);
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
