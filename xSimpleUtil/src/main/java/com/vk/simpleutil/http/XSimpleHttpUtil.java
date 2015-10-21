package com.vk.simpleutil.http;

import android.content.Context;
import android.util.SparseArray;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.vk.simpleutil.library.XSimpleLogger;

import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;

import java.util.List;
import java.util.Map;


/**
 * 网络请求框架
 */
public class XSimpleHttpUtil {
    private AsyncHttpClient client; // 实例化对象
    public PersistentCookieStore myCookieStore;
    private SparseArray<RequestHandle> requestHandles = new SparseArray<>();
    private Map<String, ?> map = null;

    // 单例模式实例化类

    private static class Holder {
        private static XSimpleHttpUtil instance = new XSimpleHttpUtil();
    }

    public static XSimpleHttpUtil getInstance() {
        return Holder.instance;
    }

    Context context;

    public void init(Context contexts) {
        context = contexts.getApplicationContext();
        client = new AsyncHttpClient();
        client.setTimeout(10 * 1000); // 设置链接超时，如果不设置，默认为10s
        if (myCookieStore == null)
            myCookieStore = new PersistentCookieStore(context);
        client.setCookieStore(myCookieStore);
//        client .addRequestProperty("Cache-Control", "no-cache");
    }

    public PersistentCookieStore getCookie() {
        if (myCookieStore == null) {
            myCookieStore = new PersistentCookieStore(context);
        }
        return myCookieStore;
    }

    /**
     * 设置cookie
     */
    public void setCookie(List<Cookie> cookies) {

        if (client != null) {
            PersistentCookieStore mCookieStore = (PersistentCookieStore) client
                    .getHttpContext().getAttribute(ClientContext.COOKIE_STORE);// 获取AsyncHttpClient中的CookieStor
            mCookieStore.clear();
            for (Cookie cookie : cookies) {
                mCookieStore.addCookie(cookie);
            }
            client.setCookieStore(mCookieStore);
        }
    }

    /**
     * clear cookie
     */
    public void clearCookie() {
        client.removeAllHeaders();
        myCookieStore.clear();
    }

    public void get(Context mContext, String urlString,
                    JsonHttpResponseHandler res) // 用一个完整url获取一个string对象
    {
        show(urlString, null);
        client.setTimeout(10 * 1000); // 设置链接超时，如果不设置，默认为10s
        client.get(mContext, initDafaultRequestParams(urlString), res);
    }

    public void get(Context mContext, String urlString, RequestParams params,
                    JsonHttpResponseHandler res) // 用一个完整url获取一个string对象
    {
        show(urlString, params);
        client.setTimeout(10 * 1000); // 设置链接超时，如果不设置，默认为10s
        client.get(mContext, urlString, initDafaultRequestParams(params), res);
    }

    public void get(String urlString, JsonHttpResponseHandler res) // 不带参数，获取json对象或者数组
    {
        show(urlString, null);
        client.setTimeout(10 * 1000); // 设置链接超时，如果不设置，默认为10s
        client.get(initDafaultRequestParams(urlString), res);
    }

    public void get(String urlString, RequestParams params,
                    JsonHttpResponseHandler res) // 带参数，获取json对象或者数组
    {
        show(urlString, params);
        client.setTimeout(10 * 1000); // 设置链接超时，如果不设置，默认为10s
        client.get(urlString, initDafaultRequestParams(params), res);
    }

    public void get(String urlString, BinaryHttpResponseHandler bHandler) // 下载数据使用，会返回byte数据
    {
        show(urlString, null);
        client.setTimeout(10 * 1000); // 设置链接超时，如果不设置，默认为10s
        client.get(initDafaultRequestParams(urlString), bHandler);
    }

    public void put(String urlString, JsonHttpResponseHandler res) // 不带参数，获取json对象或者数组
    {
        show(urlString, null);
        client.setTimeout(30 * 1000); // 设置链接超时，如果不设置，默认为10s
        client.put(urlString, res);
    }

    public void put(String urlString, RequestParams params,
                    JsonHttpResponseHandler res) // 不带参数，获取json对象或者数组
    {
        show(urlString, params);
        client.setTimeout(30 * 1000); // 设置链接超时，如果不设置，默认为10s
        client.put(urlString, initDafaultRequestParams(params), res);
    }

