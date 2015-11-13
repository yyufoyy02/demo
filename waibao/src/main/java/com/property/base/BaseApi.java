package com.property.base;

import android.content.Context;
import android.support.v4.util.ArrayMap;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.property.model.JsonRestfulHeadModel;
import com.vk.simpleutil.http.XSimpleHttpUtil;
import com.vk.simpleutil.json.XSimpleJsonBeen;

import org.json.JSONObject;

import java.util.Map;

public class BaseApi {

    enum HttpMethod {
        GET, POST, PUT, DELETE
    }

    public static String BaseUrl = "http://lintianranqi.com/lift/Admin/RestApi/";
    public Map<String, String> mTempMap = new ArrayMap<>();
    private Map<String, String> mParameterMap = new ArrayMap<>();
    private StringBuffer urlStr = new StringBuffer();
    private StringBuffer strBuff = new StringBuffer();


    public void get(Context mContext, String url, Map<String, String> map,
                    JsonHttpResponseHandler mMyJsonHttpResponseCacheHandler) {
        send(mContext, HttpMethod.GET, url, map,
                mMyJsonHttpResponseCacheHandler);
    }

    public void put(Context mContext, String url, Map<String, String> map,
                    JsonHttpResponseHandler mMyJsonHttpResponseCacheHandler) {

        send(mContext, HttpMethod.PUT, url, map,
                mMyJsonHttpResponseCacheHandler);

    }

    public void post(Context mContext, String url, Map<String, String> map,
                     JsonHttpResponseHandler mMyJsonHttpResponseCacheHandler) {
        send(mContext, HttpMethod.POST, url, map,
                mMyJsonHttpResponseCacheHandler);
    }


    public void delete(Context mContext, String url, Map<String, String> map,
                       JsonHttpResponseHandler mMyJsonHttpResponseCacheHandler) {
        send(mContext, HttpMethod.DELETE, url, map,
                mMyJsonHttpResponseCacheHandler);
    }

    public void send(Context mContext, HttpMethod mHttpMethod, String url,
                     Map<String, String> Map,
                     JsonHttpResponseHandler mMyJsonHttpResponseCacheHandler) {
        if (mMyJsonHttpResponseCacheHandler == null)
            mMyJsonHttpResponseCacheHandler = new JsonHttpResponseHandler();
        mParameterMap.clear();
        if (Map != null)
            mParameterMap.putAll(Map);
        urlStr.setLength(0);
        if (url != null && url.indexOf("http://") == 0) {
            urlStr.append(url);
        } else {
            urlStr.append(BaseUrl.toString() + url);
        }
        switch (mHttpMethod) {

            case POST:
                XSimpleHttpUtil.getInstance().post(mContext, urlStr.toString(),
                        new RequestParams(mParameterMap),
                        mMyJsonHttpResponseCacheHandler);
                break;
            case PUT:
                XSimpleHttpUtil.getInstance().put(mContext, urlStr.toString(),
                        new RequestParams(mParameterMap),
                        mMyJsonHttpResponseCacheHandler);
                break;
            case DELETE:
                if (mParameterMap.isEmpty()) {
                    XSimpleHttpUtil.getInstance().delete(mContext, urlStr.toString(),
                            mMyJsonHttpResponseCacheHandler);
                } else {
                    XSimpleHttpUtil.getInstance().delete(urlStr.toString(),
                            new RequestParams(mParameterMap),
                            mMyJsonHttpResponseCacheHandler);
                }
                break;
            case GET:
            default:
                XSimpleHttpUtil.getInstance().get(mContext, urlStr.toString(),
                        new RequestParams(mParameterMap),
                        mMyJsonHttpResponseCacheHandler);

                break;
        }

    }

    public static JsonRestfulHeadModel JsonRestfulHeadPublicData(
            String s) {
        return XSimpleJsonBeen.json2JavaBean(s,
                JsonRestfulHeadModel.class);
    }

    public static JsonRestfulHeadModel JsonRestfulHeadPublicData(
            JSONObject s) {
        return JsonRestfulHeadPublicData(s.toString());
    }

}
