package com.property.api;

import android.content.Context;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.property.base.BaseApi;
import com.property.utils.UserDataUtil;


public class MaintenanceApi extends BaseApi {
    private static class Holder {
        private static MaintenanceApi instance = new MaintenanceApi();
    }

    private MaintenanceApi() {
    }

    public static MaintenanceApi getInstance() {
        return Holder.instance;
    }

    public void getList(Context context, JsonHttpResponseHandler mMyJsonHttpResponseCacheHandler) {
        mTempMap.clear();
        mTempMap.put("staff_id", UserDataUtil.getInstance().getStaff_id());
        get(context, "maintenance_list.json", mTempMap, mMyJsonHttpResponseCacheHandler);
    }

    public void getList(Context context, String id, JsonHttpResponseHandler mMyJsonHttpResponseCacheHandler) {
        mTempMap.clear();
        mTempMap.put("staff_id", UserDataUtil.getInstance().getStaff_id());
        mTempMap.put("type", "1");
        mTempMap.put("id", id);
        get(context, "maintenance_list.json", mTempMap, mMyJsonHttpResponseCacheHandler);
    }

    public void scan(Context context, String maintenance_id, String code, JsonHttpResponseHandler mMyJsonHttpResponseCacheHandler) {
        mTempMap.clear();
        mTempMap.put("code", code);
        mTempMap.put("maintenance_id", maintenance_id);
        get(context, "maintenance_scan.json", mTempMap, mMyJsonHttpResponseCacheHandler);
    }

    public void sign(Context context, String maintenance_id, JsonHttpResponseHandler mMyJsonHttpResponseCacheHandler) {
        mTempMap.clear();
        mTempMap.put("maintenance_id", maintenance_id);
        get(context, "maintenance_sign.json", mTempMap, mMyJsonHttpResponseCacheHandler);
    }
}
