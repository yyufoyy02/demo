package com.property.activity.maintenance;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.property.ActivityForResult;
import com.property.activity.R;
import com.property.adapter.PlanAdapter;
import com.property.api.MaintenanceApi;
import com.property.base.BaseActivity;
import com.property.enumbase.UpdateType;
import com.property.http.MyJsonDataResponseCacheHandler;
import com.property.model.PlanModel;
import com.vk.simpleutil.adapter.XSimpleRecyclerAdapter;
import com.vk.simpleutil.view.PullToRefreshRecyclerView;
import com.vk.simpleutil.view.pulltorefresh.lib.PullToRefreshBase;
import com.vk.simpleutil.view.pulltorefresh.lib.extras.IXListViewListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * 维保
 */
public class MaintenancePlanActivity extends BaseActivity implements IXListViewListener {

    @InjectView(R.id.list)
    PullToRefreshRecyclerView listView;
    PlanAdapter planAdapter;
    List<PlanModel> planModellist = new ArrayList<>();
    String id;

    @Override
    public int onCreateViewLayouId() {
        return R.layout.message_activity;
    }

    @Override
    public void initAllData() {
        setTitle("维保计划");
        planAdapter = new PlanAdapter(mContext, planModellist);
        listView.setLayoutManager(new LinearLayoutManager(mContext));
        listView.setPullRefreshLoadEnable(true, true, PullToRefreshBase.Mode.BOTH);
        listView.setOnXListViewListener(this);
        listView.setAdapter(planAdapter);
        getList(UpdateType.top);
    }

    @Override
    public void initListener() {
        findViewById(R.id.toolbar).findViewById(R.id.search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        planAdapter.setOnItemClickListener(new XSimpleRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(mContext, MaintenancePlanDetailActivity.class)
                        .putExtra("planModel", (Parcelable) planModellist.get(position)));
            }
        });
    }


    void getList(final UpdateType updateType) {
        String id = null;
        if (updateType == UpdateType.top) {
            if (planModellist.isEmpty())
                showProgressDialog();
            id = null;
        } else {
            if (!planModellist.isEmpty())
                id = planModellist.get(planModellist.size() - 1).getId();
        }
        MaintenanceApi.getInstance().getPlanList(mContext, id, new MyJsonDataResponseCacheHandler<List<PlanModel>>(PlanModel.class, planModellist.isEmpty()) {
            @Override
            public void onJsonDataSuccess(List<PlanModel> object) {
                if (updateType == UpdateType.top)
                    planModellist.clear();
                planModellist.addAll(object);
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
    public void onRefresh() {
        getList(UpdateType.top);
    }

    @Override
    public void onLoadMore() {
        getList(UpdateType.bottom);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityForResult.MaintenanceListRefresh) {
            ActivityForResult.MaintenanceListRefresh = false;
            getList(UpdateType.top);
        }
    }
}
