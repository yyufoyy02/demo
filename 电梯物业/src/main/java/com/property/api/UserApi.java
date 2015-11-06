package com.property.api;

import android.content.Context;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.property.base.BaseApi;

public class UserApi extends BaseApi {
    private static class Holder {
        private static UserApi instance = new UserApi();
    }

    private UserApi() {
    }

    public static UserApi getInstance() {
        return Holder.instance;
    }

    public void login(Context context, String username, String password, JsonHttpResponseHandler mMyJsonHttpResponseCacheHandler) {
        mTempMap.clear();
        mTempMap.put("username", username);
        mTempMap.put("password", password);
        post(context, "a=login", mTempMap, mMyJsonHttpResponseCacheHandler);
    }
}
