package com.property;

import android.app.Application;
import android.content.Context;

import com.property.activity.R;
import com.vk.simpleutil.XSimpleBaseUtil;
import com.vk.simpleutil.XSimpleBaseUtil.Builder;
import com.vk.simpleutil.library.XSimpleImage;
import com.vk.simpleutil.library.XSimpleLogger;

/**
 * @author Administrator
 */
public class BaseApplication extends Application {


    /**
     * getContext
     */
    public static Context mContext;


    /**
     * A singleton instance of the application class for easy access in other
     * places
     */
    private static BaseApplication sInstance;
    public boolean isPad;
    public boolean debug;

    public void onCreate() {
        super.onCreate();
        sInstance = this;
        mContext = getApplicationContext();
        DebugMode(true);
        DataInit();
    }

    /**
     * DebugMode
     */
    private void DebugMode(Boolean Debug) {
        debug = Debug;

        XSimpleLogger.setDebugMode(Debug);// 日志文件调试模式
    }

    /**
     * 数据初始化
     */
    private void DataInit() {

        XSimpleBaseUtil.initConfig(new Builder().initContext(mContext)
                .isDebug(debug).isPad(isPad).alertDialogTheme(R.style.MyAlertDialogStyle)
                .initDefaultRequestParams(null).build());
        XSimpleImage.initDefaultImageLoaderConfig(getApplicationContext());

    }


    /**
     * synchronized
     *
     * @return ApplicationController singleton instance
     */
    public static BaseApplication getInstance() {

        return sInstance;
    }

}
