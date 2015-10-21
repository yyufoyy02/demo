package com.property.epiboly;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.property.conf.Common;
import com.property.connector.protocol.BusinessHttpProtocol;
import com.property.database.UserWorker;
import com.property.entity.User;
import com.property.ui.activity.carManagerActivity;
import com.property.ui.codeScan.CaptureActivity;
import com.property.ui.common.SuperActivity;
import com.property.utils.CommonUtil;
import com.property.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by Heart on 2015/8/17.
 */
public class HomeActivity extends SuperActivity implements View.OnClickListener {
    private long FLAG_EMPTY_BOTTLE = -1;
    private final int REQUEST_CODE_EMPTY = 0X00012;
    private User user = Common.getInstance().user;
    private ImageButton home_person;
    private ImageButton home_repair;
    private ImageButton home_vehicles;
    private ImageButton home_bottle;
    private ImageButton home_delivery;
    private ImageButton home_attendance;
    private ImageButton home_empty;
    private ImageButton home_logout;
    private PopupWindow showWindow;
    private View rootView;
    private View loading_progress_layout;

    public static void launchActivity(Activity fromActivity) {
        Intent i = new Intent(fromActivity, HomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        fromActivity.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_home, null);
        setContentView(R.layout.activity_home);
        init();
        initListener();
        initJpush();
    }

    public void init() {
        loading_progress_layout = findViewById(R.id.loading_progress_layout);
    }

    public void initListener() {
        findViewById(R.id.home_person).setOnClickListener(this);
        findViewById(R.id.home_repair).setOnClickListener(this);
        findViewById(R.id.home_vehicles).setOnClickListener(this);
        findViewById(R.id.home_bottle).setOnClickListener(this);
        findViewById(R.id.home_delivery).setOnClickListener(this);
        findViewById(R.id.home_attendance).setOnClickListener(this);
        findViewById(R.id.home_empty).setOnClickListener(this);
        findViewById(R.id.home_logout).setOnClickListener(this);

    }

    @Override
    public void onGeneralError(String e, long flag) {
        dismissProgressDialog();
        if (flag == FLAG_EMPTY_BOTTLE) {
            Utils.toastMsg(this, "空气瓶回收 " + e);
        }
    }

    @Override
    public void onGeneralSuccess(String result, long flag) {

        JSONObject json = null;
        try {
            json = new JSONObject(result);
            if (flag == FLAG_EMPTY_BOTTLE) {
                dismissProgressDialog();
                Utils.toastMsg(this, "空气瓶回收 " + Utils.decodeUnicode(json.getString("msg")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        if (showWindow != null)
            showWindow.dismiss();
        super.onDestroy();
    }

    public void logout() {
        showLoading();
        JPushInterface.setAliasAndTags(getApplicationContext(), "", null, mAliasCallback);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hidenLoading();
                new UserWorker(HomeActivity.this).removeAll(user.getId());
                Common.getInstance().user = null;
                StartActity.launchActivity(HomeActivity.this);
                finish();
            }
        }, 1500);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_empty:
                showWindow(2);

                break;
            case R.id.home_person:
                MainActivity.lauchActivity(this, 0);
                break;
            case R.id.home_repair:
                MainActivity.lauchActivity(this, 3);
                break;
            case R.id.home_vehicles:
                carManagerActivity.launchActivity(this);
                break;
            case R.id.home_bottle:
                MainActivity.lauchActivity(this, 4);
                break;
            case R.id.home_delivery:
                MainActivity.lauchActivity(this, 2);
                break;
            case R.id.home_attendance:
                MainActivity.lauchActivity(this, 1);
                break;
            //case R.id.home_empty: MainActivity.lauchActivity(this,2);break;
            case R.id.home_logout:
                showWindow(1);
                break;
            case R.id.ly_prompt:
                showWindow.dismiss();
                int position = (int) v.getTag();
                if (position == 1)
                    logout();
                else if (position == 2)
                    CaptureActivity.launchActivity(this, REQUEST_CODE_EMPTY);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (-1 == resultCode) {
            switch (requestCode) {
                case REQUEST_CODE_EMPTY:
                    FLAG_EMPTY_BOTTLE = BusinessHttpProtocol.gasBottleIn(this, Common.getInstance().user.getId(), data.getStringExtra("code"));
                    showProgressDialog(FLAG_EMPTY_BOTTLE);
                    break;
            }
        }
    }

    private void showWindow(int position) {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.7f;
        getWindow().setAttributes(params);
        View showView = LayoutInflater.from(this).inflate(
                R.layout.ly_prompt_dialog, null);
        TextView titleView = (TextView) showView.findViewById(R.id.prompt_title);
        TextView contentView = (TextView) showView.findViewById(R.id.prompt_content);
        String title = "";
        String content = "";
        if (position == 1) {
            title = "确定退出";
            content = "确定退出登录？";

        } else if (position == 2) {
            title = "回收空瓶";
            content = "确定回收空瓶？";
        }
        titleView.setText(title);
        contentView.setText(content);

        LinearLayout ly_prompt = (LinearLayout) showView.findViewById(R.id.ly_prompt);
        ly_prompt.setOnClickListener(this);
        ly_prompt.setTag(position);
        if (showWindow == null) {
            showWindow = new PopupWindow(this);
        }

        showWindow.setContentView(showView);
        showWindow.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        showWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);

        showWindow.setFocusable(true);
        showWindow.setOutsideTouchable(true);
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        showWindow.setBackgroundDrawable(dw);
        showWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);

        showWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha = 1.0f;
                getWindow().setAttributes(params);
            }
        });
    }

    private static final int MSG_SET_ALIAS = 1001;

    public void initJpush() {
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, Common.getInstance().user.getPhone()));
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
                    break;
            }
        }
    };
    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Utils.log("JPUSH", logs);
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Utils.log("JPUSH", logs);
                    if (CommonUtil.isNetworkAvailable(getApplicationContext())) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    } else {
                        Utils.log("JPUSH", "No network");
                    }
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
                    Utils.log("JPUSH", logs);
            }

            //Utils.toastMsg(MainActivity.this,logs);
        }

    };

    public void showLoading() {
        loading_progress_layout.setVisibility(View.VISIBLE);
    }

    public void hidenLoading() {
        loading_progress_layout.setVisibility(View.GONE);
    }
}
