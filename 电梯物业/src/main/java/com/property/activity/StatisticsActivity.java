package com.property.activity;

import com.property.base.BaseActivity;
import com.vk.simpleutil.view.PullToRefreshRecyclerView;

import butterknife.InjectView;

public class StatisticsActivity extends BaseActivity {
    @InjectView(R.id.list)
    PullToRefreshRecyclerView listView;

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
