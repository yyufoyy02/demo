package com.gas.ui.common;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.gas.CloseAllActivity;
import com.gas.connector.ConnectorManage;
import com.gas.connector.HttpCallBack;
import com.gas.utils.Utils;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Heart on 2015/7/20.
 */
public abstract class SuperActivity extends Activity implements  Thread.UncaughtExceptionHandler,HttpCallBack,DialogInterface.OnDismissListener {
    public Context mContext;
    private long currentFlag;
    private static CustomProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        onCreate(savedInstanceState, true);
    }

    protected void onCreate(Bundle savedInstanceState, boolean addToStack) {
        super.onCreate(savedInstanceState);
         // if(!Config.DEBUG)
         //Thread.setDefaultUncaughtExceptionHandler(this);
        mContext = this;
        if (addToStack)
            CloseAllActivity.getInstance().addActivity(this);
        if (progressDialog == null)
            progressDialog = CustomProgressDialog.createDialog(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Utils.log("SuperActivity", "onStart");
        setComponentListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    /**
     * 设置视图组件事件监听器
     *
     * @param context 环境对象
     */

    protected void setComponentListener(SuperActivity context) {

    }
    /**
     * 显示等待对话框np
     */
    public void showProgressDialog() {

        // 显示进度对话框
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f; // 0.0-1.0
        getWindow().setAttributes(lp);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    /**
     * 显示等待对话框np 绑定请求flag
     */
    public void showProgressDialog(long flag) {
        // 显示进度对话框
        currentFlag = flag;
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f; // 0.0-1.0
        getWindow().setAttributes(lp);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    /**
     * 关闭进度对话框
     */
    public void dismissProgressDialog() {
        // 关闭进度对话框
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 1.0f; // 0.0-1.0
        getWindow().setAttributes(lp);

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
    /**
     * 把软键盘隐藏
     */
    public boolean hiddenKeyBoard(Activity mActivity) {
        // 点击屏幕任何地方则把软键盘隐藏
        if (mActivity.getCurrentFocus() != null) {
            ((InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
        }
        return false;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        ex.printStackTrace();
        Utils.dumpLogcat();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        CloseAllActivity.getInstance().close();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    // 保存数据
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    // 恢复数据
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

    protected void setOnDismissListener(DialogInterface.OnDismissListener listener) {
        progressDialog.setOnDismissListener(listener);
    }

    /**
     * 获取string
     *
     * @param id
     * @return
     */
    public String getResString(int id) {
        return getResources().getString(id);
    }

    /**
     * 获取图片资源
     *
     * @param id
     * @return
     */
    public Drawable getResDrawable(int id) {
        return getResources().getDrawable(id);
    }

    /**
     * 获取color值
     *
     * @param id
     * @return
     */
    public int getResColor(int id) {
        return getResources().getColor(id);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if(currentFlag != 0){
            ConnectorManage.getInstance().cancleRequest(currentFlag);
            currentFlag = 0;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
