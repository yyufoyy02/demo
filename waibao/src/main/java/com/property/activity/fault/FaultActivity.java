package com.property.activity.fault;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.property.ActivityForResult;
import com.property.activity.DetailActivity;
import com.property.activity.MessageActivity;
import com.property.activity.R;
import com.property.adapter.MessageAdapter;
import com.property.api.FaultApi;
import com.property.base.BaseActivity;
import com.property.enumbase.MessageType;
import com.property.enumbase.UpdateType;
import com.property.http.MyJsonDataResponseCacheHandler;
import com.property.model.FaultModel;
import com.property.model.ImageModel;
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
public class FaultActivity extends BaseActivity implements IXListViewListener {
    List<MessageModel> list = new ArrayList<>();
    @InjectView(R.id.list)
    PullToRefreshRecyclerView listView;
    MessageAdapter mMessageAdapter;
    List<FaultModel> faultModellist = new ArrayList<>();
    String id;

    @Override
    public int onCreateViewLayouId() {
        return R.layout.message_activity;
    }

    @Override
    public void initAllData() {
        setTitle("抢修单");
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

    List<MessageModel> initFault2Message(List<FaultModel> list) {
        List<MessageModel> messageModelList = new ArrayList<>();
        for (FaultModel faultModel : list) {
            MessageModel messageModel = new MessageModel();
            messageModel.setFault_id(faultModel.getId());
            messageModel.setAddress(faultModel.getAddress());
            messageModel.setIcon(new ImageModel("drawable://" + R.drawable.icon_book));
            messageModel.setName("市政府电梯" + faultModel.getElevetor_number() + "号维保");
            messageModel.setTime(1444492800);
            messageModel.setStatus(faultModel.getStatus());
            messageModel.setMessage_type(0);
            messageModelList.add(messageModel);
        }
        return messageModelList;
    }

    void getList(final UpdateType updateType) {

        String id = null;
        if (updateType == UpdateType.top) {
            if (faultModellist.isEmpty())
                showProgressDialog();
            id = null;
        } else {
            id = faultModellist.get(faultModellist.size() - 1).getId();
        }
        FaultApi.getInstance().getList(mContext, id, new MyJsonDataResponseCacheHandler<List<FaultModel>>(FaultModel.class, faultModellist.isEmpty()) {
            @Override
            public void onJsonDataSuccess(List<FaultModel> object) {
                if (updateType == UpdateType.top)
                    faultModellist.clear();
                faultModellist.addAll(object);
                list.clear();
                list.addAll(initFault2Message(faultModellist));
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
            Intent intent = new Intent(mContext, DetailActivity.class)
                    .putExtra("code", data.getStringExtra("code")).putExtra("id", faultModellist.get(mMessageAdapter.getScanPostion()).getId())
                    .putExtra("messageType", MessageType.repair);
            if (mMessageAdapter != null && mMessageAdapter.bdLocation != null)
                intent.putExtra("latitude", mMessageAdapter.bdLocation.getLatitude()).putExtra("longitude", mMessageAdapter.bdLocation.getLongitude());
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
