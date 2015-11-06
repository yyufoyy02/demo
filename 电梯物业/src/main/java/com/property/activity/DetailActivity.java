package com.property.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.property.base.BaseActivity;
import com.property.model.ElevatorModel;
import com.vk.simpleutil.library.XSimpleTime;

import butterknife.ButterKnife;
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

    ElevatorModel elevatorModel = new ElevatorModel();

    @Override
    public int onCreateViewLayouId() {
        return R.layout.detail_activity;
    }

    @Override
    public void initAllData() {
        setTitle("电梯信息");
        elevatorModel.setId("dt20150912");
        elevatorModel.setAddress("市政府大院2号梯");
        elevatorModel.setBrand("三菱");
        elevatorModel.setProperty_company("惠州物业公司");
        elevatorModel.setMaintenance_company("惠州维保公司");
        elevatorModel.setPhone("13380123456");
        elevatorModel.setLast_time(1420819200);
        elevatorModel.setThis_time(1445270400);
        elevatorModel.setReason("电梯升降系统螺丝损坏、保险烧坏、零件需要更换。");
        edtDetailId.setText(elevatorModel.getId());
        edtDetailAddress.setText(elevatorModel.getAddress());
        edtDetailBrand.setText(elevatorModel.getBrand());
        edtDetailPropertyCompany.setText(elevatorModel.getProperty_company());
        edtDetailMaintenanceCompany.setText(elevatorModel.getMaintenance_company());
        edtDetailPhone.setText(elevatorModel.getPhone());
        edtDetailLasttime.setText(XSimpleTime.getFormatTimeFromTimestamp((long) elevatorModel.getLast_time(), "yyyy-MM-dd"));
        edtDetailThistime.setText(XSimpleTime.getFormatTimeFromTimestamp((long) elevatorModel.getThis_time(), "yyyy-MM-dd"));
        tvReason.setText(elevatorModel.getReason());
    }

    @Override
    public void initListener() {

    }


    @OnClick(R.id.tv_detail_submit)
    void submit(View view) {
        startActivity(new Intent(mContext, DetailEditActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.inject(this);
    }
}
