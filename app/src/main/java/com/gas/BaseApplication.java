package com.gas;

import android.app.Application;
import android.content.Context;
import android.telephony.TelephonyManager;

import com.gas.entity.User;
import com.gas.utils.BaiduLocationUtil;
import com.gas.utils.ImageViewUtil;
import com.gas.utils.StringEncrypt;
import com.pgyersdk.crash.PgyCrashManager;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Heart on 2015/7/16.
 */
public class BaseApplication extends Application{
    public static User user;
    public static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        ImageViewUtil.initDefault(getApplicationContext());
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        this.mContext = getBaseContext();
        BaiduLocationUtil.getInstance(this);
        PgyCrashManager.register(this, "66a0cbb2f41686b59a52833aa45d0442");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        ImageViewUtil.getDefault().clearDefaultLoaderMemoryCache();
    }


    /**
     * 获得设备id
     * @return
     */
    public static String getDeviceId(){
        String deviceId=((TelephonyManager)mContext.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        return StringEncrypt.encodeByAsymmetric(deviceId, StringEncrypt.EncodeType.MD5, StringEncrypt.Case.LOWER);
    }

}
