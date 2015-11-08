package com.property.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.property.adapter.MessageAdapter;
import com.property.base.BaseActivity;
import com.property.enumbase.MessageType;
import com.property.enumbase.UpdateType;
import com.property.model.MessageModel;
import com.vk.simpleutil.view.PullToRefreshRecyclerView;
import com.vk.simpleutil.view.pulltorefresh.lib.PullToRefreshBase;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

public class MessageActivity extends BaseActivity {
    public static final int REQUEST_CODE_SCANLE = 99;
    MessageType type;
    List<MessageModel> list = new ArrayList<>();
    @InjectView(R.id.list)
    PullToRefreshRecyclerView listView;
    MessageAdapter mMessageAdapter;
    UpdateType updateType;

    @Override
    public int onCreateViewLayouId() {
        return R.layout.message_activity;
    }

    @Override
    public void initAllData() {
//        type = (MessageType) getIntent().getSerializableExtra("type");
//        switch (type) {
//            case all:
//                setTitle("消息");
//                MessageModel messageModel1 = new MessageModel();
//                messageModel1.setAddress("惠州市江北市政府");
//                messageModel1.setIcon(new ImageModel("drawable://" + R.drawable.icon_book));
//                messageModel1.setName("市政府电梯108号维保");
//                messageModel1.setTime(1444492800);
//                messageModel1.setStatus(0);
//                messageModel1.setMessage_type(1);
//                list.add(messageModel1);
//                MessageModel messageModel2 = new MessageModel();
//                messageModel2.setAddress("惠州市江北市政府");
//                messageModel2.setIcon(new ImageModel("drawable://" + R.drawable.icon_setting));
//                messageModel2.setName("市政府电梯108号维保");
//                messageModel2.setTime(1444492800);
//                messageModel2.setStatus(0);
//                messageModel2.setMessage_type(0);
//                list.add(messageModel2);
//                break;
//            case maintenance:
//                setTitle("维保单");
//                MessageModel messageModel3 = new MessageModel();
//                messageModel3.setAddress("惠州市江北市政府");
//                messageModel3.setIcon(new ImageModel("drawable://" + R.drawable.icon_book));
//                messageModel3.setName("市政府电梯108号维保");
//                messageModel3.setTime(1444492800);
//                messageModel3.setStatus(0);
//                messageModel3.setMessage_type(1);
//                list.add(messageModel3);
//                MessageModel messageModel4 = new MessageModel();
//                messageModel4.setAddress("惠州市江北市政府");
//                messageModel4.setIcon(new ImageModel("drawable://" + R.drawable.icon_book));
//                messageModel4.setName("市政府电梯108号维保");
//                messageModel4.setTime(1444492800);
//                messageModel4.setStatus(1);
//                messageModel4.setMessage_type(0);
//                list.add(messageModel4);
//                break;
//            case repair:
//                setTitle("抢修单");
//                MessageModel messageModel5 = new MessageModel();
//                messageModel5.setAddress("惠州市江北市政府");
//                messageModel5.setIcon(new ImageModel("drawable://" + R.drawable.icon_setting));
//                messageModel5.setName("市政府电梯108号维保");
//                messageModel5.setTime(1444492800);
//                messageModel5.setStatus(0);
//                messageModel5.setMessage_type(0);
//                list.add(messageModel5);
//                MessageModel messageModel6 = new MessageModel();
//                messageModel6.setAddress("惠州市江北市政府");
//                messageModel6.setIcon(new ImageModel("drawable://" + R.drawable.icon_setting));
//                messageModel6.setName("市政府电梯108号维保");
//                messageModel6.setTime(1444492800);
//                messageModel6.setStatus(1);
//                messageModel6.setMessage_type(0);
//                list.add(messageModel6);
//                break;
//        }
        initView();
    }

    void initView() {
        listView.setLayoutManager(new LinearLayoutManager(mContext));
        listView.setPullRefreshLoadEnable(false, false, PullToRefreshBase.Mode.BOTH);
//        mMessageAdapter = new MessageAdapter(mContext, list);
//        listView.setAdapter(mMessageAdapter);
//        listView.notifyDataSetChanged();
    }

    @Override
    public void initListener() {
        findViewById(R.id.toolbar).findViewById(R.id.search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