    public void put(Context mContext, String urlString, RequestParams params,
                    JsonHttpResponseHandler res) // 不带参数，获取json对象或者数组
    {
        show(urlString, params);
        client.setTimeout(30 * 1000); // 设置链接超时，如果不设置，默认为10s
        client.put(mContext, urlString, initDafaultRequestParams(params), res);
    }

    public void post(String urlString, RequestParams params,
                     JsonHttpResponseHandler res, int tag) { // 带参数post
        show(urlString, params);
        client.setTimeout(30 * 1000); // 设置链接超时，如果不设置，默认为10s
        res.sendRetryMessage(tag);
        RequestHandle handle = client.post(urlString,
                initDafaultRequestParams(params), res);
        requestHandles.put(tag, handle);
    }

    public void post(Context mContext, String urlString, RequestParams params,
                     JsonHttpResponseHandler res, int tag) { // 带参数post
        show(urlString, params);
        client.setTimeout(30 * 1000); // 设置链接超时，如果不设置，默认为10s
        res.sendRetryMessage(tag);
        RequestHandle handle = client.post(mContext, urlString,
                initDafaultRequestParams(params), res);
        requestHandles.put(tag, handle);
    }

    public void post(Context mContext, String urlString, RequestParams params,
                     JsonHttpResponseHandler res) { // 带参数post
        show(urlString, params);
        client.setTimeout(30 * 1000); // 设置链接超时，如果不设置，默认为10s
        client.post(mContext, urlString, initDafaultRequestParams(params), res);

    }

    public void delete(Context mContext, String urlString,
                       JsonHttpResponseHandler res, int tag) { // 带参数post
        show(urlString, null);
        client.setTimeout(10 * 1000); // 设置链接超时，如果不设置，默认为10s
        res.sendRetryMessage(tag);
        RequestHandle handle = client.delete(mContext,
                initDafaultRequestParams(urlString), res);
        requestHandles.put(tag, handle);

    }

    public void delete(String urlString, RequestParams params,
                       JsonHttpResponseHandler res) { // 带参数post
        show(urlString, null);
        client.setTimeout(10 * 1000); // 设置链接超时，如果不设置，默认为10s
        client.delete(urlString, initDafaultRequestParams(params), res);
    }

    public void delete(Context mContext, String urlString,
                       JsonHttpResponseHandler res) { // 带参数post
        show(urlString, null);
        client.setTimeout(10 * 1000); // 设置链接超时，如果不设置，默认为10s
        client.delete(mContext, urlString, res);
    }

    private void show(String urlString, RequestParams params) {
        if (params != null) {
            XSimpleLogger.Log().e(urlString + "[" + params.toString() + "]");
        } else {
            XSimpleLogger.Log().e(urlString);
        }
    }

    public AsyncHttpClient getClient() {

        return client;
    }

    /**
     * 根据tag，删除网络请求
     *
     * @param context
     * @param tag
     */
    public void cancleRequest(Context context, int tag) {

        if (requestHandles.get(tag) != null
                && !requestHandles.get(tag).isCancelled()) {
            requestHandles.get(tag).cancel(true);
            XSimpleLogger.Log().e("a" + requestHandles.get(tag));
        }

    }

    /**
     * 根据context，删除网络请求
     *
     * @param context
     */
    public void cancleRequest(Context context) {

        client.cancelRequests(context, true);
    }

    /**
     * 根据context，删除网络请求
     */
    public void cancleAllRequests() {

        client.cancelAllRequests(true);
    }

    private RequestParams initDafaultRequestParams(RequestParams params) {
        if (map != null) {
            if (params == null)
                params = new RequestParams();
            for (Map.Entry<String, ?> entry : map.entrySet()) {
                params.put(entry.getKey(), entry.getValue());
            }
        }
        return params;
    }

    private StringBuffer strbuff = new StringBuffer();

    private String initDafaultRequestParams(String url) {
        strbuff.setLength(0);
        strbuff.append(url);
        if (map != null) {
            if (strbuff.indexOf("?") == -1) {
                strbuff.append("?");
            }
            for (Map.Entry<String, ?> entry : map.entrySet()) {
                if (strbuff.charAt(strbuff.length() - 1) != '?')
                    strbuff.append("&");
                strbuff.append(entry.getKey());
                strbuff.append("=");
                strbuff.append(entry.getValue());
            }
        }
        return strbuff.toString();
    }

    public void setDafaultRequestParams(Map<String, ?> map) {
        this.map = map;
    }
}
