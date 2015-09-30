package com.gas.ui.activity;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gas.conf.Common;
import com.gas.connector.protocol.BusinessHttpProtocol;
import com.gas.entity.RepairOrder;
import com.gas.entity.User;
import com.gas.epiboly.R;
import com.gas.ui.common.SuperActivity;
import com.gas.utils.TimeFormat;
import com.gas.utils.Utils;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by Heart on 2015/8/17.
 */
public class repairDetail extends SuperActivity implements View.OnClickListener {
    public User user = Common.getInstance().user;
    private long ORDER_DETAIL_FLAG = -1;
    private long VERIFY_BOTTLE_FLAG = -1;
    private long FINISH_ORDER_FLAG = -1;
    private long REJECT_ORDER_FLAG = -1;
    private long ACCEPT_ORDER_FLAG = -1;

    private PopupWindow showWindow;
    private Button accpet_order;
    private Button refuse_order;
    private Button finish_order;

    private TextView custom_name;
    private ImageView order_address_nav_bt;
    private TextView order_no;
    private TextView customer_address;
    private TextView customer_phone;
    private TextView order_time;
    private TextView order_delivery_hours;
    private TextView order_delivery_date;
    private TextView order_status;
    private TextView repair_type;
    private TextView repair_remark;
    private TextView total_cost;
    private LinearLayout ly_repair_cost;
    private EditText repair_cost;
    private RelativeLayout ly_accept_order;
    private LinearLayout ly_unaccept_order;
    private View loading_progress_layout;
    public static int REQUEST_CODE;
    private static RepairOrder itemOrder;
    private Gson gson = new Gson();
    private View rootView;
    private View popupProgress;
    private boolean isPay = false;

    private Handler handler = new Handler();

    public static void launchActivity(Fragment mContext, int requestCode, RepairOrder item) {
        Intent intent = new Intent();
        intent.setClass(mContext.getActivity(), repairDetail.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        REQUEST_CODE = requestCode;
        ((Fragment) mContext).startActivityForResult(intent,
                requestCode);
        itemOrder = item;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_rorder_detail, null);
        setContentView(rootView);
        init();
        initListener();
    }

    public void init() {
        findViewById(R.id.title_home).setVisibility(View.GONE);
        accpet_order = (Button) findViewById(R.id.accpet_order);
        refuse_order = (Button) findViewById(R.id.refuse_order);
        finish_order = (Button) findViewById(R.id.finish_order);
        ly_unaccept_order = (LinearLayout) findViewById(R.id.ly_unaccept_order);
        custom_name = (TextView) findViewById(R.id.custom_name);
        order_no = (TextView) findViewById(R.id.order_no);
        customer_address = (TextView) findViewById(R.id.customer_address);
        customer_phone = (TextView) findViewById(R.id.customer_phone);
        order_delivery_hours = (TextView) findViewById(R.id.order_delivery_hours);
        order_delivery_date = (TextView) findViewById(R.id.order_delivery_date);
        order_address_nav_bt = (ImageView) findViewById(R.id.order_address_nav_bt);
        order_status = (TextView) findViewById(R.id.order_status);
        order_time = (TextView) findViewById(R.id.order_time);
        repair_type = (TextView) findViewById(R.id.repair_type);
        repair_remark = (TextView) findViewById(R.id.repair_remark);
        order_status.setText(getOrderStutus(itemOrder.getStatus() + ""));
        total_cost = (TextView) findViewById(R.id.total_cost);
        ly_repair_cost = (LinearLayout) findViewById(R.id.ly_repair_cost);

        custom_name.setText(itemOrder.getClient_name());
        order_no.setText(itemOrder.getOrder_no());
        customer_phone.setText(itemOrder.getTelphone());
        customer_address.setText(itemOrder.getAddress());
        order_time.setText(TimeFormat.convertTimeLong2String(itemOrder.getCtime() * 1000, Calendar.SECOND));
        order_delivery_date.setText("(" + TimeFormat.convertTimeLong2String((itemOrder.getRepair_date() * 1000), Calendar.DATE) + ")");
        order_delivery_hours.setText(itemOrder.getRepair_time());
        repair_remark.setText(itemOrder.getRemark());
        repair_type.setText(itemOrder.getRepair_type());


        total_cost.setText(itemOrder.getTotal_cost());
        if(itemOrder.getStatus()>=4){
            ly_repair_cost.setVisibility(View.VISIBLE);
        }

        showBottomLy();

    }

