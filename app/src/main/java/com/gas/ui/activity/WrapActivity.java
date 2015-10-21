package com.property.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.property.adapter.CommonAdapter;
import com.property.adapter.ViewHolder;
import com.property.conf.Common;
import com.property.connector.protocol.BusinessHttpProtocol;
import com.property.entity.CarBean;
import com.property.entity.CarList;
import com.property.entity.CarList.Car;
import com.property.entity.User;
import com.property.epiboly.HomeActivity;
import com.property.epiboly.R;
import com.property.ui.common.SuperActivity;
import com.property.utils.Utils;
import com.property.utils.wrapCarUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Heart on 2015/8/22.
 * 车辆绑定
 */
public class WrapActivity extends SuperActivity implements View.OnClickListener {

    private User user = Common.getInstance().user;
    private PopupWindow showWindow;
    private long FLAG_WRAP_CAR = -1;
    private long FLAG_UNWRAP_CAR = -1;

    private LinearLayout ly_license_number;
    private LinearLayout ly_select;
    private LinearLayout ly_select_number;
    private TextView license_number;
    private TextView tv_license_number;
    private TextView operator;
    private CarBean carBean;
    private Button button_wrap_car;
    private Button button_unwrap_car;
    private List<Car> list;
    private Car currentCar;
    private CommonAdapter<Car> carListAdapter;

    private boolean isUnwrap;
    private Handler handler = new Handler();
    private static View loading_progress_layout;

    public static void launchActivity(Activity fromActivity, CarList carList) {
        Intent i = new Intent(fromActivity, WrapActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putParcelableArrayListExtra("carList", (ArrayList<? extends Parcelable>) carList.car_no);
        fromActivity.startActivity(i);
    }

    public static void launchActivity(Activity fromActivity, CarBean carBean) {
        Intent i = new Intent(fromActivity, WrapActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("car", carBean);
        fromActivity.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_wrap);
        getExtra();
        init();
        initListener();
    }

    public void getExtra() {
        carBean = getIntent().getParcelableExtra("car");

        list = getIntent().getParcelableArrayListExtra("carList");

        if (list != null) {
            for (CarList.Car car : list) {
                Utils.log(car.toString());
            }
        }

    }


    public void init() {


        findViewById(R.id.scan_code).setVisibility(View.GONE);
        loading_progress_layout = findViewById(R.id.loading_progress_layout);
        ly_license_number = (LinearLayout) findViewById(R.id.ly_license_number);
        ly_select = (LinearLayout) findViewById(R.id.ly_select);
        ly_select_number = (LinearLayout) findViewById(R.id.ly_select_number);
        license_number = (TextView) findViewById(R.id.license_number);
        tv_license_number = (TextView) findViewById(R.id.tv_license_number);
        button_wrap_car = (Button) findViewById(R.id.button_wrap_car);
        button_unwrap_car = (Button) findViewById(R.id.button_unwrap_car);
        operator = (TextView) findViewById(R.id.operator);
        operator.setText("操作员：" + user.getName());

        if (list == null) {
            isWrap(true);
            license_number.setText(carBean.getCar_no());
        } else {
            isWrap(false);
            currentCar = list.get(0);
            tv_license_number.setText(list.get(0).car_no);
        }
    }

    public void initListener() {
        findViewById(R.id.title_back).setOnClickListener(this);
        button_wrap_car.setOnClickListener(this);
        button_unwrap_car.setOnClickListener(this);
        ly_select_number.setOnClickListener(this);
        findViewById(R.id.title_home).setOnClickListener(this);
    }


    @Override
    public void onGeneralError(String e, long flag) {
        hidenLoading();
        Utils.toastMsg(this, e);
    }

    @Override
    public void onGeneralSuccess(String result, long flag) {
        try {
            JSONObject json = new JSONObject(result);
            if (flag == FLAG_WRAP_CAR) {
                hidenLoading();
                Utils.toastMsg(this, json.optString("msg"));
                isWrap(true);
                license_number.setText(currentCar.car_no);
            } else if (flag == FLAG_UNWRAP_CAR) {
                hidenLoading();
                isUnwrap = true;
                Utils.toastMsg(this, json.optString("msg"));
                wrapCarUtil.stopLocation(this);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1500);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_unwrap_car:
                if (isUnwrap) return;
                showLoading();
                FLAG_UNWRAP_CAR = BusinessHttpProtocol.unwrapCar(this, user.getId() + "");
                break;
            case R.id.button_wrap_car:
                FLAG_WRAP_CAR = BusinessHttpProtocol.wrapCar(this, user.getId() + "", currentCar.id);
                showLoading();
                break;
            case R.id.ly_select_number:
                showWindow();
                break;
            case R.id.title_back:
                finish();
                break;
            case R.id.title_home:
                HomeActivity.launchActivity(this);
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

    public void isWrap(boolean isWrap) {
        ly_license_number.setVisibility(View.GONE);
        ly_select.setVisibility(View.GONE);
        button_wrap_car.setVisibility(View.GONE);
        button_unwrap_car.setVisibility(View.GONE);

        if (isWrap) {
            ly_license_number.setVisibility(View.VISIBLE);
            button_unwrap_car.setVisibility(View.VISIBLE);
        } else {
            ly_select.setVisibility(View.VISIBLE);
            button_wrap_car.setVisibility(View.VISIBLE);
        }
    }

    private void showWindow() {
        View showView = LayoutInflater.from(this).inflate(
                R.layout.dialog_list, null);
        ListView list_view = (ListView) showView.findViewById(R.id.list_view);


        if (showWindow == null) {
            showWindow = new PopupWindow(this);
        }

        carListAdapter = new CommonAdapter<Car>(this, list, R.layout.item_text) {
            @Override
            public void convert(ViewHolder helper, final Car item) {
                helper.setText(R.id.content, item.car_no);
                // helper.getView(R.id.ly_item).setBackgroundColor(getResColor(R.color.car_item_bg));
                helper.getView(R.id.ly_item).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentCar = item;
                        tv_license_number.setText(currentCar.car_no);
                    }
                });
            }
        };
        int showHeight = 0;
        if (list.size() > 4) {
            View listItem = carListAdapter.getView(0, null, list_view);
            listItem.measure(0, 0);
            showHeight = listItem.getMeasuredHeight() * 4;
        } else {
            View listItem = carListAdapter.getView(0, null, list_view);
            listItem.measure(0, 0);
            showHeight = listItem.getMeasuredHeight() * list.size();
        }

        list_view.setAdapter(carListAdapter);
        showWindow.setContentView(showView);
        showWindow.setWidth(ly_select.getWidth());
        showWindow.setHeight(showHeight);

        showWindow.setFocusable(true);
        showWindow.setOutsideTouchable(true);
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        showWindow.setBackgroundDrawable(dw);
        showWindow.showAsDropDown(ly_select);

        showWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
            }
        });
    }
}
