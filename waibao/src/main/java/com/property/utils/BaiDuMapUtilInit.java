package com.property.utils;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

public class BaiDuMapUtilInit {
    private LocationClient mLocationClient = null;
    private BDLocationListener myListener = new MyLocationListener();
    private Context mContext;
    private LocationCallback mLocationCallback;


    private static class Holder {
        private static BaiDuMapUtilInit instance = new BaiDuMapUtilInit();
    }

    private BaiDuMapUtilInit() {
    }

    public static BaiDuMapUtilInit getInstance() {
        return Holder.instance;
    }

    /**
     * 初始化
     */
    public void BaiDuMapDataInit(Context mmContext) {
//        SDKInitializer.initialize(mmContext);
        mContext = mmContext;

        /** 获取位置初始化 */
        mLocationClient = new LocationClient(mContext); // 声明LocationClient类
        mLocationClient.registerLocationListener(myListener); // 注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Battery_Saving);// 设置定位模式
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度，默认值gcj02
        option.setScanSpan(1);// 设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
        option.setIgnoreKillProcess(true);
        option.setNeedDeviceDirect(false);// 返回的定位结果包含手机机头的方向
        mLocationClient.setLocOption(option);
        if (mLocationClient.isStarted()) {
            mLocationClient.requestLocation();
        }
    }


    public void startBaiDuMapReceiveLocation(LocationCallback mLocationCallbacks) {
        this.mLocationCallback = mLocationCallbacks;
        if (mLocationClient != null) {
            if (!mLocationClient.isStarted())
                mLocationClient.start();
            if (mLocationClient.isStarted()) {
                mLocationClient.requestLocation();
            } else {
                if (mLocationCallback != null)
                    mLocationCallback.getLocationFail();
            }
        } else {
            if (mLocationCallback != null)
                mLocationCallback.getLocationFail();
        }

    }

    public void stopBaiDuMapReceiveLocation() {
        LocationOnDestroy();
    }




    /**
     * 获取经纬度
     */
    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation poiLocation) {
            // TODO Auto-generated method stub
            StringBuffer sb = new StringBuffer(256);
            sb.append("Poi time : ");
            sb.append(poiLocation.getTime());
            sb.append("\nerror code : ");
            sb.append(poiLocation.getLocType());
            sb.append("\nlatitude : ");
            sb.append(poiLocation.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(poiLocation.getLongitude());
            sb.append("\nradius : ");
            sb.append(poiLocation.getRadius());
            if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                sb.append("\naddr : ");
                sb.append(poiLocation.getAddrStr());
            }

            sb.append("noPoi information");

            // Log.e("onReceiveLocation", sb.toString());
            if (mLocationCallback != null) {
                mLocationCallback.getLocation(poiLocation);
            }
            if (mLocationClient != null) {
                mLocationClient.stop();
            }

        }

    }


    /**
     * 更新地图静态图片
     */
    public String getMapImageViewUrl(float Longitude, float Latitude, int zoom) {

        return "http://api.map.baidu.com/staticimage?width=340&height=240&center="
                + Longitude
                + ","
                + Latitude
                + "&markers="
                + Longitude
                + ","
                + Latitude
                + "&zoom="
                + zoom
                + "&markerStyles=l,,0xff0000&scale=2";
    }

    public void LocationOnDestroy() {
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.stop();
        }
    }

    public void onDestory() {
        LocationOnDestroy();
    }

    public interface LocationCallback {
        void getLocation(BDLocation location);

        void getLocationFail();

    }

//    public interface SearchCallback {
//        /**
//         * 获取地理编码结果
//         */
//        void onGetReverseGeoCodeResult(ReverseGeoCodeResult result);
//
//        /**
//         * 获取反向地理编码结果
//         */
//        void onGetGeoCodeResult(GeoCodeResult result);
//
//        void onGetErro(ERRORNO error);
//    }
}
