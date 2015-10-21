package com.property.ui.activity;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.property.adapter.CommonAdapter;
import com.property.adapter.ViewHolder;
import com.property.conf.Common;
import com.property.connector.HttpCallBack;
import com.property.connector.protocol.BusinessHttpProtocol;
import com.property.database.SharedPreferenceUtil;
import com.property.entity.DeliveryOrder;
import com.property.entity.User;
import com.property.epiboly.MainActivity;
import com.property.epiboly.R;
import com.property.ui.codeScan.CaptureActivity;
import com.property.ui.common.SuperActivity;
import com.property.utils.TimeFormat;
import com.property.utils.Utils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Heart on 2015/8/11.
 */
public class orderDetail extends SuperActivity implements HttpCallBack, View.OnClickListener {

    public User user = Common.getInstance().user;
    public static int REQUEST_CODE;
    public static final int REQUEST_LOGIN_ACTIVITY = 0X10012;
    private final int REQUEST_CODE_SCANLE = 0X00012;
    private long ORDER_DETAIL_FLAG = -1;
    private long VERIFY_BOTTLE_FLAG = -1;
    private long FINISH_ORDER_FLAG = -1;
    private long REJECT_ORDER_FLAG = -1;
    private long ACCEPT_ORDER_FLAG = -1;
    private List<String> bottleList = new LinkedList<>();
    private Map<Long, String> bottleMap = new HashMap<>();

    private PopupWindow showWindow;
    private Button accpet_order;
    private Button refuse_order;
    private Button finish_order;


    private ImageView order_address_nav_bt;
    private ImageView order_detail_icon;
    private TextView order_no;
    private TextView customer_address;
    private TextView customer_phone;
    private TextView order_time;
    private TextView order_delivery_hours;
    private TextView order_delivery_date;
    private TextView pay_type;
    private TextView pay_amount;
    private TextView order_status;
    private TextView custom_name;
    private TextView cash_pledge;
    private TextView bottle_num;
    private ListView productListView;
    private LinearLayout ly_accept_order;
    private LinearLayout ly_unaccept_order;
    private Button bottle_type_in;
    private Button bottle_list;
    private EditText bottle_code;
    private View popupLoading;

    private static DeliveryOrder itemOrder;
    private Gson gson = new Gson();
    private Handler handler = new Handler();
    private View rootView;
    private List<String> productList = new ArrayList<>();
    private CommonAdapter<String> productAdapter;

    public static void launchActivity(Fragment mContext, int requestCode, DeliveryOrder item) {
        Intent intent = new Intent();
        intent.setClass(mContext.getActivity(), orderDetail.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        REQUEST_CODE = requestCode;
        ((Fragment) mContext).startActivityForResult(intent,
                requestCode);
        itemOrder = item;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_dorder_detail, null);
        setContentView(rootView);
        init();
        initListener();
    }

    public void init() {

        findViewById(R.id.title_home).setVisibility(View.GONE);
        String bottles[] = SharedPreferenceUtil.getInstance(this).getString(SharedPreferenceUtil.ORDER_DELIVERY_BOTTLE + itemOrder.getId()).split(",");
        for (String temp : bottles) {
            if (!Utils.isEmptyOrNullStr(temp)) bottleList.add(temp);
        }


        ORDER_DETAIL_FLAG = BusinessHttpProtocol.getOrderDetails(this, itemOrder.getId());
        accpet_order = (Button) findViewById(R.id.accpet_order);
        refuse_order = (Button) findViewById(R.id.refuse_order);
        finish_order = (Button) findViewById(R.id.finish_order);

        order_address_nav_bt = (ImageView) findViewById(R.id.order_address_nav_bt);
        order_detail_icon = (ImageView) findViewById(R.id.order_detail_icon);
        order_no = (TextView) findViewById(R.id.order_no);
        customer_address = (TextView) findViewById(R.id.customer_address);
        customer_phone = (TextView) findViewById(R.id.customer_phone);
        order_time = (TextView) findViewById(R.id.order_time);
        order_delivery_hours = (TextView) findViewById(R.id.order_delivery_hours);
        order_delivery_date = (TextView) findViewById(R.id.order_delivery_date);
        pay_type = (TextView) findViewById(R.id.pay_type);
        pay_amount = (TextView) findViewById(R.id.pay_amount);
        order_status = (TextView) findViewById(R.id.order_status);
        custom_name = (TextView) findViewById(R.id.custom_name);
        cash_pledge = (TextView) findViewById(R.id.cash_pledge);
        bottle_num = (TextView) findViewById(R.id.bottle_num);
        ly_accept_order = (LinearLayout) findViewById(R.id.ly_accept_order);
        ly_unaccept_order = (LinearLayout) findViewById(R.id.ly_unaccept_order);
        productListView = (ListView) findViewById(R.id.product_list);
        bottle_type_in = (Button) findViewById(R.id.bottle_type_in);
        bottle_list= (Button) findViewById(R.id.bottle_list);


        custom_name.setText(itemOrder.getClient_name());
        order_no.setText(itemOrder.getOrder_no() + "");
        order_status.setText(getOrderStutus(itemOrder.getStatus()));
        customer_address.setText(itemOrder.getAddress());
        customer_phone.setText(itemOrder.getTelphone());
        pay_type.setText(itemOrder.getPay_type() == 1 ? "到付" : "已支付");
        pay_amount.setText("￥"+itemOrder.getTotal_cost() );
        cash_pledge.setText("￥"+itemOrder.getTotal_yj());
        order_time.setText(TimeFormat.convertTimeLong2String(itemOrder.getAdd_time() * 1000, Calendar.SECOND));
        order_delivery_hours.setText(itemOrder.getSend_time());
        order_delivery_date.setText("(" + TimeFormat.convertTimeLong2String((itemOrder.getSend_date() * 1000), Calendar.DATE) + ")");
        bottle_num.setText(itemOrder.getTotal_count() - bottleList.size() + "");
        showBottomLy();

        productAdapter = new CommonAdapter<String>(this, productList, R.layout.item_product_list) {
            @Override
            public void convert(ViewHolder helper, String item) {
                helper.setText(R.id.product_name, item.split(",")[0]);
                helper.setText(R.id.product_num, item.split(",")[1]);
            }
        };

        productListView.setAdapter(productAdapter);
    }

