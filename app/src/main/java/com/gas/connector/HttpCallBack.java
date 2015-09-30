package com.gas.connector;

/**
 * Created by Heart on 2015/7/19.
 */
public interface HttpCallBack {
    public void onGeneralSuccess( String result , long flag ) ;
    public void onGeneralError( String e , long flag );
}
