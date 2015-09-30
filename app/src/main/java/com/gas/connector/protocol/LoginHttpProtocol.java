package com.gas.connector.protocol;

import com.gas.BaseApplication;
import com.gas.conf.Common;
import com.gas.conf.Config;
import com.gas.connector.ConnectorManage;
import com.gas.connector.HttpCallBack;
import com.gas.database.SharedPreferenceUtil;
import com.gas.utils.Utils;

import org.json.JSONObject;

import java.util.LinkedHashMap;

/**
 * Created by Heart on 2015/7/27.
 */
public class LoginHttpProtocol {
    public static long Post(String url,
                            LinkedHashMap<String, Object> map, HttpCallBack callback) {
        return ConnectorManage.getInstance().Post(url, map, callback);
    }

    public static long login(HttpCallBack callback, String username, String password) {
        LinkedHashMap<String, Object> entity = new LinkedHashMap<String, Object>();
        entity.put("username", username);
        entity.put("password", password);
        return Post(Config.loginUrl, entity, callback);
    }

    public static long serviceTime() {
        HttpCallBack callBack = new HttpCallBack() {
            @Override
            public void onGeneralSuccess(String result, long flag) {
                Utils.log("time", Utils.decodeUnicode(result));
                Utils.log("time", System.currentTimeMillis() / 1000);

                try {
                    JSONObject json = new JSONObject(result);
                    long service_time = json.getLong("localtime");
                    long local_tiem = System.currentTimeMillis() / 1000;
                    Common.getInstance().serverToClientTime = local_tiem - service_time;
                    SharedPreferenceUtil.getInstance(BaseApplication.mContext).putLong(SharedPreferenceUtil.TIME_DIFFERENCE, local_tiem - service_time);
                    SharedPreferenceUtil.getInstance(BaseApplication.mContext).putLong(SharedPreferenceUtil.SERVICE_TIEM, service_time);
                 } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onGeneralError(String e, long flag) {
                Common.getInstance().serverToClientTime = SharedPreferenceUtil.getInstance(BaseApplication.mContext).getLong(SharedPreferenceUtil.TIME_DIFFERENCE);
            }
        };
        return Post(Config.localTime, null, callBack);
    }
}
