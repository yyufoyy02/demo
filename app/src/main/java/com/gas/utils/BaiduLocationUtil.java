package com.property.utils;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.property.database.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

/**
 * Created by Heart on 2015/7/16.
 */
public class BaiduLocationUtil {
    private final String TAG = BaiduLocationUtil.class.getName() + "";
    private static BaiduLocationUtil mBaiduLocationUtil;
    private boolean isListener = false;
    private Timer timer;
    private Context mContext;
    private boolean mIsFirst = false;
    private List<BaiduCallBack> callBacks;
    private BaiduCallBack currentCallBack;
    private LocationClient client;
    private LocationClientOption option;
    private MyLocationListenner locCationListenner;
    private BaiduLocationUtil(Context context) {
        this.mContext = context;
        client = new LocationClient(context);
        option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy);
        option.setOpenGps(true);
        option.setIsNeedAddress(true);
        option.setCoorType("bd09ll");
        option.setScanSpan(0);
        callBacks = new ArrayList<BaiduCallBack>();
        startBaiduListener(null);
    }

    public static BaiduLocationUtil getInstance( Context context ){

        if(mBaiduLocationUtil == null){
            mBaiduLocationUtil = new BaiduLocationUtil(context);
        }

        return mBaiduLocationUtil;
    }

    public void delInstance(){
        mBaiduLocationUtil = null;
    }

    public void startBaiduListener(final BaiduCallBack baiduCallBack ){
     //   option.setAddrType("all");
        client.setLocOption(option);
        setCallBack(baiduCallBack);
        try {
            Utils.log( TAG + "startBaiduListener" , "isListener:" + isListener );
            synchronized (this) {
                if(!isListener){
                    isListener = true;
                    currentCallBack =baiduCallBack;
                    removeLister();
                    locCationListenner = new MyLocationListenner();
                    client.registerLocationListener(locCationListenner);
                    client.start();
                    LightTimer lt = new LightTimer() {
                        @Override
                        public void run(LightTimer timer) {
                            // TODO Auto-generated method stub
                            if(callBacks.contains(baiduCallBack)) {
                                 update(0, 0, 0, "", "");
                            }
                        }
                    };
                    lt.startTimerDelay(20 * 1000, 0,1);

                }
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            System.out.println("-----------------------------------------------------");
        }
    }

    private class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            Utils.log("dlocation", "onReceiveLocation:" + location.getAddrStr());
            String address = ",,,";
            String simpleAddress = "";
            String city;
            if(locCationListenner == null){
                  update( 0 , 0 , 0 , address , simpleAddress );
                 return;
            }
            Utils.log( "dlocation" , "type:" + location.getProvince( ) );

            final int lat = (int) (location.getLatitude()*1E6);
            final int lng = (int) (location.getLongitude()*1E6);
            Utils.log( "addrStr" , "type:" + location.getLatitude() +" "+location.getLongitude() );
            if(!Utils.isEmptyOrNullStr( location.getAddrStr( ) ) ){
                mIsFirst =true;
                address = location.getProvince( ) + "," + location.getCity( ) + ","
                        + location.getDistrict( ) + location.getStreet( )
                        + location.getStreetNumber( );
                simpleAddress = location.getCity( );
            }
            Utils.log( "type" , "type:" + location.getLocType( ) );

            int type = 1;
            if ( lat == 0 && lng == 0 )
            {
                type = 0;
            }

            if(type == 1){
                SharedPreferenceUtil.getInstance(mContext).putString(SharedPreferenceUtil.LONGITUDE,lat / 1e6 + "/" + lng / 1e6 + "/"
                        + address);
            }
            update( type , lat , lng , address , simpleAddress );
        }

    }

    private void update(int type , int lat , int lng , String address , String simpleAddress){
      synchronized (this) {
          removeLister();
          if (callBacks != null && callBacks.size() > 0) {
              if (callBacks.get(0) != null) {
                  callBacks.get(0).updateBaidu(type, lat, lng, address, simpleAddress);
              }
              callBacks.clear();
          }
      }
    }
    public void removeLister(){
        try {
            if(locCationListenner != null){
                client.unRegisterLocationListener(locCationListenner);
                locCationListenner = null;
            }
            client.stop();
        } catch (Exception e) {
            // TODO: handle exception

            e.printStackTrace();
        }
        isListener = false;

    }



    public void setCallBack(BaiduCallBack baiduCallBack){
        callBacks.add(baiduCallBack);
    }
    public interface BaiduCallBack {
        public void updateBaidu(int type, int lat, int lng, String address,
                                String simpleAddress);
    }
}
