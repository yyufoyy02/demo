package com.gas.ui.common;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.gas.connector.ConnectorManage;
import com.gas.epiboly.R;
import com.gas.utils.Utils;
import com.google.gson.Gson;

/**
 * Created by Heart on 2015/7/21.
 */
public class BaseFragment extends Fragment implements DialogInterface.OnDismissListener {
    private Activity mActivity;
    private  CustomProgressDialog progressDialog;
    private ProgressDialog mProgressDialog;
    private long currentFlag;

    protected int mScreenWidth;
    protected int mScreenHeight;
    protected Gson gson = new Gson();
    public BaseFragment() {
        super();
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        mActivity = activity;
        DisplayMetrics metric = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        mScreenWidth = metric.widthPixels;
        mScreenHeight = metric.heightPixels;
        if (progressDialog == null)
            progressDialog = CustomProgressDialog.createDialog(activity);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void alertToast(String msg) {
        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
    }

    public String rString(int stringId) {
        String str = "";
        str = this.getResources().getString(stringId);
        return str;
    }

    public void showLoading() {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog.show(mActivity, "",
                    rString(R.string.loading), false, true);
        } else {
            mProgressDialog.setMessage(rString(R.string.loading));
            mProgressDialog.show();
        }
    }

    public void showLoading(int stringId) {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog.show(mActivity, "",
                    rString(stringId), false, true);
        } else {
            mProgressDialog.setMessage(rString(stringId));
            mProgressDialog.show();
        }
    }

    public void showLoading(String message) {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog.show(mActivity, "", message,
                    false, true);
        } else {
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        }
    }

    public void closeLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    /**
     * 显示等待对话框np 绑定请求flag
     */
    public void showProgressDialog(long flag) {
        // 显示进度对话框
        currentFlag = flag;
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.7f; // 0.0-1.0
        getActivity().getWindow().setAttributes(lp);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    /**
     * 关闭进度对话框
     */
    public void dismissProgressDialog() {
        // 关闭进度对话框
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 1.0f; // 0.0-1.0
        getActivity().getWindow().setAttributes(lp);

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        Utils.log("back", "xiaoshi ");
        if(currentFlag != 0){
            ConnectorManage.getInstance().cancleRequest(currentFlag);
            currentFlag = 0;
        }
    }
}
