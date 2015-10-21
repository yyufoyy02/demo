package com.property;

import android.app.Application;
import android.content.Context;

/**
 * Created by Heart on 2015/7/16.
 */
public class BaseApplication extends Application {

    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

    }

}
