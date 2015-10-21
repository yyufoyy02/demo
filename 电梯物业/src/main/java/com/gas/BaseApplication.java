package com.gas;

import android.app.Application;
import android.content.Context;

import com.pgyersdk.crash.PgyCrashManager;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Heart on 2015/7/16.
 */
public class BaseApplication extends Application {

    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        this.mContext = getBaseContext();

        PgyCrashManager.register(this, "66a0cbb2f41686b59a52833aa45d0442");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

    }

}
