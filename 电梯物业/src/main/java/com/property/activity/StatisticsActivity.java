package com.property.activity;

import com.property.base.BaseActivity;

public class StatisticsActivity extends BaseActivity {
    @Override
    public int onCreateViewLayouId() {
        return R.layout.statistics_activity;
    }

    @Override
    public void initAllData() {
        setTitle("数据统计");
    }

    @Override
    public void initListener() {

    }
}
