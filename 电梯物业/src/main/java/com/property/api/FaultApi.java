package com.property.api;

import android.content.Context;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.property.base.BaseApi;
import com.property.utils.UserDataUtil;


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
        if (id == null) {
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
}
