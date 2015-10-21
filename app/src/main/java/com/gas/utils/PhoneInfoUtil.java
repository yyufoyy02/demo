package com.property.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.property.conf.Config;
import com.property.database.SharedPreferenceUtil;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Heart on 2015/5/28.
 */
public class PhoneInfoUtil {
    private TelephonyManager telephonyManager;
    private Context context;
    static PhoneInfoUtil phoneinfo;
    private Map<String, Object> infoMap;

    private PhoneInfoUtil(Context context) {
        telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        this.context = context;
        infoMap = new HashMap<String, Object>();
    }

    public int getAndroidOSVersionCode() {
        return Build.VERSION.SDK_INT;
    }


    public String getAndroidOSVersion() {
        return Build.VERSION.RELEASE;
    }

    public String getScreenDpi() {
        return context.getResources().getDisplayMetrics().widthPixels + "*"
                + context.getResources().getDisplayMetrics().heightPixels;
    }

    public int getSIMState() {
        return telephonyManager.getSimState();
    }

    public String getSIMStateStr(int state) {
        int index = 5;
        switch (state) {
            case TelephonyManager.SIM_STATE_ABSENT: {
                index = 0;
            }
            break;
            case TelephonyManager.SIM_STATE_NETWORK_LOCKED: {
                index = 1;
            }
            break;
            case TelephonyManager.SIM_STATE_PIN_REQUIRED: {
                index = 2;
            }
            break;
            case TelephonyManager.SIM_STATE_PUK_REQUIRED: {
                index = 3;
            }
            break;
            case TelephonyManager.SIM_STATE_READY: {
                index = 4;
            }
            break;
        }
        //String[] simStates = context.getResources().getStringArray(R.array.sim_states);
        //return simStates[index];
        return null;

    }

    public static PhoneInfoUtil getInstance(Context context) {
        if (phoneinfo == null) {
            phoneinfo = new PhoneInfoUtil(context);
        }
        return phoneinfo;
    }

    /**
     * 获取手机mac地址
     */
    public String macAddress() {
        String macAddress = "";
        if (infoMap.containsKey("mac_address")) {
            macAddress = (String) infoMap.get("mac_address");
        } else {
            try {
                WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo info = wifi.getConnectionInfo();
                macAddress = info.getMacAddress();
            } catch (Exception e) {

            }
            infoMap.put("mac_address", macAddress);
        }
        return macAddress;
    }

    /**
     * 平台
     */
    public int getPlat() {
        return Config.PLAT;
    }

    /**
     * 手机型号
     *
     * @return
     */
    public String getModel() {
        String model = "";
        if (infoMap.containsKey("get_model")) {
            model = (String) infoMap.get("get_model");
        } else {
            model = Build.MODEL;
            infoMap.put("get_model", model);
        }
        return model;
    }

    public String getBrand() {
        String brand = "";
        if (infoMap.containsKey("get_brand")) {
            brand = (String) infoMap.get("get_brand");
        } else {
            brand = Build.BRAND;
            infoMap.put("get_brand", brand);
        }
        return brand;
    }

    public String getDevice() {
        String device = "";
        if (infoMap.containsKey("get_device")) {
            device = (String) infoMap.get("get_device");
        } else {
            device = Build.DEVICE;
            infoMap.put("get_device", device);
        }
        return device;
    }

    public String getDeviceInfos() {
        return getBrand() + "@" + getDevice() + "@" + getModel();
    }

    /**
     * 固件号
     *
     * @return
     */
    public String getFramework() {
        String firewall = "";
        if (infoMap.containsKey("get_fire_wall")) {
            firewall = (String) infoMap.get("get_fire_wall");
        } else {
            firewall = Build.VERSION.RELEASE;
//			CommonFunction.log( "shifengxiong","android.os.Build.VERSION.CODENAME="+android.os.Build.VERSION.CODENAME );
//			CommonFunction.log( "shifengxiong","android.os.Build.VERSION.RELEASE="+android.os.Build.VERSION.RELEASE );
//			CommonFunction.log( "shifengxiong","android.os.Build.VERSION.SDK_INT="+android.os.Build.VERSION.SDK_INT );
            infoMap.put("get_fire_wall", firewall);
        }
        return firewall;
    }

