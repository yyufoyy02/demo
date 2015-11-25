package com.property.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.property.activity.fault.FaultActivity;
import com.property.activity.maintenance.MaintenancePlanActivity;
import com.vk.simpleutil.library.XAppRouter;
import com.vk.simpleutil.library.XAppRouter.RouteNotFoundException;
import com.vk.simpleutil.library.XAppRouter.RouterCallback;
import com.vk.simpleutil.library.XSimpleLogger;
import com.vk.simpleutil.library.XSimpleUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

public class AppRoute {
    private static String urlTag;
    private static final int code = -1;
    private static Bundle mBundle = new Bundle();
    private static final String scheme = "weibao://";


    private static class Holder {
        private static AppRoute instance = new AppRoute();
    }

    private AppRoute() {
    }

    public static AppRoute getInstance() {
        return Holder.instance;
    }

    public static void goActivity(Context mContext, String url) {
        goActivity(mContext, url, null);
    }


    public static void goActivity(Context mContext, String url, Bundle extras) {
        route(mContext, url, extras);
    }


    private static String initRoute(String url, Bundle extras) {
        String newUrl = url.intern();
        if (url.startsWith(scheme)) {
            Uri uri = Uri.parse(url);
            newUrl = ((uri.getAuthority() != null ? uri.getAuthority() : "") + (uri
                    .getPath() != null ? uri.getPath() : "")).replace(".json",
                    "").intern();
            Iterator<String> iterator = uri.getQueryParameterNames().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                if (extras == null)
                    extras = new Bundle();
                extras.putString(key, uri.getQueryParameter(key));
            }

        } else {

            if (url.indexOf("://") == -1 && url.indexOf("%3a%2f%2") == -1) {
                Uri uri = Uri.parse(url);
                newUrl = ((uri.getAuthority() != null ? uri.getAuthority() : "") + (uri
                        .getPath() != null ? uri.getPath() : "")).replace(
                        ".json", "").intern();
                Iterator<String> iterator = uri.getQueryParameterNames()
                        .iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    if (extras == null)
                        extras = new Bundle();
                    extras.putString(key, uri.getQueryParameter(key));
                }
            } else if (url.indexOf("http://") != -1) {
                try {
                    newUrl = url.substring(0, url.indexOf("http://"))
                            + URLEncoder.encode(
                            url.substring(url.indexOf("http://")),
                            "utf-8").intern();
                } catch (UnsupportedEncodingException e1) {
                    // TODO Auto-generated catch block
                }

            } else if (url.indexOf("https://") != -1) {
                try {
                    newUrl = url.substring(0, url.indexOf("https://"))
                            + URLEncoder.encode(
                            url.substring(url.indexOf("https://")),
                            "utf-8").intern();
                } catch (UnsupportedEncodingException e1) {
                    // TODO Auto-generated catch block
                }

            }

        }
        mBundle.clear();
        if (extras != null)
            mBundle.putAll(extras);
        return newUrl;
    }

    static boolean FLAG_ACTIVITY_NEW_TASK = false;

    public static void initStartActivity(Context activity, Intent mIntent) {
        if (FLAG_ACTIVITY_NEW_TASK)
            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        FLAG_ACTIVITY_NEW_TASK = false;
        activity.startActivity(mIntent);
    }


    public static void setActivityNewTask(boolean FLAG_ACTIVITY_NEW_TASKs) {
        FLAG_ACTIVITY_NEW_TASK = FLAG_ACTIVITY_NEW_TASKs;
    }


    private static void route(final Context mActivity, String url,
                              Bundle extras) {
        if (XSimpleUtil.isFastClick() && urlTag.toString().equals(url))
            return;
        urlTag = url.intern();
        XSimpleLogger.Log().e("route:" + urlTag);
        final XAppRouter router = XAppRouter.sharedRouter();
        router.setContext(mActivity);
        router.map("fault_list", new RouterCallback() {
            @Override
            public void run(Map<String, String> params, Bundle extras) {
                // TODO Auto-generated method stub
                initStartActivity(mActivity, new Intent(mActivity, FaultActivity.class));
            }
        });
        router.map("plan_list", new RouterCallback() {
            @Override
            public void run(Map<String, String> params, Bundle extras) {
                // TODO Auto-generated method stub
//                if (extras == null || !extras.containsKey("maintenance_id"))
//                    return;
                initStartActivity(mActivity, new Intent(mActivity, MaintenancePlanActivity.class));
            }
        });
        try {
            router.open(initRoute(url, extras), mBundle);
        } catch (RouteNotFoundException e) {
            // TODO: handle exception
            e.printStackTrace();
        } catch (NullPointerException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}