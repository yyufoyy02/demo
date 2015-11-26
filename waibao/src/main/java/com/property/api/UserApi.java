package com.property.api;

import android.content.Context;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.property.base.BaseApi;
import com.property.utils.UserDataUtil;

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
        post(context, "login.json", mTempMap, mMyJsonHttpResponseCacheHandler);
    }

    public void password(Context context, String old_password,
                         String new_password, String new_password2, JsonHttpResponseHandler mMyJsonHttpResponseCacheHandler) {
        mTempMap.clear();
        mTempMap.put("staff_id", UserDataUtil.getInstance().getStaff_id());
        mTempMap.put("password", old_password);
        mTempMap.put("new_password", new_password);
        mTempMap.put("new_password2", new_password2);
        post(context, "password.json", mTempMap, mMyJsonHttpResponseCacheHandler);
    }

    public void getMessages(Context context, JsonHttpResponseHandler mMyJsonHttpResponseCacheHandler) {
        mTempMap.clear();
        mTempMap.put("staff_id", UserDataUtil.getInstance().getStaff_id());
        get(context, "messages.json", mTempMap, mMyJsonHttpResponseCacheHandler);
    }
}
