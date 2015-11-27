package com.property.api;


import android.content.Context;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.property.base.BaseApi;
import com.property.utils.UserDataUtil;

public class StatisticsApi extends BaseApi {
    private static class Holder {
        private static StatisticsApi instance = new StatisticsApi();
    }

    private StatisticsApi() {
    }

    public static StatisticsApi getInstance() {
        return Holder.instance;
    }

    public void getStatistics(Context context, double begin_time, double end_time, JsonHttpResponseHandler jsonHttpResponseHandler) {
        mTempMap.clear();
        mTempMap.put("staff_id", UserDataUtil.getInstance().getStaff_id());
        if (begin_time != 0 && end_time != 0) {
            mTempMap.put("begin_time", begin_time + "");
            mTempMap.put("end_time", end_time + "");
        }
        get(context, "statistics.json", mTempMap, jsonHttpResponseHandler);
    }
}
