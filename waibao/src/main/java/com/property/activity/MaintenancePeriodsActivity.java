package com.property.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.property.adapter.MaintenancePeriodsAdapter;
import com.property.adapter.SignModel;
import com.property.api.MaintenanceApi;
import com.property.base.BaseActivity;
import com.property.enumbase.MessageType;
import com.property.enumbase.UpdateType;
import com.property.http.MyJsonDataResponseCacheHandler;
import com.property.model.MaintenanceModel;
import com.property.model.PlanModel;
import com.property.ui.codeScan.CaptureActivity;
import com.vk.simpleutil.adapter.XSimpleRecyclerAdapter;
import com.vk.simpleutil.library.XSimpleLogger;
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
    PlanModel planModel;

    @Override
    public int onCreateViewLayouId() {
        return R.layout.maintenanceperiods_main;
    }

    @Override
    public void initAllData() {
        planModel = getIntent().getParcelableExtra("planModel");
        setTitle(planModel.getPlan_name());
        maintenancePeriodsAdapter = new MaintenancePeriodsAdapter(mContext, list);
        listView.setLayoutManager(new LinearLayoutManager(mContext));
        listView.setPullRefreshLoadEnable(true, true, PullToRefreshBase.Mode.BOTH);
        listView.setOnXListViewListener(this);
        listView.setAdapter(maintenancePeriodsAdapter);
        tvMaintenanceperiodsPeriods.setText(planModel.getOk_count() + "/" + planModel.getLifts_count());
        if (planModel.getStatus() == 2) {
            tvMaintenanceperiodsLoading.setText("已完成");
        } else {
            tvMaintenanceperiodsLoading.setText("正在进行中");
        }
        tvMaintenanceperiodsMaintenance.setText("开始维保");
    }

    @Override
    public void initListener() {
        tvMaintenanceperiodsMaintenance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CaptureActivity.launchActivity((Activity) mContext, MessageActivity.REQUEST_CODE_SCANLE);
            }
        });
        maintenancePeriodsAdapter.setOnItemClickListener(new XSimpleRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MaintenanceModel maintenanceModel = list.get(position);
                if (maintenanceModel.getM_status() == 2) {
                    startActivity(new Intent(mContext, DetailCompleteActivity.class).putExtra("id", maintenanceModel.getId()).putExtra("messageType", MessageType.maintenance));
                } else {
                    MaintenanceApi.getInstance().sign(mContext, maintenanceModel.getId(), planModel.getId(), new MyJsonDataResponseCacheHandler<SignModel>(SignModel.class, false) {
                        @Override
                        public void onJsonDataSuccess(SignModel object) {

                        }

                        @Override
                        public boolean onJsonCacheData(boolean has) {
                            return false;
                        }
                    });
                }
            }
        });
    }

    void getList(final UpdateType updateType) {
        String id = null;
        if (updateType == UpdateType.top) {
            if (list.isEmpty())
                showProgressDialog(mContext);
            id = null;
        } else {
            id = list.get(list.size() - 1).getId();
        }
        MaintenanceApi.getInstance().getPlanList(mContext, id, new MyJsonDataResponseCacheHandler<List<MaintenanceModel>>(MaintenanceModel.class, list.isEmpty()) {
            @Override
            public void onJsonDataSuccess(List<MaintenanceModel> object) {
                if (updateType == UpdateType.top)
                    list.clear();
                list.addAll(object);
                listView.notifyDataSetChanged();
            }

            @Override
            public void onHttpComplete() {
                super.onHttpComplete();
                listView.onRefreshComplete();
                dismissProgressDialog();
            }

            @Override
            public boolean onJsonCacheData(boolean has) {
                return false;
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
        getList(UpdateType.top);
    }

    @Override
    public void onLoadMore() {
        getList(UpdateType.bottom);
    }
}
