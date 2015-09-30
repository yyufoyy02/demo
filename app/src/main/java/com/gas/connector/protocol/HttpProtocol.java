package com.gas.connector.protocol;

import com.gas.connector.ConnectorManage;
import com.gas.connector.HttpCallBack;

import java.util.LinkedHashMap;

/**
 * Created by Heart on 2015/7/19.
 */
public class HttpProtocol {
    public static long Post( String url,
                            LinkedHashMap<String, Object> map, HttpCallBack callback) {
        return ConnectorManage.getInstance().Post(url, map, callback);
    }

    public static long test(HttpCallBack callback){
        LinkedHashMap< String , Object > entity = new LinkedHashMap< String , Object >( );
        entity.put( "tn" , "myie2dg");
        return Post("http://www.weithink.com.cn/ranqi/index.php/Admin/Android/localtime",null,callback);
    }

}