    public void initListener() {
        bottle_list.setOnClickListener(this);
        bottle_type_in.setOnClickListener(this);
        accpet_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWindow(0);
            }
        });
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
        ly_accept_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CaptureActivity.launchActivity(orderDetail.this, REQUEST_CODE_SCANLE);
            }
        });
        customer_phone.setOnClickListener(this);
        order_address_nav_bt.setOnClickListener(this);
        findViewById(R.id.title_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancle:
                showWindow.dismiss();
                break;
            case R.id.submit:
                String code = bottle_code.getText().toString();
                if(Utils.isEmptyOrNullStr(code)){
                    Utils.toastMsg(this,"瓶号不能留空");
                    return;
                }
                showPopLoading();


                VERIFY_BOTTLE_FLAG = BusinessHttpProtocol.gasBottleOut(this, user.getId(), itemOrder.getId(), code);
                bottleMap.put(VERIFY_BOTTLE_FLAG, code);
                break;
            case R.id.bottle_list:
                showWindow(4);
                break;
            case R.id.bottle_type_in:
                showWindow(3);
                break;
            case R.id.title_back:
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
                showWindow.dismiss();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.showLoading();
                    }
                });

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (position == 0) {
                            ACCEPT_ORDER_FLAG = BusinessHttpProtocol.getDeliverOrder(orderDetail.this, itemOrder.getId(), user.getId());
                        } else if (position == 1) {
                            REJECT_ORDER_FLAG = BusinessHttpProtocol.rejectDeliverOrder(orderDetail.this, itemOrder.getId(), user.getId());
                        } else if (position == 2) {
                            FINISH_ORDER_FLAG = BusinessHttpProtocol.finishOrder(orderDetail.this, user.getId(), itemOrder.getId());
                        }
                    }
                },100);
                break;
        }
    }

    @Override
    public void onGeneralError(final String e, long flag) {
        hidenPopLoading();
        if (bottleMap.containsKey(flag)) {
            bottleMap.remove(flag);
            Utils.toastMsg(this, "煤气瓶号错误");
        }else if(ACCEPT_ORDER_FLAG == flag ||REJECT_ORDER_FLAG == flag || FINISH_ORDER_FLAG==flag ){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    dismissProgressDialog();
                    Utils.toastMsg(orderDetail.this, e);
                }
            });
        }
    }

    @Override
    public void onGeneralSuccess(String result, long flag) {
        try {
            hidenPopLoading();
            JSONObject json = new JSONObject(result);
            if (bottleMap.containsKey(flag)) {


                Utils.toastMsg(this,Utils.decodeUnicode(json.getString("msg")));
                if(showWindow != null && showWindow.isShowing())
                    showWindow.dismiss();
                hidenPopLoading();


                bottleList.add(bottleMap.get(flag));
                bottle_num.setText(itemOrder.getTotal_count() - bottleList.size() + "");
                String tempStr = "";
                for (String temp : bottleList) {
                    tempStr = tempStr + "," + temp;
                }
                SharedPreferenceUtil.getInstance(this).putString(SharedPreferenceUtil.ORDER_DELIVERY_BOTTLE + itemOrder.getId(), tempStr);
                if (itemOrder.getTotal_count() == bottleList.size()) {
                    ly_accept_order.setVisibility(View.GONE);
                    finish_order.setVisibility(View.VISIBLE);
                }
            } else if (ORDER_DETAIL_FLAG == flag) {
                //itemOrder  = gson.fromJson(result,DeliveryOrder.class);
                JSONArray jsonArray = new JSONArray(json.getString("order_list"));

                Utils.log("jsonarray", jsonArray.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsontemp = jsonArray.getJSONObject(i);
                    Utils.log("jsontemp", jsontemp.toString());
                    productList.add(jsontemp.getString("product_type") + "," + jsontemp.getString("count"));
                }
                productAdapter.notifyDataSetChanged();
                Utils.setListViewHeightBasedOnChildren(productListView);
            }else if(ACCEPT_ORDER_FLAG == flag ||REJECT_ORDER_FLAG == flag || FINISH_ORDER_FLAG==flag ){

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.hidenLoading();
                        showWindow.dismiss();
                    }
                });
                Utils.toastMsg(this,Utils.decodeUnicode(json.getString("msg")));
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent();
                        intent.putExtra("itemOrder", itemOrder);
                        setResult(2, intent);
                        finish();
                    }
                }, 400);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_SCANLE && resultCode == RESULT_OK) {
            VERIFY_BOTTLE_FLAG = BusinessHttpProtocol.gasBottleOut(this, user.getId(), itemOrder.getId(), data.getStringExtra("code"));
            bottleMap.put(VERIFY_BOTTLE_FLAG, data.getStringExtra("code"));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void showBottomLy() {
        ly_unaccept_order.setVisibility(View.GONE);
        finish_order.setVisibility(View.GONE);
        ly_accept_order.setVisibility(View.GONE);
        switch (itemOrder.getStatus()) {
            case "0":
            case "1":
            case "2":
                ly_unaccept_order.setVisibility(View.VISIBLE);
                break;
            case "3":
                if (itemOrder.getTotal_count() == bottleList.size()) {
                    finish_order.setVisibility(View.VISIBLE);
                } else {
                    ly_accept_order.setVisibility(View.VISIBLE);
                }
                break;
            case "4":
            case "5":
                break;
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
                return "配送完成";
        }
        return "";
    }

    //接单0  拒绝1  确认订单2  手工输入3  煤气瓶列表4
    private void showWindow(int position) {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.7f;
        getWindow().setAttributes(params);
        View showView = null;
        if (showWindow == null) {
            showWindow = new PopupWindow(this);
        }
        if (position < 3) {
            showView = LayoutInflater.from(this).inflate(
                    R.layout.ly_prompt_dialog, null);
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
                title = "派送完成";
                content = "确定派送完成？";
            }
            titleView.setText(title);
            contentView.setText(content);

            LinearLayout ly_prompt = (LinearLayout) showView.findViewById(R.id.ly_prompt);
            ly_prompt.setTag(position);
            ly_prompt.setOnClickListener(this);
            showWindow.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
            showWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        }else if(position == 3){
            showView = LayoutInflater.from(this).inflate(
                    R.layout.dialog_type_in_bottle, null);
            showView.findViewById(R.id.submit).setOnClickListener(this);
            showView.findViewById(R.id.cancle).setOnClickListener(this);
            bottle_code = (EditText) showView.findViewById(R.id.bottle_code);
            showWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            showWindow.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
            popupLoading = showView.findViewById(R.id.loading_progress_layout);
        }else if(position ==4){
            showView = LayoutInflater.from(this).inflate(
                    R.layout.dialog_list, null);
            ListView listVIew = (ListView) showView.findViewById(R.id.list_view);
            CommonAdapter<String> commonAdapter = new CommonAdapter<String>(this,bottleList,R.layout.item_bottle_list) {
                @Override
                public void convert(ViewHolder helper, String item) {
                     helper.setText(R.id.bottle_code,item);
                     helper.getView(R.id.bottle_del);
                }
            };
            if(bottleList.size()==0){
                showView.findViewById(R.id.tv_empty).setVisibility(View.VISIBLE);
            }
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            int h = dm.heightPixels;
            int w = dm.widthPixels;

            listVIew.setAdapter(commonAdapter);
            Utils.setListViewHeightBasedOnChildren(listVIew);
            showWindow.setWidth((int) (w*0.8));
            showWindow.setHeight( (int) (h*0.8));

        }



        showWindow.setContentView(showView);


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
    protected void onDestroy() {

        if(showWindow != null)
           showWindow.dismiss();
        super.onDestroy();
    }

   public void showPopLoading(){
       if(popupLoading != null && !popupLoading.isShown())
       popupLoading.setVisibility(View.VISIBLE);
   }

    public void hidenPopLoading(){
        if(popupLoading != null && popupLoading.isShown())
         popupLoading.setVisibility(View.GONE);
    }
}
