package com.property.http;

import com.google.gson.JsonElement;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.property.BaseApplication;
import com.vk.simpleutil.json.GsonUtils;
import com.vk.simpleutil.library.XSimpleACache;
import com.vk.simpleutil.library.XSimpleLogger;
import com.vk.simpleutil.library.XSimpleText;
import com.vk.simpleutil.library.XSimpleToast;

import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.json.JSONObject;

import java.util.Locale;


public abstract class MyJsonHttpResponseCacheHandler extends
        JsonHttpResponseHandler {

    public abstract void onJsonSuccess(int statusCode, Header[] headers,
                                       JSONObject response);

    public abstract void onJsonFailure(int statusCode, Header[] headers,
                                       Throwable throwable, String errorResponse);

    /**
     * 缓存模式<br />
     * forexample： if (list.isEmpty()){ return true};<br />
     * return false;
     *
     * @return 写入且提取缓存
     */

    public abstract boolean onJsonCache();

    /**
     * 缓存数据 <br />
     * forexample： if (type==Update.Top){ return true};<br />
     * return false;
     *
     * @param has 缓存模式开启时是否有数据
     * @return 写入但不提取缓存
     */
    public abstract boolean onJsonCacheHasData(boolean has);

    private boolean isCache = false;
    private boolean onJsonCache = false;
    private int cacheTime = onJsonCacheTime();

    /**
     * 缓存时间,小于0没时间限制
     */
    public int onJsonCacheTime() {
        return 0;
    }

    /**
     * 网络连接不可用toast弹出
     */
    public boolean onErrorToast() {
        return true;
    }

    @Override
    public void onStart() {
        onJsonCache = onJsonCache();
        if (onJsonCache) {
            if (!XSimpleText.isEmpty(getRequestURI().toString())) {
                JSONObject mJSONObject = XSimpleACache.get(
                        BaseApplication.mContext).getAsJSONObject(
                        getRequestURI().toString());
                if (mJSONObject != null) {
                    onJsonSuccess(HttpStatus.SC_OK, null, mJSONObject);
                    isCache = onJsonCacheHasData(true);
                } else {
                    isCache = onJsonCacheHasData(false);
                }
            }
        } else {
            isCache = onJsonCacheHasData(true);
        }
        super.onStart();
    }

    private boolean success(JSONObject response) {
        JsonElement rootNode = GsonUtils.getJsonElement(response.toString());
        JsonElement error_code = rootNode.isJsonObject() ? rootNode
                .getAsJsonObject().get("flag") : null;
        if (error_code != null && error_code.getAsString().equals("success")) {
            XSimpleLogger.Log().e("error_code.asInt()" + error_code.getAsString());
            return true;
        }
        return false;
    }
//
//    private void successStatus(JSONObject response) {
//        JsonElement rootNode = GsonUtils.getJsonElement(response.toString());
//        JsonElement error_code = rootNode.isJsonObject() ? rootNode
//                .getAsJsonObject().get("error_code") : null;
//        if (error_code != null) {
//            XSimpleLogger.Log().e("error_code.asInt()" + error_code.getAsInt());
//            ErrorCode.doErrorCode(null, error_code.getAsInt());
//        }
//        rootNode = null;
//    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        // TODO Auto-generated method stub
        onJsonSuccess(statusCode, headers, response);
//        successStatus(response);
        if (onJsonCache || isCache) {
            if (success(response)) {
                if (cacheTime <= 0) {
                    XSimpleACache.get(BaseApplication.mContext).put(
                            getRequestURI().toString(), response);
                } else {
                    XSimpleACache.get(BaseApplication.mContext).put(
                            getRequestURI().toString(), response, cacheTime);
                }
            }
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String respone,
                          Throwable throwable) {
        // super.onFailure(statusCode, headers, respone, throwable);
        if (statusCode != 200) {
            XSimpleToast.showToast("网络连接错误...statusCode=" + statusCode);
            XSimpleLogger.Log().showToast(
                    "(statusCode:" + statusCode + ")" + "respone:" + respone);
        }
        onJsonFailure(statusCode, headers, throwable, respone);

    }

    @Override
    public void onFailure(int statusCode, Header[] headers,
                          Throwable throwable, JSONObject errorResponse) {
        // super.onFailure(statusCode, headers, throwable, errorResponse);
        XSimpleLogger.Log().e("fail===========");
        // TODO Auto-generated method stub
        StringBuilder builder = new StringBuilder();
        // super.setRequestHeaders(requestHeaders)
        if (headers != null) {

            for (Header h : headers) {
                String _h = String.format(Locale.US, "%s : %s", h.getName(),
                        h.getValue());
                builder.append(_h);
                builder.append("\n");
            }
        }

        if (errorResponse != null) {
            if (onErrorToast()) {
                XSimpleToast.showToast("网络连接不可用...statusCode=" + statusCode);
            }
            onJsonFailure(statusCode, headers, throwable,
                    errorResponse.toString() + "");
        } else {
            if (onErrorToast()) {
                XSimpleToast.showToast("网络连接不可用...statusCode=" + statusCode);
            }
            onJsonFailure(statusCode, headers, throwable, "NULL");
        }
        XSimpleLogger.Log().showToast(
                builder.toString() + "(statusCode:" + statusCode + ")"
                        + "throwable:" + throwable.getMessage());
    }

}
