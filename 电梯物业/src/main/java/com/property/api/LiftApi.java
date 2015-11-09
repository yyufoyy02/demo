package com.property.api;

import android.content.Context;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.property.base.BaseApi;

import java.util.Map;


public class LiftApi extends BaseApi {
    private static class Holder {
        private static LiftApi instance = new LiftApi();
    }

    private LiftApi() {
    }

    public static LiftApi getInstance() {
        return Holder.instance;
    }

    public void putlift(Context context, String id, Map<String, String> map, JsonHttpResponseHandler mMyJsonHttpResponseCacheHandler) {
        mTempMap.clear();
        mTempMap.put("lift_id", id);
        if (map != null)
            mTempMap.putAll(map);
        get(context, "fault_list.json", mTempMap, mMyJsonHttpResponseCacheHandler);
    }
}
