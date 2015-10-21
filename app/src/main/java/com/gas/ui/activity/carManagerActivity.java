package com.property.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.property.conf.Common;
import com.property.connector.protocol.BusinessHttpProtocol;
import com.property.entity.CarBean;
import com.property.entity.CarList;
import com.property.entity.User;
import com.property.epiboly.R;
import com.property.ui.common.SuperActivity;
import com.property.utils.Utils;
import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * Created by Heart on 2015/8/22.
 */
public class carManagerActivity extends SuperActivity implements View.OnClickListener {

    private User user = Common.getInstance().user;
    private View loading_progress_layout;
    private long FLAG_CAR_WRAP = -1;
    private long FLAG_CAR_OIL_WEAR = -1;

    private Gson gson = new Gson();
    private CarBean carBean;

    public static void launchActivity(Activity fromActivity) {
        Intent i = new Intent(fromActivity, carManagerActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        fromActivity.startActivity(i);
    }

    public void init() {
        loading_progress_layout = findViewById(R.id.loading_progress_layout);
        findViewById(R.id.car_oil_wear).setOnClickListener(this);
        findViewById(R.id.car_wrap).setOnClickListener(this);
        findViewById(R.id.title_back).setOnClickListener(this);
        findViewById(R.id.title_home).setVisibility(View.GONE);
        findViewById(R.id.scan_code).setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);
        init();
    }

    @Override
    public void onGeneralError(String e, long flag) {
        dismissProgressDialog();
    }

    @Override
    public void onGeneralSuccess(String result, long flag) {

        try {
            JSONObject json = new JSONObject(result);

            if (flag == FLAG_CAR_WRAP) {
                hidenLoading();
                if (json.optString("sign").equals("one")) {
                    carBean = gson.fromJson(json.optString("car_one"), CarBean.class);
                    WrapActivity.launchActivity(this, carBean);
                } else {
                    CarList carList = gson.fromJson(result, CarList.class);
                    if (carList.car_no.size() == 0) {
                        Utils.toastMsg(this, "没有可绑定车辆");
                        return;
                    }
                    WrapActivity.launchActivity(this, carList);
                }


            } else if (flag == FLAG_CAR_OIL_WEAR) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.car_oil_wear:
                //   showLoading();
                OilActivity.launchActivity(this);
                break;

            case R.id.car_wrap:

                FLAG_CAR_WRAP = BusinessHttpProtocol.getCar(this, user.getId() + "", user.getDepot_id() + "");
                showLoading();
                break;

            case R.id.title_back:
                //FLAG_CAR_OIL_WEAR;
                finish();
                break;
        }
    }

    public void showLoading() {
        loading_progress_layout.setVisibility(View.VISIBLE);
    }

    public void hidenLoading() {
        loading_progress_layout.setVisibility(View.GONE);
    }
}
