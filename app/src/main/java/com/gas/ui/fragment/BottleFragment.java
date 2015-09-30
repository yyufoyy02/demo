package com.gas.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gas.adapter.CommonAdapter;
import com.gas.adapter.ViewHolder;
import com.gas.conf.Common;
import com.gas.connector.HttpCallBack;
import com.gas.connector.protocol.BusinessHttpProtocol;
import com.gas.entity.Bottle;
import com.gas.entity.BottleBean;
import com.gas.entity.BottleBean.BottleLog;
import com.gas.entity.User;
import com.gas.epiboly.R;
import com.gas.ui.codeScan.CaptureActivity;
import com.gas.ui.common.BaseFragment;
import com.gas.utils.TimeFormat;
import com.gas.utils.Utils;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Heart on 2015/7/22.
 */
public class BottleFragment extends BaseFragment implements View.OnClickListener, HttpCallBack {

    private User user = Common.getInstance().user;

    private final int REQUEST_CODE_EMPTY = 0X00012;
    private final int REQUEST_CODE_FULL = 0X00013;
    private final int REQUEST_MSG_BOTTLE = 0X00014;
    private final int REQUEST_LOG_BOTTLE = 0X00015;

    private long FLAG_EMPTY_BOTTLE = -1;
    private long FLAG_FULL_BOTTLE = -1;
    private long FLAG_MSG_BOTTLE = -1;
    private long FLAG_LOG_BOTTLE = -1;
    private Activity mActivity;
    private View rootView;

    private Button bottle_empty;
    private Button bottle_full;
    private Button bottle_msg;
    private Button bottle_log;

    private PopupWindow showWindow;
    private Gson gson = new Gson();
    private Bottle bottle;
    private List<BottleLog> logList = new ArrayList<>();
    private CommonAdapter<BottleLog> logAdapter;

    public void init() {
        bottle_empty = (Button) rootView.findViewById(R.id.bottle_empty);
        bottle_full = (Button) rootView.findViewById(R.id.bottle_full);
        bottle_msg = (Button) rootView.findViewById(R.id.bottle_msg);
        bottle_log = (Button) rootView.findViewById(R.id.bottle_log);


    }

