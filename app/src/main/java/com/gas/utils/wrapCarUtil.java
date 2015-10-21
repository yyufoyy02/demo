package com.property.utils;

import android.content.Context;
import android.content.Intent;

import com.property.database.SharedPreferenceUtil;
import com.property.ui.common.LocationService;

/**
 * Created by Heart on 2015/8/22.
 */
public class wrapCarUtil {
    public static void wrapCar(String carId){

    }

    public static boolean startLocation(Context mContext){
        boolean isShare =  SharedPreferenceUtil.getInstance(mContext).getString(SharedPreferenceUtil.SHARED_PLACES).equals("1");

        if(isShare){
            startService(mContext);
        }
        return  isShare;
    }

    public static void startLocation(Context mContext,String carId){
        SharedPreferenceUtil.getInstance(mContext).putString(SharedPreferenceUtil.SHARED_CAR,carId);
        SharedPreferenceUtil.getInstance(mContext).putString(SharedPreferenceUtil.SHARED_PLACES, "1");
        startService(mContext);
    }


    public static  void stopLocation(Context mContext){
        SharedPreferenceUtil.getInstance(mContext).putString(SharedPreferenceUtil.SHARED_PLACES,"0");
        SharedPreferenceUtil.getInstance(mContext).putString(SharedPreferenceUtil.SHARED_CAR, "");
        stopService(mContext);
    }

    private static void startService(Context mContext){
        Intent intent=new Intent(mContext, LocationService.class);
        mContext.startService(intent);
    }

    private static  void stopService(Context mContext){
        Intent intent=new Intent(mContext, LocationService.class);
        mContext.stopService(intent);

    }

}