    /**
     * 手机号
     *
     * @return
     * @author:linyg
     */
    public String getPhoneNum() {
        return telephonyManager.getLine1Number();
    }

    /**
     * 获取当前系统语言
     *
     */
    public String getLanguage() {
        return Locale.getDefault().getLanguage();
    }

    public String getSettingLang() {
        return context.getResources().getConfiguration().locale.getCountry();
    }

    /**
     * 获取当前国家和地区
     *
     * @return
      * @author:linyg
     */
    public String getCountry() {
        return Locale.getDefault().getCountry();
    }

    /**
     * 获取当前网络类型
     *
      * @return
     */
    public String getNetType() {
        String netType = "";
        try {
            ConnectivityManager conn = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            String type = conn.getActiveNetworkInfo().getTypeName();
            // 若当前是wifi网络，则直接返回wifi; 否则返回详细的网络连接类型
            return type;
            // if (type != null && type.equalsIgnoreCase("wifi")) {
            // netType = type;
            // } else {
            // context.getSystemService(Context.CONNECTIVITY_SERVICE);
            // NetworkInfo mobNetInfo = conn
            // .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            // netType = mobNetInfo.
            // }
        } catch (Exception e) {
        }
        return netType;
    }

    /**
     * 获取设备唯一码
     *
     * @return
     */
    public String getDeviceId() {
        boolean invalidDeviceID = false;
        // 获取imei号
        String deviceID = telephonyManager.getDeviceId();
        if (deviceID == null) {
            invalidDeviceID = true;
        } else if (deviceID.length() == 0 || deviceID.startsWith("000000000000")
                || deviceID.equals("0")) {
            invalidDeviceID = true;
        }

        // 如果未获取到 2.2以上的系统才能使用
        if (invalidDeviceID && Integer.parseInt(Build.VERSION.SDK) >= 9) {
            try {
                Class<?> c = Class.forName("android.os.SystemProperties");
                Method get = c.getMethod("get", String.class, String.class);
                deviceID = (String) (get.invoke(c, "ro.serialno", "unknown"));
            } catch (Exception e) {
            }

            if (deviceID == null) {
                invalidDeviceID = true;
            } else if (deviceID.length() == 0 || deviceID.startsWith("000000000000")
                    || deviceID.equals("0") || deviceID.equals("unknown")) {
                invalidDeviceID = true;
            } else {
                invalidDeviceID = false;
            }
        }

        // 以上都无法获取到，则从sharepreference和SD卡
        if (invalidDeviceID) {
            String spDeviceId = SharedPreferenceUtil.getInstance(context).getString(
                    SharedPreferenceUtil.DEVICEID);
            if (spDeviceId != null && !spDeviceId.equals("")) {
                deviceID = spDeviceId;
            } else {
                FileInputStream fs = null;
                try {
                    fs = new FileInputStream(Utils.getSDPath() + "/iaroundid");
                    BufferedReader br = new BufferedReader(new InputStreamReader(fs));
                    deviceID = br.readLine().trim();
                } catch (Exception e) {
                    e.printStackTrace();
                    deviceID = null;
                } finally {
                    try {
                        if (fs != null)
                            fs.close();
                    } catch (Exception e) {
                    }
                }
            }

            if (deviceID == null) {
                invalidDeviceID = true;
            } else if (deviceID.length() == 0 || deviceID.startsWith("000000000000")
                    || deviceID.equals("0") || deviceID.equals("unknown")) {
                invalidDeviceID = true;
            } else {
                invalidDeviceID = false;
                // 保存文件
                try {
                    FileWriter fw = new FileWriter(Utils.getSDPath() + "/iaroundid");
                    fw.write(deviceID, 0, deviceID.length());
                    fw.flush();
                } catch (Exception e) {
                }
            }
        }

        // 自动生成，并将保存在本地
        if (invalidDeviceID) {
            StringBuffer buff = new StringBuffer();
            buff.append("id_");
            String constantChars = "1234567890abcdefghijklmnopqrstuvw";
            for (int i = 0; i < 32; i++) {
                int randomChar = (int) (Math.random() * 100);
                int ch = randomChar % 30;
                buff.append(constantChars.charAt(ch));
            }
            deviceID = buff.toString().toLowerCase();
            // 保存文件
            try {
                FileWriter fw = new FileWriter(Utils.getSDPath() + "/iaroundid");
                fw.write(deviceID, 0, deviceID.length());
                fw.flush();
            } catch (Exception e) {
            }
        }
        SharedPreferenceUtil.getInstance(context).putString(SharedPreferenceUtil.DEVICEID,
                deviceID);
        return deviceID;
    }

