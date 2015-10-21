package com.property.base;

import android.content.Context;
import android.support.v4.util.ArrayMap;

import com.google.gson.JsonElement;
import com.loopj.android.http.RequestParams;
import com.meijialove.MJLApplication;
import com.meijialove.http.utils.MyJsonHttpResponseCacheHandler;
import com.meijialove.http.utils.URLHelper;
import com.meijialove.modelresult.JsonRestfulHeadPublicDataResult;
import com.meijialove.result.JsonHeadPublicDataResult;
import com.vk.simpleutil.http.XSimpleHttpUtil;
import com.vk.simpleutil.json.GsonUtils;
import com.vk.simpleutil.json.XSimpleJsonBeen;
import com.vk.simpleutil.library.XSimpleACache;
import com.vk.simpleutil.library.XSimpleText;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

public class BaseApi {

    enum HttpMethod {
        GET, POST, PUT, DELETE
    }

    public static String BaseUrl = URLHelper.URL_MJB;
    public Map<String, String> mTempMap = new ArrayMap<>();
    private Map<String, String> mParameterMap = new ArrayMap<>();
    private StringBuffer urlStr = new StringBuffer();
    private StringBuffer strBuff = new StringBuffer();



    public void get(Context mContext, String url, Map<String, String> map,
                    String fieldStr,
                    MyJsonHttpResponseCacheHandler mMyJsonHttpResponseCacheHandler) {
        if (!XSimpleText.isEmpty(fieldStr)) {
            if (map == null)
                map = new ArrayMap<String, String>();
            map.put("fields", fieldStr);
        }
        send(mContext, HttpMethod.GET, url, map,
                mMyJsonHttpResponseCacheHandler);
    }

    public void getList(Context mContext, String url, Map<String, String> map,
                        String fieldStr,
                        MyJsonHttpResponseCacheHandler mMyJsonHttpResponseCacheHandler) {
        if (!XSimpleText.isEmpty(fieldStr)) {
            if (map == null)
                map = new ArrayMap<String, String>();
            map.put("fields", BaseModel.getChildFields("list[]", fieldStr));
        }
        send(mContext, HttpMethod.GET, url, map,
                mMyJsonHttpResponseCacheHandler);
    }

    public void put(Context mContext, String url, Map<String, String> map,
                    String fieldStr,
                    MyJsonHttpResponseCacheHandler mMyJsonHttpResponseCacheHandler) {
        if (!XSimpleText.isEmpty(fieldStr)) {
            if (map == null)
                map = new ArrayMap<String, String>();
            map.put("fields", fieldStr);
        }
        send(mContext, HttpMethod.PUT, url, map,
                mMyJsonHttpResponseCacheHandler);

    }

    public void putList(Context mContext, String url, Map<String, String> map,
                        String fieldStr,
                        MyJsonHttpResponseCacheHandler mMyJsonHttpResponseCacheHandler) {
        if (!XSimpleText.isEmpty(fieldStr)) {
            if (map == null)
                map = new ArrayMap<String, String>();
            map.put("fields", BaseModel.getChildFields("list[]", fieldStr));
        }
        send(mContext, HttpMethod.PUT, url, map,
                mMyJsonHttpResponseCacheHandler);
    }

    public void post(Context mContext, String url, Map<String, String> map,
                     String fieldStr,
                     MyJsonHttpResponseCacheHandler mMyJsonHttpResponseCacheHandler) {
        if (!XSimpleText.isEmpty(fieldStr)) {
            if (map == null)
                map = new ArrayMap<String, String>();
            map.put("fields", fieldStr);
        }
        send(mContext, HttpMethod.POST, url, map,
                mMyJsonHttpResponseCacheHandler);

    }

    public void postList(Context mContext, String url, Map<String, String> map,
                         String fieldStr,
                         MyJsonHttpResponseCacheHandler mMyJsonHttpResponseCacheHandler) {
        if (!XSimpleText.isEmpty(fieldStr)) {
            if (map == null)
                map = new ArrayMap<String, String>();
            map.put("fields", BaseModel.getChildFields("list[]", fieldStr));
        }
        send(mContext, HttpMethod.POST, url, map,
                mMyJsonHttpResponseCacheHandler);
    }

