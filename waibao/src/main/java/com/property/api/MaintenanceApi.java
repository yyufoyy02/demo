package com.property.api;

import android.content.Context;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.property.base.BaseApi;
import com.property.utils.UserDataUtil;

import java.util.Map;


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

    public void getList(Context context, String id, String plan_id, JsonHttpResponseHandler mMyJsonHttpResponseCacheHandler) {
        mTempMap.clear();
        mTempMap.put("staff_id", UserDataUtil.getInstance().getStaff_id());
        mTempMap.put("plan_id", plan_id);
        if (id != null) {
            mTempMap.put("type", "1");
            mTempMap.put("id", id);
        }
        get(context, "maintenance_list.json", mTempMap, mMyJsonHttpResponseCacheHandler);
    }

    public void getPlanList(Context context, String id, JsonHttpResponseHandler mMyJsonHttpResponseCacheHandler) {
        mTempMap.clear();
        mTempMap.put("staff_id", UserDataUtil.getInstance().getStaff_id());
        if (id != null) {
            mTempMap.put("type", "1");
            mTempMap.put("id", id);
        }
        get(context, "plan_list.json", mTempMap, mMyJsonHttpResponseCacheHandler);
    }

    public void scan(Context context, String plan_id, String code, JsonHttpResponseHandler mMyJsonHttpResponseCacheHandler) {
        mTempMap.clear();
        mTempMap.put("code", code);
        mTempMap.put("plan_id", plan_id);
        get(context, "maintenance_scan.json", mTempMap, mMyJsonHttpResponseCacheHandler);
    }

    public void sign(Context context, String lift_id, String plan_id, JsonHttpResponseHandler mMyJsonHttpResponseCacheHandler) {
        mTempMap.clear();
        mTempMap.put("staff_id", UserDataUtil.getInstance().getStaff_id());
        mTempMap.put("lift_id", lift_id);
        mTempMap.put("plan_id", plan_id);
        post(context, "maintenance_sign.json", mTempMap, mMyJsonHttpResponseCacheHandler);
    }

    public void getRuleList(Context context, String rule_id, JsonHttpResponseHandler mMyJsonHttpResponseCacheHandler) {
        mTempMap.clear();
        mTempMap.put("new_rule_id", rule_id);
        get(context, "rule.json", mTempMap, mMyJsonHttpResponseCacheHandler);
    }

    public void putMaintenance(Context context, String maintenance_id, Map<String, String> map,
                               JsonHttpResponseHandler mMyJsonHttpResponseCacheHandler) {
        mTempMap.clear();
        mTempMap.put("maintenance_id", maintenance_id);
        if (map != null)
            mTempMap.putAll(map);
        put(context, "maintenance_on.json", mTempMap, mMyJsonHttpResponseCacheHandler);
    }
    public void getDeal(Context context, String id, JsonHttpResponseHandler mMyJsonHttpResponseCacheHandler) {
        mTempMap.clear();
        mTempMap.put("maintenance_id", id);
        get(context, "maintenance_history.json", mTempMap, mMyJsonHttpResponseCacheHandler);
    }
}
