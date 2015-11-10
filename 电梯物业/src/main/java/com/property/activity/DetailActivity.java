package com.property.activity;


import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.property.api.FaultApi;
import com.property.api.MaintenanceApi;
import com.property.base.BaseActivity;
import com.property.enumbase.MessageType;
import com.property.http.MyJsonDataResponseCacheHandler;
import com.property.model.LiftModel;
import com.vk.simpleutil.library.XSimpleTime;

import butterknife.InjectView;
import butterknife.annotation.event.OnClick;

public class DetailActivity extends BaseActivity {

    @InjectView(R.id.edt_detail_id)
    TextView edtDetailId;
    @InjectView(R.id.edt_detail_address)
    TextView edtDetailAddress;
    @InjectView(R.id.edt_detail_brand)
    TextView edtDetailBrand;
    @InjectView(R.id.edt_detail_property_company)
    TextView edtDetailPropertyCompany;
    @InjectView(R.id.edt_detail_maintenance_company)
    TextView edtDetailMaintenanceCompany;
    @InjectView(R.id.edt_detail_phone)
    TextView edtDetailPhone;
    @InjectView(R.id.edt_detail_lasttime)
    TextView edtDetailLasttime;
    @InjectView(R.id.edt_detail_thistime)
    TextView edtDetailThistime;
    @InjectView(R.id.tv_detail_reason)
    TextView tvReason;
    MessageType messageType;
String id;
    @Override
    public int onCreateViewLayouId() {
        return R.layout.detail_activity;
    }

    @Override
    public void initAllData() {
        setTitle("电梯信息");
        id=getIntent().getStringExtra("id");
        messageType = (MessageType) getIntent().getSerializableExtra("messageType");
        getScan(id, getIntent().getStringExtra("code"));
    }

    void initData(LiftModel liftModel) {
        if (liftModel == null)
            return;
        edtDetailId.setText(liftModel.getLift_code());
        edtDetailAddress.setText(liftModel.getLift_address());
        edtDetailBrand.setText(liftModel.getLift_brand());
        edtDetailPropertyCompany.setText(liftModel.getCustomer());
        edtDetailMaintenanceCompany.setText(liftModel.getCompany());
        edtDetailPhone.setText(liftModel.getPhone());
        edtDetailLasttime.setText(XSimpleTime.getFormatTimeFromTimestamp((long) liftModel.getLast_time(), "yyyy-MM-dd"));
        edtDetailThistime.setText(XSimpleTime.getFormatTimeFromTimestamp((long) liftModel.getStart_time(), "yyyy-MM-dd"));
        tvReason.setText(liftModel.getFault_type());
    }

    void getScan(String id, String code) {
        showProgressDialog(mContext);
        MyJsonDataResponseCacheHandler myJsonDataResponseCacheHandler = new MyJsonDataResponseCacheHandler<LiftModel>(LiftModel.class, false) {
            @Override
            public void onJsonDataSuccess(LiftModel object) {
                initData(object);
            }

            @Override
            public boolean onJsonCacheData(boolean has) {
                return false;
            }

            @Override
            public void onHttpComplete() {
                super.onHttpComplete();
                dismissProgressDialog();
            }
        };
        if (messageType == MessageType.repair) {
            FaultApi.getInstance().scan(mContext, id, code, myJsonDataResponseCacheHandler);
        } else if (messageType == MessageType.maintenance) {
            MaintenanceApi.getInstance().scan(mContext, id, code, myJsonDataResponseCacheHandler);
        }
    }

    @Override
    public void initListener() {

    }


    @OnClick(R.id.tv_detail_submit)
    void submit(View view) {
        startActivity(new Intent(mContext, DetailEditActivity.class) .putExtra("messageType", messageType).putExtra("id",id));
    }

}
