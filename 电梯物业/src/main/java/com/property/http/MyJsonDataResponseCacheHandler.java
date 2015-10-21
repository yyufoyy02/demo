package com.property.http;

import com.google.gson.JsonElement;
import com.meijialove.ErrorCode;
import com.meijialove.activity.base.BaseApi;
import com.meijialove.modelresult.JsonRestfulHeadPublicDataResult.errorCodeListener;
import com.vk.simpleutil.library.XSimpleLogger;
import com.vk.simpleutil.library.XSimpleToast;

import org.apache.http.Header;
import org.json.JSONObject;

public abstract class MyJsonDataResponseCacheHandler<T> extends
        MyJsonHttpResponseCacheHandler {

    /**
     * 网络请求完成
     */
    public abstract void onHttpComplete();

    /**
     * 数据解析成功
     *
     * @param object 返回模型
     */
    public abstract void onJsonDataSuccess(T object);

    /**
     * 所有的失败
     */
    public void onFail() {

    }

    /**
     * 数据没有找到
     *
     * @param error_code        错误码
     * @param error_description 错误信息
     * @return 是否屏蔽, 错误信息的toast
     */
    public abstract boolean onJsonDataNoFound(int error_code,
                                              String error_description);

    /**
     * 数据错误
     *
     * @param error_code        错误码
     * @param error_description 误信息
     * @return 是否屏蔽, 错误信息的toast
     */
    public boolean onJsonDataError(int error_code, String error_description) {
        return false;
    }

    /**
     * 缓存数据 <br />
     * forexample： if (type==Update.Top){ return true};<br />
     * return false;
     *
     * @param has 缓存模式开启时是否有数据
     * @return 写入但不提取缓存
     */
    public abstract boolean onJsonCacheData(boolean has);

    /**
     * 网络请求错误
     *
     * @param statusCode
     * @param headers
     * @param throwable
     * @param errorResponse
     */
    public void onHttpFailure(int statusCode, Header[] headers,
                              Throwable throwable, String errorResponse) {
    }

    private boolean isList;
    @SuppressWarnings("rawtypes")
    private Class typeReference;
    private boolean cache;

    /**
     * @param typeReference 模型class
     * @param useCache      是否使用缓存,list.isEmpty
     */
    public MyJsonDataResponseCacheHandler(Class<?> typeReference,
                                          boolean useCache) {
        this.typeReference = typeReference;
        this.cache = useCache;
    }

    @Override
    public void onJsonSuccess(int statusCode, Header[] headers,
                              JSONObject response) {
        // TODO Auto-generated method stub
        onHttpComplete();
        if (response == null)
            return;
        XSimpleLogger.Log().e(response.toString());
        BaseApi.JsonRestfulHeadPublicData(response).addOnErrorListener(
                new errorCodeListener() {

                    @SuppressWarnings("unchecked")
                    @Override
                    public void codeSuccess(int error_code, JsonElement data) {
                        if (data.isJsonNull()) {
                            onJsonDataError(ErrorCode.JSON_ERROR, "解析错误");
                            return;
                        }

                        isList = data.isJsonArray()
                                || data.getAsJsonObject().has("list");
                        try {
                            if (!isList) {
                                onJsonDataSuccess((T) BaseApi
                                        .json2JavaBean(data, typeReference));
                            } else {

                                onJsonDataSuccess((T) BaseApi
                                        .json2JavaBeanList(data, typeReference));
                            }
                        } catch (ClassCastException e) {
                            e.printStackTrace();
                            onJsonDataError(ErrorCode.JSON_ERROR, "解析错误");
                        }

                    }

                    @Override
                    public void codeNoData(int error_code,
                                           String error_description) {
                        // TODO Auto-generated method stub
                        if (!onJsonDataNoFound(error_code, error_description))
                            XSimpleToast.showToast(error_description);
                        onFail();
                    }

                    @Override
                    public void codeError(int error_code,
                                          String error_description) {
                        // TODO Auto-generated method stub
                        if (!onJsonDataError(error_code, error_description))
                            XSimpleToast.showToast(error_description);
                        onFail();
                    }
                });
    }

    @Override
    public void onJsonFailure(int statusCode, Header[] headers,
                              Throwable throwable, String errorResponse) {
        // TODO Auto-generated method stub
        onHttpComplete();
        onHttpFailure(statusCode, headers, throwable, errorResponse);
        onFail();
    }

    @Override
    public boolean onJsonCache() {
        // TODO Auto-generated method stub
        return cache;
    }

    @Override
    public boolean onJsonCacheHasData(boolean has) {
        // TODO Auto-generated method stub
        return onJsonCacheData(has);
    }

}
