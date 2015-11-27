package com.property.popupwindow;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.property.activity.R;
import com.property.activity.fault.FaultActivity;
import com.property.activity.maintenance.MaintenancePlanActivity;

public class MessagePopupWindow extends PopupWindow {

    private View mMenuView;
    TextView tvMaintenancecount;
    TextView tvFaultcount;
    TextView tvMaintenancetitle;
    TextView tvFaulttitle;

    public MessagePopupWindow(final Context mContexts) {
        super(mContexts);
        mMenuView = ((LayoutInflater) mContexts
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.alert_message_popup, null);
        tvMaintenancecount = (TextView) mMenuView.findViewById(R.id.tv_index_maintenancecount);
        tvFaultcount = (TextView) mMenuView.findViewById(R.id.tv_index_faultcount);
        tvMaintenancetitle = (TextView) mMenuView.findViewById(R.id.tv_index_maintenancetitle);
        tvFaulttitle = (TextView) mMenuView.findViewById(R.id.tv_index_faulttitle);
        initPopup();
        initListenr(mContexts);

    }

    private void initListenr(final Context mContexts) {
        tvFaulttitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContexts.startActivity(new Intent(mContexts, FaultActivity.class));
                dismiss();
            }
        });
        tvMaintenancetitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContexts.startActivity(new Intent(mContexts, MaintenancePlanActivity.class));
                dismiss();

            }
        });
    }

    public void setCount(int maintenancecount, int faultcount) {
        tvMaintenancecount.setText(maintenancecount + "");
        tvFaultcount.setText(faultcount + "");
    }

    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
    }

    public void showAsDropDown(View parent, int x, int y) {
        super.showAsDropDown(parent, x, y);
    }

    private void initPopup() {
        // 设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置点击窗口外边窗口不消失
        this.setOutsideTouchable(false);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
//        this.setAnimationStyle(R.style.AnimFade);
        ColorDrawable dw = new ColorDrawable(0x00000000);
//         设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }
}