    /**
     * 生产厂商
     *
     * @param @return
     * @return String
     */
    public String phoneManufacturer() {
        return Build.MANUFACTURER;
    }

    /**
     * 运营商名称
     *
     * @param @return
     * @return String
     */
    public String phoneCarrierName() {
        return telephonyManager.getNetworkOperatorName();
    }

    /**
     * 运营商国家码
     *
     * @param @return
     * @return String
     */
    public String phoneCarrierCountryIso() {
        return telephonyManager.getNetworkCountryIso();
    }

    /**
     * 手机国家码
     *
     * @param @return
     * @return String
     */
    public String phoneOperator() {
        return telephonyManager.getNetworkOperator();
    }

    /**
     * 获取电信运营商
     *
     * @return String
     */
    public String getIMSI() {
        String imsi = "";
        if (infoMap.containsKey("imsi")) {
            imsi = (String) infoMap.get("imsi");
        } else {
            String subscriberId = telephonyManager.getSubscriberId();
            if (subscriberId != null) {
                if (imsi.startsWith("46000") || subscriberId.startsWith("46002")) {// 中国移动
                    imsi = "中国移动";
                } else if (subscriberId.startsWith("46001")) {// 中国联通
                    imsi = "中国联通";
                } else if (imsi.startsWith("46003")) {// 中国电信
                    imsi = "中国电信";
                } else if (imsi.startsWith("46692")) {//台湾中华电信
                    imsi = "中华电信";
                } else {
                    imsi = "Unknow";
                }
            }
            infoMap.put("imsi", imsi);
        }
        return imsi;
    }

    /**
     * 获取电信运营商
     *
     * @return String
     */
    public String getIMSICode() {
        String imsicode = "";
        if (infoMap.containsKey("imsicode")) {
            imsicode = (String) infoMap.get("imsicode");
        } else {
            String subscriberId = telephonyManager.getSubscriberId();
            if (subscriberId == null) {
                subscriberId = "";
            }
            infoMap.put("imsicode", imsicode);
        }
        return imsicode;
    }

    /**
     * 是否为中国大陸
     *
     * @return boolean
     */
    public boolean isChinaCarrier() {
        boolean isChina = false;
        if (infoMap.containsKey("is_china_carrier")) {
            isChina = (Boolean) infoMap.get("is_china_carrier");
        } else {
            String imsi = telephonyManager.getSubscriberId();
            if (!TextUtils.isEmpty(imsi)) { // 含手机卡
                if (imsi.startsWith("460"))
                    isChina = true;
            } else { // 不含手机卡，根据地区
                if (getCountry().toLowerCase().equals("cn")) {
                    isChina = true;
                }
            }
            infoMap.put("is_china_carrier", isChina);
        }
        return isChina;
    }

    /**
     * imei号
     *
     * @return
     */
    public String getIMEI() {
        String imei = telephonyManager.getDeviceId();
        return imei == null ? "" : imei;
    }



    /**
     * 获取包签名值
     *
     * @param context
     * @return
     */
    public PackageInfo getPackageSign(Context context) {
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> apps = pm.getInstalledPackages(PackageManager.GET_SIGNATURES);
        Iterator<PackageInfo> iter = apps.iterator();
        while (iter.hasNext()) {
            PackageInfo info = iter.next();
            String packageName = info.packageName;
            if (packageName.equals("net.iaround")) {
                return info;
                // return info.signatures[0].toCharsString();
            }
        }
        return null;
    }

    /**
     * 获取注册唯一标识（时间戳），如果没有则生成
     *
     * @author tanzy
     */
//    public String getUniqueCode() {
//        String code = UniqueCodeModel.getInstant().getUniqueCode();
//        if (code == null) {
//            UniqueCodeModel.getInstant().putUniqueCode(
//                    CryptoUtil.md5(System.currentTimeMillis() + ""));
//        }
//
//        return UniqueCodeModel.getInstant().getUniqueCode();
//    }


}
