package com.property.api;

import android.content.Context;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.property.base.BaseApi;


public class LanguageApi extends BaseApi {
    private static class Holder {
        private static LanguageApi instance = new LanguageApi();
    }

    private LanguageApi() {
    }

    public static LanguageApi getInstance() {
        return Holder.instance;
    }

    public void getLanguage(Context context, JsonHttpResponseHandler mMyJsonHttpResponseCacheHandler) {
        get(context, "language.json", mTempMap, mMyJsonHttpResponseCacheHandler);
    }
}
