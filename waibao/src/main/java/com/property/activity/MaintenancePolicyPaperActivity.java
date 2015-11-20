package com.property.activity;


import com.property.base.BaseActivity;

public class MaintenancePolicyPaperActivity extends BaseActivity {
    @Override
    public int onCreateViewLayouId() {
        return R.layout.maintenancepolicypaperactivity_main;
    }

    @Override
    public void initAllData() {
        setTitle("维保细则");
    }

    @Override
    public void initListener() {

    }
}
