package com.property.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.property.adapter.MaintenancePeriodsAdapter;
import com.property.base.BaseActivity;
import com.property.enumbase.MessageType;
import com.property.model.MaintenanceModel;
import com.property.ui.codeScan.CaptureActivity;
import com.vk.simpleutil.adapter.XSimpleRecyclerAdapter;
import com.vk.simpleutil.library.XSimpleLogger;
import com.vk.simpleutil.library.XSimpleToast;
import com.vk.simpleutil.view.PullToRefreshRecyclerView;
import com.vk.simpleutil.view.pulltorefresh.lib.PullToRefreshBase;
import com.vk.simpleutil.view.pulltorefresh.lib.extras.IXListViewListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;


public class MaintenancePeriodsActivity extends BaseActivity implements IXListViewListener {
    @InjectView(R.id.tv_maintenanceperiods_periods)
    TextView tvMaintenanceperiodsPeriods;
    @InjectView(R.id.tv_maintenanceperiods_loading)
    TextView tvMaintenanceperiodsLoading;
    @InjectView(R.id.tv_maintenanceperiods_maintenance)
    TextView tvMaintenanceperiodsMaintenance;
    @InjectView(R.id.list)
    PullToRefreshRecyclerView listView;
    MaintenancePeriodsAdapter maintenancePeriodsAdapter;
    List<MaintenanceModel> list = new ArrayList<>();
    int postion = 0;

    @Override
    public int onCreateViewLayouId() {
        return R.layout.maintenanceperiods_main;
    }

    @Override
    public void initAllData() {
        setTitle(getIntent().getStringExtra("title"));
        maintenancePeriodsAdapter = new MaintenancePeriodsAdapter(mContext, list);
        listView.setLayoutManager(new LinearLayoutManager(mContext));
        listView.setPullRefreshLoadEnable(true, true, PullToRefreshBase.Mode.BOTH);
        listView.setOnXListViewListener(this);
        listView.setAdapter(maintenancePeriodsAdapter);
    }

    @Override
    public void initListener() {
        maintenancePeriodsAdapter.setOnItemClickListener(new XSimpleRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MaintenanceModel maintenanceModel = list.get(position);
                if (maintenanceModel.getM_status() == 2) {
                    XSimpleToast.showToast("查看");
                } else {
                    CaptureActivity.launchActivity((Activity) mContext, MessageActivity.REQUEST_CODE_SCANLE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MessageActivity.REQUEST_CODE_SCANLE && resultCode == RESULT_OK && data != null && postion != -1) {
            XSimpleLogger.Log().e("code:" + data.getStringExtra("code"));
            startActivity(new Intent(mContext, DetailActivity.class)
                    .putExtra("code", data.getStringExtra("code")).putExtra("id", list.get(postion).getId())
                    .putExtra("messageType", MessageType.maintenance));
        }

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }
}
