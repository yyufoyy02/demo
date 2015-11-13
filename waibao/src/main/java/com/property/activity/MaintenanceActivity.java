package com.property.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.property.ActivityForResult;
import com.property.adapter.MessageAdapter;
import com.property.api.MaintenanceApi;
import com.property.base.BaseActivity;
import com.property.enumbase.MessageType;
import com.property.enumbase.UpdateType;
import com.property.http.MyJsonDataResponseCacheHandler;
import com.property.model.ImageModel;
import com.property.model.MaintenanceModel;
import com.property.model.MessageModel;
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
public class MaintenanceActivity extends BaseActivity implements IXListViewListener {

    List<MessageModel> list = new ArrayList<>();
    @InjectView(R.id.list)
    PullToRefreshRecyclerView listView;
    MessageAdapter mMessageAdapter;
    List<MaintenanceModel> maintenanceModellist = new ArrayList<>();
    String id;

    @Override
    public int onCreateViewLayouId() {
        return R.layout.message_activity;
    }

    @Override
    public void initAllData() {
        setTitle("维保");
        mMessageAdapter = new MessageAdapter(mContext, list);
        listView.setLayoutManager(new LinearLayoutManager(mContext));
        listView.setPullRefreshLoadEnable(true, true, PullToRefreshBase.Mode.BOTH);
        listView.setOnXListViewListener(this);
        listView.setAdapter(mMessageAdapter);
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

    List<MessageModel> initMaintenance2Message(List<MaintenanceModel> list) {
        List<MessageModel> messageModelList = new ArrayList<>();
        for (MaintenanceModel maintenanceModel : list) {
            MessageModel messageModel = new MessageModel();
            messageModel.setAddress(maintenanceModel.getAddress());
            messageModel.setIcon(new ImageModel("drawable://" + R.drawable.icon_book));
            messageModel.setName("市政府电梯" + maintenanceModel.getElevetor_number() + "号维保");
            messageModel.setTime(1444492800);
            messageModel.setStatus(maintenanceModel.getM_status());
            messageModel.setMessage_type(1);
            messageModelList.add(messageModel);
        }
        return messageModelList;
    }

    void getList(final UpdateType updateType) {
        String id = null;
        if (updateType == UpdateType.top || maintenanceModellist.isEmpty()) {
            showProgressDialog(mContext);
            id = null;
        } else {
            id = maintenanceModellist.get(maintenanceModellist.size() - 1).getId();
        }
        MaintenanceApi.getInstance().getList(mContext, id, new MyJsonDataResponseCacheHandler<List<MaintenanceModel>>(MaintenanceModel.class, maintenanceModellist.isEmpty()) {
            @Override
            public void onJsonDataSuccess(List<MaintenanceModel> object) {
                if (updateType == UpdateType.top)
                    maintenanceModellist.clear();
                maintenanceModellist.addAll(object);
                list.clear();
                list.addAll(initMaintenance2Message(maintenanceModellist));
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
        if (requestCode == MessageActivity.REQUEST_CODE_SCANLE && resultCode == RESULT_OK && data != null && mMessageAdapter.getScanPostion() != -1) {
            XSimpleLogger.Log().e("code:" + data.getStringExtra("code"));
            startActivity(new Intent(mContext, DetailActivity.class)
                    .putExtra("code", data.getStringExtra("code")).putExtra("id", maintenanceModellist.get(mMessageAdapter.getScanPostion()).getId())
                    .putExtra("messageType", MessageType.maintenance));

        }

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
