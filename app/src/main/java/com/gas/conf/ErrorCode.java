package com.property.conf;

import android.content.Context;

import com.property.BaseApplication;
import com.property.CloseAllActivity;
import com.property.epiboly.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Heart on 2015/7/28.
 */
public class ErrorCode {
    private static Map<Integer, String> map = new HashMap<Integer, String>();

    /** 网络连接失败 */
    public static final int E_101 = 101;
    /** 发送数据失败 */
    public static final int E_102 = 102;
    /** 连接超时 */
    public static final int E_103 = 103;
    /** 请求失败 **/
    public static final int E_104 = 104;

    private static Context mContext = BaseApplication.mContext;
    private static String[] errArr = mContext.getResources().getStringArray(
            R.array.ErrorCode);
    private static String err_item1 = mContext.getResources().getStringArray(
            R.array.ErrorCode)[0];
    private static String item1_id = err_item1.split("@")[0];
    private static String item1_desc = err_item1.split("@")[1];

    private static String err_item2 = mContext.getResources().getStringArray(
            R.array.ErrorCode)[1];
    private static String item2_desc = err_item2.split("@")[1];

    static {
        mContext = CloseAllActivity.getInstance().getTopActivity();
        errArr = mContext.getResources().getStringArray(R.array.ErrorCode);
        err_item1 = mContext.getResources().getStringArray(R.array.ErrorCode)[0];
        item1_id = err_item1.split("@")[0];
        item1_desc = err_item1.split("@")[1];
        for (int j = 0; j < errArr.length; j++) {
            String[] errdes = errArr[j].split("@");
            int a = Integer.parseInt(errdes[0]);
            String b = "";
            if(errdes.length>2)
            {
                for(int i = 1 ,iMax =errdes.length ;i<iMax ;i++ )
                {
                    b +=errdes[i];
                    if( i+1 < iMax )
                    {
                        b+="@" ;
                    }
                }
            }
            else
            {
                b =errdes[1];
            }
            map.put(a, b);
        }

    }
}
