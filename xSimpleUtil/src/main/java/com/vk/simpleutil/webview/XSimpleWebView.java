package com.vk.simpleutil.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.vk.simpleutil.http.XSimpleHttpUtil;
import com.vk.simpleutil.library.XSimpleLogger;

import org.apache.http.cookie.Cookie;

import java.util.List;



public class XSimpleWebView extends WebView {

    public XSimpleWebView(Context context) {
        super(context);

    }

    public XSimpleWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub

    }

    public XSimpleWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    private static void synCookies(Context mContext, List<Cookie> cookies) {
        if (!cookies.isEmpty()) {
            CookieSyncManager.createInstance(mContext.getApplicationContext())
                    .startSync();
            CookieManager cookieManager = CookieManager.getInstance();
            CookieSyncManager.getInstance().sync();
            for (Cookie cookie : cookies) {
                String cookieString = cookie.getName() + "="
                        + cookie.getValue() + "; domain=" + cookie.getDomain();
                cookieManager.setCookie("http://meijialove.com/", cookieString);
                CookieSyncManager.getInstance().sync();
            }

            CookieSyncManager.getInstance().sync();
        }
    }

    public static void initCookies(Context context) {
        initCookies(context, XSimpleHttpUtil.getInstance().getCookie()
                .getCookies());
    }

    public static void initCookies(Context context, List<Cookie> mlist) {
        CookieSyncManager.createInstance(context.getApplicationContext())
                .startSync();
        synCookies(context.getApplicationContext(), mlist);
    }
    public static void cleanCookies(Context context) {
        CookieSyncManager.createInstance(context.getApplicationContext())
                .startSync();
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
    }

    @SuppressLint("SetJavaScriptEnabled")
    @SuppressWarnings("deprecation")
    public void initDefaultSettings(Context context) {
        setHorizontalScrollBarEnabled(false);// 水平不显示
        setVerticalScrollBarEnabled(false); // 垂直不显示
        WebSettings mWebSettings = getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setAllowFileAccess(false);
        // // 设置WebView属性，能够执行Javascript脚本
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        mWebSettings.setLoadsImagesAutomatically(true);
        mWebSettings.setBlockNetworkImage(false);
        mWebSettings.setBlockNetworkLoads(false);
        mWebSettings.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setPluginState(PluginState.ON);
        mWebSettings.setRenderPriority(RenderPriority.HIGH);
        mWebSettings.setCacheMode(WebSettings.LOAD_DEFAULT); // 设置
        // 缓存模式
        mWebSettings.setAppCacheEnabled(true);
        /**/
        mWebSettings.setBuiltInZoomControls(false);
        mWebSettings.setSaveFormData(true);
        // 启用定位数据库
        mWebSettings.setDatabaseEnabled(true);
        // 启用地理定位
        mWebSettings.setGeolocationEnabled(true);
        // 设置定位的数据库路径
        mWebSettings.setGeolocationDatabasePath(context.getApplicationContext()
                .getDir("database", Context.MODE_PRIVATE).getPath());
        setWebViewClient(XWebViewClient);
        setWebChromeClient(mWebChromeClient);
        if ((VERSION.SDK_INT > 13 && VERSION.SDK_INT < 16)
                || VERSION.SDK_INT == 21) {
            XSimpleLogger.Log().e("View.LAYER_TYPE_SOFTWARE");
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    WebViewClient XWebViewClient = new WebViewClient() {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        // 页面载入完成后调用
        @Override
        public void onPageFinished(WebView view, String url) {
            setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            super.shouldOverrideUrlLoading(view, url);
            if (url != null && !url.toLowerCase().startsWith("http")) {
                Uri uri = Uri.parse(url);
                Context context = getContext();
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
                return true;
            }

            loadUrl(url);
            return true;
        }
    };

    /**
     * 设置setWebChromeClient对象
     */
    WebChromeClient mWebChromeClient = new WebChromeClient() {
        // 配置权限（同样在WebChromeClient中实现）
        @Override
        public void onGeolocationPermissionsShowPrompt(String origin,
                                                       GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, false);
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {

        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);

        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message,
                                 JsResult result) {
            return super.onJsAlert(view, url, message, result);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }

    };

    public void onDestroy() {
        removeAllViews();
        destroy();
    }
}
