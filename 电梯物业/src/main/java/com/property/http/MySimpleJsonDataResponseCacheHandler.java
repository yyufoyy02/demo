package com.property.http;

import com.google.gson.JsonElement;
import com.meijialove.activity.base.BaseApi;
import com.meijialove.modelresult.JsonRestfulHeadPublicDataResult;
import com.vk.simpleutil.library.XSimpleLogger;

import org.apache.http.Header;
import org.json.JSONObject;


public class MySimpleJsonDataResponseCacheHandler extends MyJsonHttpResponseCacheHandler {


    public interface OnJsonCallBack {
        void success();

        void fail();
    }

    OnJsonCallBack mOnJsonCallBack;

    public MySimpleJsonDataResponseCacheHandler(OnJsonCallBack mOnJsonCallBack) {
        this.mOnJsonCallBack = mOnJsonCallBack;
    }

    @Override
    public void onJsonSuccess(int statusCode, Header[] headers, JSONObject response) {
        XSimpleLogger.Log().e(response.toString());
        BaseApi.JsonRestfulHeadPublicData(response).addOnErrorListener(new JsonRestfulHeadPublicDataResult.errorCodeListener() {
            @Override
            public void codeSuccess(int error_code, JsonElement data) {
                if (mOnJsonCallBack != null)
                    mOnJsonCallBack.success();
            }

            @Override
            public void codeNoData(int error_code, String error_description) {
                if (mOnJsonCallBack != null)
                    mOnJsonCallBack.fail();
            }

            @Override
            public void codeError(int error_code, String error_description) {
                if (mOnJsonCallBack != null)
                    mOnJsonCallBack.fail();
            }
        });
    }

    @Override
    public void onJsonFailure(int statusCode, Header[] headers, Throwable throwable, String errorResponse) {
        if (mOnJsonCallBack != null)
            mOnJsonCallBack.fail();
    }

    @Override
    public boolean onJsonCache() {
        return false;
    }

    @Override
    public boolean onJsonCacheHasData(boolean has) {
        return false;
    }
}