    public void initListener() {
        accpet_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWindow(0);
            }
        });
        customer_phone.setOnClickListener(this);
        refuse_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWindow(1);
            }
        });
        finish_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWindow(2);
            }
        });
        order_address_nav_bt.setOnClickListener(this);
        findViewById(R.id.title_back).setOnClickListener(this);
    }

    @Override
    public void onGeneralError(final String e, long flag) {
        if(ACCEPT_ORDER_FLAG == flag ||REJECT_ORDER_FLAG == flag || FINISH_ORDER_FLAG==flag ){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if(popupProgress != null && popupProgress.isShown()) popupProgress.setVisibility(View.GONE);
                    dismissProgressDialog();
                    hiddenPopLoading();
                    Utils.toastMsg(repairDetail.this, e);
                }
            });
        }
    }

    @Override
    public void onGeneralSuccess(String result, long flag) {
        try {
            dismissProgressDialog();
            JSONObject json = new JSONObject(result);
            if(ACCEPT_ORDER_FLAG == flag ||REJECT_ORDER_FLAG == flag ){
                if(popupProgress != null && popupProgress.isShown()) popupProgress.setVisibility(View.GONE);

                Utils.toastMsg(this,Utils.decodeUnicode(json.getString("msg")));
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent();
                        intent.putExtra("itemOrder", itemOrder);
                        setResult(2, intent);
                        finish();
                    }
                }, 1000);
            }else if(FINISH_ORDER_FLAG==flag){
                isPay = true;
                if(showWindow.isShowing() || showWindow != null)
                showWindow.dismiss();
                hiddenPopLoading();
                order_status.setText("维修完成");
                total_cost.setText("￥"+repair_cost.getText().toString());
                ly_repair_cost.setVisibility(View.VISIBLE);
                ly_unaccept_order.setVisibility(View.GONE);
                finish_order.setVisibility(View.GONE);
                Utils.toastMsg(this, Utils.decodeUnicode(json.getString("msg")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getOrderStutus(String status) {
        switch (status) {
            case "1":
            case "2":
                return "未接订单";
            case "3":
                return "已接订单";
            case "4":
            case "5":
                return "已完成";
            case "8":
                return "维修完成";
        }
        return "";
    }


    //接单0  拒绝1  确认订单2
    private void showWindow(int position) {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.7f;
        getWindow().setAttributes(params);
        View showView = LayoutInflater.from(this).inflate(
                R.layout.ly_prompt_dialog, null);
        View finishView = null;
        TextView titleView = (TextView) showView.findViewById(R.id.prompt_title);
        TextView contentView = (TextView) showView.findViewById(R.id.prompt_content);
        String title = "";
        String content = "";
        if (position == 0) {
            title = "接收订单";
            content = "确定接收订单？";
        } else if (position == 1) {
            title = "拒绝订单";
            content = "确定拒绝订单？";
        } else if (position == 2) {
            finishView = LayoutInflater.from(this).inflate(
                    R.layout.dialog_repair_finish, null);
            repair_cost = (EditText)finishView.findViewById(R.id.repair_cost);
            finishView.findViewById(R.id.submit).setOnClickListener(this);
            finishView.findViewById(R.id.cancle).setOnClickListener(this);
            loading_progress_layout = finishView.findViewById(R.id.loading_progress_layout);
        }
        titleView.setText(title);
        contentView.setText(content);

        LinearLayout ly_prompt = (LinearLayout) showView.findViewById(R.id.ly_prompt);
        ly_prompt.setTag(position);
        ly_prompt.setOnClickListener(this);

        if (showWindow == null) {
            showWindow = new PopupWindow(this);
        }

        if (position == 2) {

            showWindow.setContentView(finishView);
            showWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            showWindow.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        } else {
            showWindow.setContentView(showView);
            showWindow.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
            showWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        }


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
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.title_back:
                if(isPay){
                    Intent intent = new Intent();
                    intent.putExtra("itemOrder", itemOrder);
                    setResult(2, intent);
                }
                finish();
                break;
            case R.id.customer_phone:
                Utils.dialAlert(itemOrder.getTelphone(), this);
                break;
            case R.id.order_address_nav_bt:
                Utils.MapPilot(itemOrder.getAddress(),this);
                break;
            case R.id.ly_prompt:

                final int position = (int) v.getTag();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        showWindow.dismiss();
                    }
                });
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (position == 0) {
                            ACCEPT_ORDER_FLAG = BusinessHttpProtocol.getRepairOrder(repairDetail.this, itemOrder.getId(), user.getId());
                        } else if (position == 1) {
                            REJECT_ORDER_FLAG = BusinessHttpProtocol.rejectRepairOrder(repairDetail.this, itemOrder.getId(), user.getId());
                        }
                    }
                }, 100);
                break;
            case R.id.submit:
                showPopLoading();
                String cost = repair_cost.getText().toString();
                FINISH_ORDER_FLAG = BusinessHttpProtocol.finishRepariOrder(repairDetail.this, user.getId(), itemOrder.getId(), cost);
                break;
            case R.id.cancle:
                showWindow.dismiss();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isPay = false;
        if (showWindow != null && showWindow.isShowing()) showWindow.dismiss();
    }

    public void showBottomLy() {
        ly_unaccept_order.setVisibility(View.GONE);
        finish_order.setVisibility(View.GONE);
        switch (itemOrder.getStatus()) {
            case 0:
            case 1:
            case 2:
                ly_unaccept_order.setVisibility(View.VISIBLE);
                break;
            case 3:
                finish_order.setVisibility(View.VISIBLE);

                break;
            case 4:
            case 5:
                break;
        }
    }

    public void showPopLoading(){
        if(loading_progress_layout != null && !loading_progress_layout.isShown())
            loading_progress_layout.setVisibility(View.VISIBLE);
    }

    public void hiddenPopLoading(){
        if(loading_progress_layout != null && loading_progress_layout.isShown())
            loading_progress_layout.setVisibility(View.GONE);
    }
}
