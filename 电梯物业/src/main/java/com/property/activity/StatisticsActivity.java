package com.property.activity;

import android.support.v7.widget.LinearLayoutManager;

import com.property.adapter.StatisticsAdapter;
import com.property.base.BaseActivity;
import com.property.model.ImageModel;
import com.property.model.StatisticsModel;
import com.vk.simpleutil.view.PullToRefreshRecyclerView;
import com.vk.simpleutil.view.pulltorefresh.lib.PullToRefreshBase;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

public class StatisticsActivity extends BaseActivity {
    @InjectView(R.id.list)
    PullToRefreshRecyclerView listView;
    List<StatisticsModel> list = new ArrayList<>();
    StatisticsAdapter statisticsAdapter;

    @Override
    public int onCreateViewLayouId() {
        return R.layout.statistics_activity;
    }

    @Override
    public void initAllData() {
        setTitle("数据统计");
        StatisticsModel statisticsModel1 = new StatisticsModel();
        statisticsModel1.setIcon(new ImageModel("drawable://" + R.drawable.icon_book));
        statisticsModel1.setTitle("个人维保统计");
        statisticsModel1.setThis_month(5);
        statisticsModel1.setThree_month(15);
        statisticsModel1.setThis_year(155);
        statisticsModel1.setColor("#ff7f00");
        StatisticsModel statisticsModel2 = new StatisticsModel();
        statisticsModel2.setIcon(new ImageModel("drawable://" + R.drawable.icon_setting));
        statisticsModel2.setTitle("个人抢修统计");
        statisticsModel2.setThis_month(5);
        statisticsModel2.setThree_month(15);
        statisticsModel2.setThis_year(155);
        statisticsModel2.setColor("#000080");
        list.add(statisticsModel1);
        list.add(statisticsModel2);
        initView();
    }

    private void initView() {
        statisticsAdapter = new StatisticsAdapter(mContext, list);
        listView.setLayoutManager(new LinearLayoutManager(mContext));
        listView.setPullRefreshLoadEnable(false, false, PullToRefreshBase.Mode.BOTH);
        listView.setAdapter(statisticsAdapter);
        listView.notifyDataSetChanged();
    }

    @Override
    public void initListener() {

    }
}
