package com.property.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.property.adapter.CommonAdapter;
import com.property.adapter.ViewHolder;
import com.property.conf.Common;
import com.property.connector.protocol.BusinessHttpProtocol;
import com.property.entity.CarBean;
import com.property.entity.OilBean;
import com.property.entity.User;
import com.property.epiboly.R;
import com.property.ui.common.SuperActivity;
import com.property.utils.TimeFormat;
import com.property.utils.Utils;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Heart on 2015/8/23.
 * 油耗记录
 */
public class OilActivity extends SuperActivity implements View.OnClickListener {
    private User user = Common.getInstance().user;
    private long FLAG_OIL_LOG = -1;
    private long FLAG_CAR_WRAP = -1 ;
    private Button log_add;
    private CarBean carBean;
    private ListView logList;
    private PopupWindow showWindow;

    private View rootView;

    private TextView car_no;
    private EditText capactiy;
    private EditText spend;
    private EditText month;
    private EditText year;
    private EditText day;

    private Button submit;
    private Button cancle;
    private List<OilBean.Oil> oil_log;
    private Gson gson = new Gson();
    private View loading_progress_layout;
    private CommonAdapter<OilBean.Oil> oilCommonAdapter;
    public static void launchActivity(Activity fromActivity) {
        Intent i = new Intent(fromActivity, OilActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        fromActivity.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_car_oil, null);
        setContentView(rootView);
        init();
    }

    public void init() {
        loading_progress_layout = findViewById(R.id.loading_progress_layout);
        showLoading();

        findViewById(R.id.add_record).setOnClickListener(this);
        findViewById(R.id.title_back).setOnClickListener(this);
        findViewById(R.id.title_home).setVisibility(View.GONE);
        findViewById(R.id.scan_code).setVisibility(View.GONE);

        oil_log = new ArrayList<>();
        FLAG_OIL_LOG=   BusinessHttpProtocol.oilLog(this, user.getId());
        logList = (ListView) findViewById(R.id.log_list);


        oilCommonAdapter = new CommonAdapter<OilBean.Oil>(this,oil_log,R.layout.item_oil_log) {
            @Override
            public void convert(ViewHolder helper, OilBean.Oil item) {
                helper.setText(R.id.car_no,item.getCar_no());
                helper.setText(R.id.capacity,item.getOil()+"L");
                helper.setText(R.id.spend,item.getOil_cost()+"元");
                helper.setText(R.id.time, TimeFormat.convertTimeLong2String(item.getAdd_date()*1000, Calendar.DATE));
            }
        };
        logList.setAdapter(oilCommonAdapter);
    }

    @Override
    public void onGeneralError(String e, long flag) {
        hidenLoading();
    }

    @Override
    public void onGeneralSuccess(String result, long flag) {
        hidenLoading();
        if(flag == FLAG_OIL_LOG){

            oil_log = gson.fromJson(result,OilBean.class).oil_log;
            oilCommonAdapter.replaceItems(oil_log);

            for (OilBean.Oil oil : oil_log) {
                Utils.log("oil",oil.toString());
            }
        }else if(FLAG_CAR_WRAP == flag){
            try {
                JSONObject json = new JSONObject(result);
                if (json.optString("sign").equals("one")) {
                    carBean = gson.fromJson(json.optString("car_one"), CarBean.class);
                    AddoilActivity.launchActivity(this, carBean.getCar_no());
                }else {
                    Utils.toastMsg(this,"请先绑定车辆");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_record:
                showLoading();
                FLAG_CAR_WRAP = BusinessHttpProtocol.getCar(this, user.getId() + "", user.getDepot_id() + "");
                //   AddoilActivity.launchActivity(this);
                // showWindow();
                break;
            case R.id.title_back:
                finish();
                break;
        }
    }


    private void showWindow() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.7f;
        getWindow().setAttributes(params);
        View showView = LayoutInflater.from(this).inflate(
                R.layout.dialog_car_oil_add, null);

        if (showWindow == null) {
            showWindow = new PopupWindow(this);
        }

        loadDate(showView);
        showWindow.setContentView(showView);
        showWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        showWindow.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);


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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            showLoading();
            FLAG_OIL_LOG=   BusinessHttpProtocol.oilLog(this, user.getId());
        }
    }

    public void loadDate(View parent) {
        car_no = (TextView) parent.findViewById(R.id.car_no);
        capactiy = (EditText) parent.findViewById(R.id.capactiy);
        spend = (EditText) parent.findViewById(R.id.spend);
        month = (EditText) parent.findViewById(R.id.month);
        year = (EditText) parent.findViewById(R.id.year);
        day = (EditText) parent.findViewById(R.id.day);
        submit = (Button) parent.findViewById(R.id.submit);
        cancle = (Button) parent.findViewById(R.id.cancle);
        submit.setOnClickListener(this);
        cancle.setOnClickListener(this);
        car_no.setOnClickListener(this);
    }

    public void showLoading() {
        loading_progress_layout.setVisibility(View.VISIBLE);
    }

    public void hidenLoading() {
        loading_progress_layout.setVisibility(View.GONE);
    }


    @Override
    public void onBackPressed() {
        if(loading_progress_layout.isShown()){
            hidenLoading();
            return;
        }
        super.onBackPressed();

    }
}
