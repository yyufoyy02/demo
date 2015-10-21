package com.property.ui.common;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.property.conf.Common;
import com.property.connector.HttpCallBack;
import com.property.connector.protocol.BusinessHttpProtocol;
import com.property.database.SharedPreferenceUtil;
import com.property.entity.User;
import com.property.utils.BaiduLocationUtil;
import com.property.utils.LightTimer;

/**
 * Created by Heart on 2015/8/22.
 */
public class LocationService extends Service implements HttpCallBack{


    private LightTimer locationTimer;

    private User user = Common.getInstance().user;
    private String carId;

    private int loopTime = 3*60*1000;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private static final String TAG="Test";

    @Override
    //Service时被调用
    public void onCreate()
    {
        Log.i(TAG, "Service onCreate--->");
        carId = SharedPreferenceUtil.getInstance(this).getString(SharedPreferenceUtil.SHARED_CAR);
        locationTimer = new LightTimer() {
            @Override
            public void run(LightTimer timer) {
                sendLocation();
            }
        };
        // if(Config.DEBUG) loopTime = 10000;
        locationTimer.startTimer(loopTime);
        super.onCreate();
    }

    @Override
    //当调用者使用startService()方法启动Service时，该方法被调用
    public void onStart(Intent intent, int startId)
    {
        Log.i(TAG, "Service onStart--->");
        super.onStart(intent, startId);
    }

    @Override
    //当Service不在使用时调用
    public void onDestroy()
    {
        Log.i(TAG, "Service onDestroy--->");
        super.onDestroy();
        locationTimer.stop();
    }

    private void sendLocation(){
        BaiduLocationUtil.getInstance(this).startBaiduListener(new BaiduLocationUtil.BaiduCallBack() {
            @Override
            public void updateBaidu(int type, int lat, int lng, String address, String simpleAddress) {
                BusinessHttpProtocol.siteCar(LocationService.this,carId,lat,lng);
            }
        });
    }

    @Override
    public void onGeneralError(String e, long flag) {

    }

    @Override
    public void onGeneralSuccess(String result, long flag) {

    }
}
