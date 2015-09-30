package com.gas.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.gas.conf.Common;
import com.gas.conf.Config;
import com.gas.database.SharedPreferenceUtil;
import com.gas.entity.User;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Calendar;

/**
 * Created by Heart on 2015/7/16.
 */
public class Utils {
    public static boolean uiRunning = false; // 标记程序是否已经进入UI运行模式（false表示处于后台service）
    private static float density;

    public static String getSDPath( )
    {
        String sdDir = "";
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED ); // 判断sd卡是否存在
        if ( sdCardExist )
        {
            sdDir = Environment.getExternalStorageDirectory( ) + "/gas/";// 获取跟目录

        }
        else
        {
            sdDir = "/data/data/com.gas/";
            // sdDir = Environment.getDataDirectory() + "/iAround/";
        }

        File file = new File( sdDir );
        if ( !file.exists( ) )
        {
            file.mkdirs( );
            if ( !file.exists( ) )
            {
                sdDir = "/data/data/com.gas/";
                File tryfile = new File( sdDir );
                if ( !tryfile.exists( ) )
                {
                    tryfile.mkdirs( );
                }
            }
        }
        return sdDir;
    }

    public static void log( String tag , Object... msg )
    {
        if ( Config.DEBUG && msg != null )
        {
            StringBuilder sb = new StringBuilder( );
            int i = 0;
            for ( Object o : msg )
            {
                if ( i > 0 )
                {
                    sb.append( ',' );
                }
                sb.append( o == null ? "null" : o.toString( ) );
                i++;
            }

            int logStrLength = sb.length( );
            int maxLogSize = 1000;
            for ( i = 0 ; i <= logStrLength / maxLogSize ; i++ )
            {
                int start = i * maxLogSize;
                int end = ( i + 1 ) * maxLogSize;
                end = end > logStrLength ? logStrLength : end;
                if ( tag.equals( "sherlock" ) )
                {
                    Log.i(tag, sb.substring(start, end));
                }
                else
                {
                    Log.v( "DongZ_" + tag , sb.substring( start , end ) );
                }
            }
        }
    }


    public static boolean isEmptyOrNullStr( String str )
    {
        return TextUtils.isEmpty(str) || "".equals( str );
    }

    public static void toastMsg( Context context , String sMsg )
    {
        Toast.makeText(context, sMsg, Toast.LENGTH_SHORT).show( );
    }

    public static void dumpLogcat( )
    {
        if ( !Config.DEBUG )
        {
            try
            {
                File uncaughtExceptionLogFolder = new File( getSDPath( )
                        + "UncaughtExceptions/" );
                if ( !uncaughtExceptionLogFolder.exists( ) )
                {
                    uncaughtExceptionLogFolder.mkdirs( );
                }
                String fileName = TimeFormat.convertTimeLong2String(
                        System.currentTimeMillis( ) , Calendar.SECOND )
                        + ".txt";
                String dumpFile = uncaughtExceptionLogFolder.getAbsolutePath( ) + "/"
                        + fileName;
                Process pDump = Runtime.getRuntime( )
                        .exec( "logcat -v time -d -f " + dumpFile );
                pDump.waitFor( );
                Utils.log( "System.err" , "Log file has been dump to \"" + dumpFile
                        + "\"" );
            }
            catch ( Exception e )
            {
                e.printStackTrace( );
            }
        }
    }

    public static String decodeUnicode(String theString){
        char aChar;

        int len = theString.length();

        StringBuffer outBuffer = new StringBuffer(len);

        for (int x = 0; x < len;) {

            aChar = theString.charAt(x++);

            if (aChar == '\\') {

                aChar = theString.charAt(x++);

                if (aChar == 'u') {

                    // Read the xxxx

                    int value = 0;

                    for (int i = 0; i < 4; i++) {

                        aChar = theString.charAt(x++);

                        switch (aChar) {

                            case '0':

                            case '1':

                            case '2':

                            case '3':

                            case '4':

                            case '5':

                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed   \\uxxxx   encoding.");
                        }

                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';

                    else if (aChar == 'n')

                        aChar = '\n';

                    else if (aChar == 'f')

                        aChar = '\f';

                    outBuffer.append(aChar);

                }

            } else

                outBuffer.append(aChar);

        }

        return outBuffer.toString();

    }

    /** dip转px */
    public static int dipToPx( Context context , int dip )
    {
        if ( density <= 0 )
        {
            density = context.getResources( ).getDisplayMetrics( ).density;
        }
        return ( int ) ( dip * density + 0.5f );
    }

    /** px转dip */
    public static int pxToDip( Context context , int px )
    {
        if ( density <= 0 )
        {
            density = context.getResources( ).getDisplayMetrics( ).density;
        }
        return ( int ) ( ( px - 0.5f ) / density );
    }

    public static void dialAlert(final String dialNamber, final Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("电话号:"+ dialNamber);
        builder.setTitle("确定要拨打电话？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                Uri uri = Uri.parse("tel:" + dialNamber);
                intent.setAction(intent.ACTION_CALL);
                intent.setData(uri);
                context.startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                arg0.dismiss();

            }
        });
        builder.create().show();
    }

    public static void MapPilot(final String address, final Context mContext){
        try {

            final String latlng[] = SharedPreferenceUtil.getInstance(mContext).getString(SharedPreferenceUtil.LONGITUDE).split("/");
             final StringBuffer Uri1 =new StringBuffer("intent://map/direction?origin=");


            User user = Common.getInstance().user;
        //    Intent intent = Intent.getIntent("intent://map/direction?origin=latlng:"+" "+","+" "+"|name:顾客&destination=大雁塔&mode=drivingion=西安&referer=Autohome|GasStation#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");


            if(latlng.length <2) {
                BaiduLocationUtil.getInstance(mContext).startBaiduListener(new BaiduLocationUtil.BaiduCallBack() {

                    public void updateBaidu(int type, int lat, int lng, String addressLoc,
                                            String simpleAddress) {
                        try {
                            StringBuffer uriNew = new StringBuffer("intent://map/direction?origin=");
                            uriNew.append("latlng:" + latlng[0] + "," + latlng[1]);
                            uriNew.append("&destination=" + address);
                            uriNew.append("&mode=driving");
                            uriNew.append("&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                            log("baidu uriNew", Uri1.toString());
                            if(isInstallByread("com.baidu.BaiduMap")){

                                Intent intent = Intent.getIntent(uriNew.toString());
                                mContext.startActivity(intent);

                            }else{
                                toastMsg(mContext,"请先安装百度地图客户端");
                            }
                            return;
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }else {


                Uri1.append("latlng:"+latlng[0]+","+latlng[1]+"|name:位置");
                Uri1.append("&destination="+address);
                Uri1.append("&mode=driving");
                Uri1.append("&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");

                log("baidu",Uri1.toString());
                if(isInstallByread("com.baidu.BaiduMap")){

                    Intent intent = Intent.getIntent(Uri1.toString());
                    mContext.startActivity(intent);

                }else{
                    toastMsg(mContext, "请先安装百度地图客户端");
                }

            }


        } catch (URISyntaxException e) {

            e.printStackTrace();

        }
//        [/mw_shl_code]
//
//        但是，若客户端没有安装了百度地图客户端就会报 No Activity.... 直接会导致程序挂掉，这是开发者不希望看到的.所以在调用之前判断该手机是否安装了百度地图客户端，我们知道百度的包名为 com.baidu.BaiduMap
//
//        所以通过下面的方法进行判断：
//
//        [mw_shl_code=java,true]
       /**
         * 判断是否安装目标应用

         * @param packageName 目标应用安装后的包名

         * @return 是否已安装目标应用

         */


       // 使用Intent调用高德地图

//
//        try {
//
//           Intent intent = Intent.getIntent("androidamap://path?sourceApplication=GasStation" +
//                   "&sid=BGVIS1&slat=34.264642646862&slon=108.95108518068" +
//                   "&sname=当前位置" +
//                   "&did=BGVIS2&dlat=36.3&dlon=116.2&dname=终点位置&dev=1&m=2&t=0");
//
//            if(isInstallByread("com.autonavi.minimap")){
//
//                mContext.startActivity(intent); //启动调用
//
//                Log.i("GasStation", "高德地图客户端已经安装") ;
//            }else{
//                Log.d("GasStation", "没有安装高德地图客户端") ;
//            }
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//
//        }
    }



    private static boolean isInstallByread(String packageName) {

        return new File("/data/data/" + packageName).exists();

    }


    public static void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * listAdapter.getCount() )+40;
        listView.setLayoutParams(params);
    }
}
