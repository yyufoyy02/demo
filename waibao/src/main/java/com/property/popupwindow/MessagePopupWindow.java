package com.property.popupwindow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.property.activity.R;

public class MessagePopupWindow extends PopupWindow {

    private View mMenuView;
    Context context;

    public MessagePopupWindow(Context mContexts, View v) {
        super(v, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        context = mContexts;
        mMenuView = ((LayoutInflater) mContexts
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.alert_message_popup, null);

        initPopup();

    }


    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
    }


    private void initPopup() {
        // 设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置点击窗口外边窗口不消失
        this.setOutsideTouchable(true);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
//        this.setAnimationStyle(R.style.AnimFade);
//        ColorDrawable dw = new ColorDrawable(0x7F000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
//        this.setBackgroundDrawable(dw);
    }
}

