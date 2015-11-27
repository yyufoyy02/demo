package com.property.activity;


import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.property.activity.fault.FaultActivity;
import com.property.activity.maintenance.MaintenancePlanActivity;
import com.property.api.UserApi;
import com.property.base.BaseActivity;
import com.property.http.MyJsonDataResponseCacheHandler;
import com.property.model.MessageCountModel;
import com.property.popupwindow.MessagePopupWindow;
import com.property.utils.UserDataUtil;
import com.vk.simpleutil.library.XSimpleDensity;

import butterknife.InjectView;
import butterknife.annotation.event.OnClick;

public class IndexActivity extends BaseActivity {

    @InjectView(R.id.tv_index_username)
    TextView tvIndexUsername;
    @InjectView(R.id.tv_index_desc)
    TextView tvIndexDesc;
    @InjectView(R.id.iv_index_message)
    ImageView ivMessage;
    @InjectView(R.id.tv_index_count)
    TextView tvCount;
    MessagePopupWindow messagePopupWindow;

    @Override
    public int onCreateViewLayouId() {
        return R.layout.index_activity;
    }

    @Override
    public void initAllData() {
        tvIndexUsername.setText(UserDataUtil.getInstance().getUserData().getName());
        tvIndexDesc.setText(UserDataUtil.getInstance().getUserData().getDepartment());
        messagePopupWindow = new MessagePopupWindow(mContext);
    }

    @OnClick({R.id.iv_index_settring, R.id.iv_index_message, R.id.iv_index_maintenance, R.id.iv_index_repair, R.id.iv_index_statistics, R.id.iv_index_user})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_index_settring:
                startActivity(new Intent(mContext, SettingActivity.class));
                break;
            case R.id.iv_index_message:
                getMessage();
                if (messagePopupWindow.isShowing()) {
                    messagePopupWindow.dismiss();
                } else {
                    messagePopupWindow.showAsDropDown(ivMessage, -XSimpleDensity.dp2px(35), 0);
                }
                break;
            case R.id.iv_index_maintenance:
                startActivity(new Intent(mContext, MaintenancePlanActivity.class));
                break;
            case R.id.iv_index_repair:
                startActivity(new Intent(mContext, FaultActivity.class));
                break;
            case R.id.iv_index_statistics:
                startActivity(new Intent(mContext, StatisticsActivity.class));
                break;
            case R.id.iv_index_user:
                startActivity(new Intent(mContext, UserActivity.class));
                break;
            default:
                break;
        }
    }

    void initMessageView(MessageCountModel messageCountModel) {
        messagePopupWindow.setCount(messageCountModel.getMaintenance_count(), messageCountModel.getFault_count());
        if (messageCountModel.getMaintenance_count() + messageCountModel.getFault_count() != 0) {
            tvCount.setVisibility(View.VISIBLE);
            tvCount.setText(messageCountModel.getMaintenance_count() + messageCountModel.getFault_count() + "");
        } else {
            tvCount.setVisibility(View.GONE);
        }

    }

    void getMessage() {
        UserApi.getInstance().getMessages(mContext, new MyJsonDataResponseCacheHandler<MessageCountModel>(MessageCountModel.class, false) {
            @Override
            public void onJsonDataSuccess(MessageCountModel object) {
                initMessageView(object);
            }

            @Override
            public boolean onJsonCacheData(boolean has) {
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMessage();
    }

    @Override
    public void initListener() {

    }
}