    public void delete(Context mContext, String url, Map<String, String> map, String fieldStr,
                       MyJsonHttpResponseCacheHandler mMyJsonHttpResponseCacheHandler) {
        if (!XSimpleText.isEmpty(fieldStr)) {
            if (map == null)
                map = new ArrayMap<>();
            map.put("fields", fieldStr);
        }
        send(mContext, HttpMethod.DELETE, url, map,
                mMyJsonHttpResponseCacheHandler);
    }

    public void delete(Context mContext, String url, Map<String, String> map,
                       MyJsonHttpResponseCacheHandler mMyJsonHttpResponseCacheHandler) {

        send(mContext, HttpMethod.DELETE, url, map,
                mMyJsonHttpResponseCacheHandler);
    }

    public void send(Context mContext, HttpMethod mHttpMethod, String url,
                     Map<String, String> Map,
                     MyJsonHttpResponseCacheHandler mMyJsonHttpResponseCacheHandler) {
        if (mMyJsonHttpResponseCacheHandler == null)
            mMyJsonHttpResponseCacheHandler = new MyJsonHttpResponseCacheHandler() {
                @Override
                public void onJsonSuccess(int statusCode, Header[] headers, JSONObject response) {

                }

                @Override
                public void onJsonFailure(int statusCode, Header[] headers, Throwable throwable, String errorResponse) {

                }

                @Override
                public boolean onJsonCache() {
                    return false;
                }

                @Override
                public boolean onJsonCacheHasData(boolean has) {
                    return false;
                }
            };
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

    /**
     * json字符串转化为 JavaBean
     *
     * @param <T>
     * @return
     */
    public static <T> T json2JavaBean(JsonElement mJsonElement,
                                      Class<T> valueBean) {

        return XSimpleJsonBeen
                .json2JavaBean(mJsonElement.toString(), valueBean);
    }

    public static <T> T json2JavaBean(String json, Class<T> valueBean) {

        return XSimpleJsonBeen.json2JavaBean(json, valueBean);
    }

    /**
     * json字符串转化为list
     *
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> List<T> json2JavaBeanList(JsonElement mJsonElement,
                                                Class<T> typeReference) {
        if (mJsonElement.isJsonObject()) {
            return XSimpleJsonBeen.json2JavaBeanList(mJsonElement.getAsJsonObject()
                    .get("list").toString(), typeReference);
        } else {
            return XSimpleJsonBeen.json2JavaBeanList(mJsonElement.toString(), typeReference);
        }
    }

    public static <T> List<T> json2JavaBeanList(JSONObject json,
                                                Class<T> typeReference) {
        try {
            return XSimpleJsonBeen.json2JavaBeanList(json
                    .get("list").toString(), typeReference);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JsonHeadPublicDataResult JsonHeadPublicData(String s) {
        JsonHeadPublicDataResult mJsonHeadPublicDataResult = new JsonHeadPublicDataResult();
        try {
            mJsonHeadPublicDataResult = json2JavaBean(s,
                    JsonHeadPublicDataResult.class);
            JsonElement jsonObject = GsonUtils.getJsonElement(s);
            mJsonHeadPublicDataResult.setData(jsonObject.getAsJsonObject()
                    .getAsJsonObject(("data")).getAsJsonObject("result")
                    .getAsJsonObject("data"));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mJsonHeadPublicDataResult;

    }

    public static JsonRestfulHeadPublicDataResult JsonRestfulHeadPublicData(
            String s) {
        return XSimpleJsonBeen.json2JavaBean(s,
                JsonRestfulHeadPublicDataResult.class);
    }

    public static JsonRestfulHeadPublicDataResult JsonRestfulHeadPublicData(
            JSONObject s) {
        return JsonRestfulHeadPublicData(s.toString());
    }
}
