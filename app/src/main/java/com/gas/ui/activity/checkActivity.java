package com.gas.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gas.conf.Common;
import com.gas.connector.protocol.BusinessHttpProtocol;
import com.gas.entity.CheckBean;
import com.gas.entity.User;
import com.gas.epiboly.R;
import com.gas.ui.common.SuperActivity;
import com.gas.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Heart on 2015/8/22.
 */
public class checkActivity extends SuperActivity implements View.OnClickListener{

    private User user = Common.getInstance().user;

    private long FLAG_CHECK_ALL = -1;
    private long FLAG_CHECK_YEAR = -1;
    private long FLAG_CHECK_MONTH = -1;
    private static View loading_progress_layout;
    private RadioGroup group_status_selector;
    private RadioButton year;
    private RadioButton month;
    private RadioButton all;
    private TextView check_later;
    private TextView check_early;
    private TextView check_working;
    private TextView check_off;
    private TextView check_vacation;

    private Gson gson = new Gson();

    private CheckBean allBean;
    private CheckBean yearBean;
    private CheckBean monthBean;

    public static void launchActivity(Activity fromActivity) {
        Intent i = new Intent(fromActivity, checkActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        fromActivity.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        init();
        initListener();
    }

    public void init() {

        findViewById(R.id.title_home).setVisibility(View.GONE);
        findViewById(R.id.scan_code).setVisibility(View.GONE);
        loading_progress_layout = findViewById(R.id.loading_progress_layout);
        group_status_selector = (RadioGroup) findViewById(R.id.group_status_selector);
        year = (RadioButton) findViewById(R.id.radio_year);
        month = (RadioButton) findViewById(R.id.radio_month);
        all = (RadioButton) findViewById(R.id.radio_all);
        check_later = (TextView) findViewById(R.id.check_later);
        check_early = (TextView) findViewById(R.id.check_early);
        check_working = (TextView) findViewById(R.id.check_working);
        check_off = (TextView) findViewById(R.id.check_off);
        check_vacation = (TextView) findViewById(R.id.check_vacation);

        showLoading();
        FLAG_CHECK_ALL = BusinessHttpProtocol.checkingInfo(this, user.getId() + "");
        FLAG_CHECK_MONTH = BusinessHttpProtocol.checkSearch(this, user.getId() + "", 1);
        FLAG_CHECK_YEAR = BusinessHttpProtocol.checkSearch(this, user.getId() + "", 2);
    }

    public void initListener() {
        findViewById(R.id.title_back).setOnClickListener(this);
        group_status_selector.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.radio_year:
                        loadData(allBean);
                        break;
                    case R.id.radio_month:
                        loadData(monthBean);
                        break;
                    case R.id.radio_all:
                        loadData(yearBean);
                        break;
                }
            }
        });
    }

    @Override
    public void onGeneralError(String e, long flag) {
       hidenLoading();
        Utils.toastMsg(this, e);


    }

    @Override
    public void onGeneralSuccess(String result, long flag) {
        hidenLoading();

        try {
            JSONObject json = new JSONObject(result);

            if (flag == FLAG_CHECK_ALL) {
                allBean = gson.fromJson(json.optString("check"), CheckBean.class);
                loadData(allBean);
            } else if (flag == FLAG_CHECK_MONTH) {
                monthBean = gson.fromJson(json.optString("check_search"), CheckBean.class);
            } else if (flag == FLAG_CHECK_YEAR) {
                yearBean = gson.fromJson(json.optString("check_search"), CheckBean.class);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void loadData(CheckBean checkBean) {
        check_later.setText(checkBean.getLate());
        check_early.setText(checkBean.getEarly());
        check_working.setText(checkBean.getWork1());
        check_off.setText(checkBean.getWork2());
        check_vacation.setText(checkBean.getVacation());
    }

    public static void showLoading() {
        loading_progress_layout.setVisibility(View.VISIBLE);
    }

    public static void hidenLoading() {
        loading_progress_layout.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                finish();
                break;
        }
    }
}
