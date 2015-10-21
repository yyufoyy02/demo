package com.property.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.vk.simpleutil.http.XSimpleHttpUtil;
import com.vk.simpleutil.library.XSimpleAlertDialog;
import com.vk.simpleutil.library.XSimpleAppManager;

import butterknife.ButterKnife;


public abstract class BaseActivity extends AppCompatActivity {
    private Dialog mProgressDialog;
    public Context mContext = this;
    public Activity mActivity = this;

    /**
     * 视图layoutId
     */
    public abstract int onCreateViewLayouId();

    /**
     * 视图layoutId
     */
    public View onCreateViewLayouView() {
        return null;
    }


    /**
     * 初始化数据
     */
    public abstract void initAllData();

    /**
     * 设置监听器
     */
    public abstract void initListener();

    @Override
    protected void onStart() {
        super.onStart();
        mContext = this;
        mActivity = this;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (onCreateViewLayouId() != 0) {
            setContentView(onCreateViewLayouId());
        }
        if (onCreateViewLayouView() != null) {
            setContentView(onCreateViewLayouView());
        }

        XSimpleAppManager.getInstance().addActivity(this);
        ButterKnife.inject(this);
        initAllData();
        initListener();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        XSimpleAppManager.getInstance().removeActivityList(this);
        super.finish();
    }

    public void showProgressDialog(Context mContext, String msg,
                                   XSimpleAlertDialog.Callback callback) {
//        if (mContext != null && !isFinishing()) {
//            mContext.setTheme(R.style.AppDrakTheme);
//            if (mProgressDialog == null)
//                mProgressDialog = MyAlertDialogInit.getInstance()
//                        .MyProgessDialog((Activity) mContext, msg, callback);
//            mProgressDialog.show();
//
//            mProgressDialog.setCanceledOnTouchOutside(false);
//        }

    }

    /**
     * 关闭对话框
     */
    public void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

    }

    /**
     * 对话框是否存在
     */
    public boolean isProgressDialogShow() {
        return mProgressDialog != null && mProgressDialog.isShowing();
    }


    @Override
    protected void onUserLeaveHint() {
        // TODO Auto-generated method stub
        super.onUserLeaveHint();

    }

    @Override
    protected void onPause() {

        mProgressDialog = null;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        XSimpleHttpUtil.getInstance().cancleRequest(this);
        super.onDestroy();
    }

    @Override
    protected void onResume() {

        super.onResume();
    }


}
