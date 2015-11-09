package com.property.api;

import android.content.Context;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.property.base.BaseApi;
import com.property.utils.UserDataUtil;

import java.util.Map;


public class FaultApi extends BaseApi {
    private static class Holder {
        private static FaultApi instance = new FaultApi();
    }

    private FaultApi() {
    }

    public static FaultApi getInstance() {
        return Holder.instance;
    }

//    public void getList(Context context, JsonHttpResponseHandler mMyJsonHttpResponseCacheHandler) {
//        mTempMap.clear();
//        mTempMap.put("staff_id", UserDataUtil.getInstance().getStaff_id());
//        get(context, "fault_list.json", mTempMap, mMyJsonHttpResponseCacheHandler);
//    }

    public void getList(Context context, String id, JsonHttpResponseHandler mMyJsonHttpResponseCacheHandler) {
        mTempMap.clear();
        mTempMap.put("staff_id", UserDataUtil.getInstance().getStaff_id());
        if (id != null) {
            mTempMap.put("type", "1");
            mTempMap.put("id", id);
        }
        get(context, "fault_list.json", mTempMap, mMyJsonHttpResponseCacheHandler);
    }

    public void scan(Context context, String fault_id, String code, JsonHttpResponseHandler mMyJsonHttpResponseCacheHandler) {
        mTempMap.clear();
        mTempMap.put("code", code);
        mTempMap.put("fault_id", fault_id);
        get(context, "fault_scan.json", mTempMap, mMyJsonHttpResponseCacheHandler);
    }

    public void sign(Context context, String fault_id, JsonHttpResponseHandler mMyJsonHttpResponseCacheHandler) {
        mTempMap.clear();
        mTempMap.put("fault_id", fault_id);
        get(context, "fault_sign.json", mTempMap, mMyJsonHttpResponseCacheHandler);
    }

    public void putBeg(Context context, String id, Map<String, String> map, JsonHttpResponseHandler mMyJsonHttpResponseCacheHandler) {
        mTempMap.clear();
        mTempMap.put("fault_id", id);
        if (map != null)
            mTempMap.putAll(map);
        get(context, "fault_beg.json", mTempMap, mMyJsonHttpResponseCacheHandler);
    }

    public void putDeal(Context context, String id, String fault_describe,
                        int fault_parts, String fault_parts_name, String fault_cost, JsonHttpResponseHandler mMyJsonHttpResponseCacheHandler) {
        mTempMap.clear();
        mTempMap.put("fault_id", id);
        mTempMap.put("fault_describe", fault_describe);
        mTempMap.put("fault_parts", fault_parts + "");
        mTempMap.put("fault_parts_name", fault_parts_name);
        mTempMap.put("fault_cost", fault_cost);
        get(context, "fault_deal.json", mTempMap, mMyJsonHttpResponseCacheHandler);
    }

    public void putEng(Context context, String id, Map<String, String> map, JsonHttpResponseHandler mMyJsonHttpResponseCacheHandler) {
        mTempMap.clear();
        mTempMap.put("fault_id", id);
        if (map != null)
            mTempMap.putAll(map);
        get(context, "fault_eng.json", mTempMap, mMyJsonHttpResponseCacheHandler);
    }
}