    public void initListener() {
        bottle_empty.setOnClickListener(this);
        bottle_full.setOnClickListener(this);
        bottle_msg.setOnClickListener(this);
        bottle_log.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = this.getActivity();
        rootView = getView();
        init();
        initListener();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gas, container,
                false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Utils.log("bottle", requestCode + " " + resultCode);
        if (-1 == resultCode) {
            switch (requestCode) {
                case REQUEST_CODE_FULL:
                    FLAG_FULL_BOTTLE = BusinessHttpProtocol.bottleFullIn(this, data.getStringExtra("code"),user.getId()+"");
                    showProgressDialog(FLAG_FULL_BOTTLE);
                    break;
                case REQUEST_CODE_EMPTY:
                    FLAG_EMPTY_BOTTLE = BusinessHttpProtocol.gasBottleIn(this, Common.getInstance().user.getId(), data.getStringExtra("code"));
                    showProgressDialog(FLAG_EMPTY_BOTTLE);
                    break;
                case REQUEST_MSG_BOTTLE:
                    FLAG_MSG_BOTTLE = BusinessHttpProtocol.searchBottle(this, data.getStringExtra("code"));
                    showProgressDialog(FLAG_MSG_BOTTLE);
                    break;
                case REQUEST_LOG_BOTTLE:
                    FLAG_LOG_BOTTLE = BusinessHttpProtocol.searchBottleLog(this, data.getStringExtra("code"));
                    showProgressDialog(FLAG_LOG_BOTTLE);
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottle_empty:
                showWindow(3);
               //
                break;
            case R.id.bottle_full:
                showWindow(4);
               //
                break;
            case R.id.bottle_msg:
                CaptureActivity.launchActivity(this, REQUEST_MSG_BOTTLE);
                break;
            case R.id.bottle_log:
                CaptureActivity.launchActivity(this, REQUEST_LOG_BOTTLE);
                break;
            case R.id.ly_prompt:
                showWindow.dismiss();
                int position = (int) v.getTag();
                if(position ==3) {
                    CaptureActivity.launchActivity(this, REQUEST_CODE_EMPTY);
                }else {
                    CaptureActivity.launchActivity(this, REQUEST_CODE_FULL);
                }
                break;
        }
    }

    @Override
    public void onGeneralSuccess(String result, long flag) {


        Utils.log("code", result);
        try {
            JSONObject json = new JSONObject(result);
            if (flag == FLAG_EMPTY_BOTTLE) {
                dismissProgressDialog();
                Utils.toastMsg(mActivity, "空气瓶回收 " + Utils.decodeUnicode(json.getString("msg")));
            } else if (flag == FLAG_FULL_BOTTLE) {
                dismissProgressDialog();
                Utils.toastMsg(mActivity, "满气瓶入库 " + Utils.decodeUnicode(json.getString("msg")));
            } else if (flag == FLAG_MSG_BOTTLE) {
                dismissProgressDialog();
                bottle = gson.fromJson(result, Bottle.class);
                showWindow(1);
            } else if (flag == FLAG_LOG_BOTTLE) {
                dismissProgressDialog();
                logList.clear();
                logList = gson.fromJson(result, BottleBean.class).data;
//                JSONArray array = new JSONArray( result );
//
//                for (int i = 0; i < array.length(); i++) {
//                    JSONObject jsonObject = array.getJSONObject(i);
//                    BottleLog bottleLog = new BottleLog();
//                    bottleLog.setId(jsonObject.getString("id"));
//                    bottleLog.setCode(jsonObject.getString("code"));
//                    bottleLog.setAdmin_id(jsonObject.getString("admin_id"));
//                    bottleLog.setInout(Utils.decodeUnicode(jsonObject.getString("inout")));
//                    bottleLog.setAdd_time(jsonObject.getLong("add_time"));
//                    logList.add(bottleLog);
//                }
                for (BottleLog bottleLog : logList) {
                    Utils.log("czd", bottleLog.toString());
                }
                showWindow(2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onGeneralError(String e, long flag) {
        dismissProgressDialog();
        if (flag == FLAG_EMPTY_BOTTLE) {
            Utils.toastMsg(mActivity, "空气瓶回收 " + e);
        } else if (flag == FLAG_FULL_BOTTLE) {
            Utils.toastMsg(mActivity, "满气瓶入库 " + e);
        } else {
            Utils.toastMsg(mActivity, e);
        }
    }


    //1 详细列表  2操作日志
    private void showWindow(int position) {
        WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
        params.alpha = 0.7f;
        mActivity.getWindow().setAttributes(params);

        DisplayMetrics dm = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int h = dm.heightPixels;
        int w = dm.widthPixels;
        View showView = null;
        if (showWindow == null) {
            showWindow = new PopupWindow(mActivity);
        }

        if (position == 1) {
            showView = LayoutInflater.from(mActivity).inflate(
                    R.layout.dialog_bottle_detail, null);
            loadBottleDetail(showView);

            showWindow.setWidth((int) (w * 0.7));
            showWindow.setHeight((int) (h * 0.7));
        } else if (position == 2) {
            showView = LayoutInflater.from(mActivity).inflate(
                    R.layout.dialog_bottle_log, null);
            loadBottleLog(showView);

            showWindow.setWidth((int) (w * 0.9));
            showWindow.setHeight((int) (h * 0.8));
        }else if( position ==3){
            showView = LayoutInflater.from(getActivity()).inflate(
                    R.layout.ly_prompt_dialog, null);
            TextView titleView = (TextView) showView.findViewById(R.id.prompt_title);
            TextView contentView = (TextView) showView.findViewById(R.id.prompt_content);
            String title = "回收空瓶";
            String  content = "确定回收空瓶？";
            titleView.setText(title);
            contentView.setText(content);
            LinearLayout ly_prompt = (LinearLayout) showView.findViewById(R.id.ly_prompt);
            ly_prompt.setTag(position);
            ly_prompt.setOnClickListener(this);
            showWindow.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
            showWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        }else if(position ==4){
            showView = LayoutInflater.from(getActivity()).inflate(
                    R.layout.ly_prompt_dialog, null);
            TextView titleView = (TextView) showView.findViewById(R.id.prompt_title);
            TextView contentView = (TextView) showView.findViewById(R.id.prompt_content);
            String title = "满气入库";
            String  content = "确定满气入库？";
            titleView.setText(title);
            contentView.setText(content);
            LinearLayout ly_prompt = (LinearLayout) showView.findViewById(R.id.ly_prompt);
            ly_prompt.setTag(position);
            ly_prompt.setOnClickListener(this);
            showWindow.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
            showWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
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
                WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
                params.alpha = 1.0f;
                mActivity.getWindow().setAttributes(params);
            }
        });
    }

    public void loadBottleLog(View parentView) {
        ListView listView = (ListView) parentView.findViewById(R.id.log_list);
        logAdapter = new CommonAdapter<BottleLog>(getActivity(), logList, R.layout.item_bottle_log) {
            @Override
            public void convert(ViewHolder helper, BottleLog item) {
                helper.setText(R.id.date, TimeFormat.convertTimeLong2String(item.add_time * 1000, Calendar.DATE));
                helper.setText(R.id.user, item.getId());
                helper.setText(R.id.msg, item.getInout());
            }
        };
        listView.setAdapter(logAdapter);

        parentView.findViewById(R.id.ly_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWindow.dismiss();
            }
        });
    }


    public void loadBottleDetail(View parentView) {
        Utils.log("bottle_msg", bottle.toString());
        TextView bottle_no = (TextView) parentView.findViewById(R.id.bottle_no);
        TextView bottle_date = (TextView) parentView.findViewById(R.id.bottle_date);
        TextView detection_one_time = (TextView) parentView.findViewById(R.id.detection_one_time);
        TextView bottle_gasstate = (TextView) parentView.findViewById(R.id.bottle_gasstate);
        TextView bottle_spec = (TextView) parentView.findViewById(R.id.bottle_spec);
        TextView detection = (TextView) parentView.findViewById(R.id.detection);
        TextView detection_two_time = (TextView) parentView.findViewById(R.id.detection_two_time);
        bottle_no.setText(bottle.getCode());
        bottle_date.setText(TimeFormat.convertTimeLong2String(bottle.getDate() * 1000, Calendar.SECOND));
        detection_one_time.setText(bottle.getDetection_one_time() == 0 ? "暂无记录" : TimeFormat.convertTimeLong2String(bottle.getDetection_one_time()*1000, Calendar.SECOND));
        detection_two_time.setText(bottle.getDetection_one_time() == 0 ? "暂无记录" : TimeFormat.convertTimeLong2String(bottle.getDetection_one_time()*1000, Calendar.SECOND));
        bottle_gasstate.setText(getGasstate(bottle.getGasstate()));
        bottle_spec.setText(bottle.getSpec());
        detection.setText(bottle.getDetection());

    }


    public String getGasstate(String position) {
        switch (position) {
            case "1":
                return "使用";
            case "2":
                return "空瓶";
            case "3":
                return "满气";
            case "4":
                return "损坏";
            case "5":
                return "维修";
            case "7":
                return "使用";
        }
        return "";
    }
}
