package com.property.activity.fault;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.property.ActivityForResult;
import com.property.activity.DetailActivity;
import com.property.activity.MessageActivity;
import com.property.activity.R;
import com.property.adapter.FaultAdapter;
import com.property.api.FaultApi;
import com.property.base.BaseActivity;
import com.property.enumbase.MessageType;
import com.property.enumbase.UpdateType;
import com.property.http.MyJsonDataResponseCacheHandler;
import com.property.model.FaultModel;
import com.vk.simpleutil.library.XSimpleLogger;
import com.vk.simpleutil.view.PullToRefreshRecyclerView;
import com.vk.simpleutil.view.pulltorefresh.lib.PullToRefreshBase;
import com.vk.simpleutil.view.pulltorefresh.lib.extras.IXListViewListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by Administrator on 2015/11/8.
 */
public class FaultActivity extends BaseActivity implements IXListViewListener {
    List<FaultModel> list = new ArrayList<>();
    @InjectView(R.id.list)
    PullToRefreshRecyclerView listView;
    FaultAdapter faultAdapter;
    String id;

    @Override
    public int onCreateViewLayouId() {
        return R.layout.message_activity;
    }

    @Override
    public void initAllData() {
        setTitle("抢修单");
        faultAdapter = new FaultAdapter(mContext, list);
        listView.setLayoutManager(new LinearLayoutManager(mContext));
        listView.setPullRefreshLoadEnable(true, true, PullToRefreshBase.Mode.BOTH);
        listView.setOnXListViewListener(this);
        listView.setAdapter(faultAdapter);
        getList(UpdateType.top);
    }

    @Override
    public void initListener() {
        findViewById(R.id.toolbar).findViewById(R.id.search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    void getList(final UpdateType updateType) {

        String id = null;
        if (updateType == UpdateType.top) {
            if (list.isEmpty())
                showProgressDialog();
            id = null;
        } else {
            if (!list.isEmpty())
                id = list.get(list.size() - 1).getId();
        }
        FaultApi.getInstance().getList(mContext, id, new MyJsonDataResponseCacheHandler<List<FaultModel>>(FaultModel.class, list.isEmpty()) {
            @Override
            public void onJsonDataSuccess(List<FaultModel> object) {
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
        if (requestCode == MessageActivity.REQUEST_CODE_SCANLE && resultCode == RESULT_OK && data != null && faultAdapter.getScanPostion() != -1) {
            XSimpleLogger.Log().e("code:" + data.getStringExtra("code"));
            Intent intent = new Intent(mContext, DetailActivity.class)
                    .putExtra("code", data.getStringExtra("code")).putExtra("id", list.get(faultAdapter.getScanPostion()).getId())
                    .putExtra("messageType", MessageType.repair);
            startActivity(intent);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityForResult.FaultListRefresh) {
            ActivityForResult.FaultListRefresh = false;
            getList(UpdateType.top);
        }
    }
}
