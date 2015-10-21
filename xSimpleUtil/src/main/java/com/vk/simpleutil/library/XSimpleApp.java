package com.vk.simpleutil.library;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiManager;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

/**
 * 跟App相关的辅助类
 * 
 * @author Administrator
 * 
 */
public class XSimpleApp {

	private XSimpleApp() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");

	}

	private static Context context;

	public static void init(Context contexts) {
		context = contexts.getApplicationContext();
	}

	/** 获取唯一android ID */
	public static String getDeviceAndroidId(Context mContext) {
		return Secure.getString(mContext.getContentResolver(),
				Secure.ANDROID_ID);
	}

	/** 获取唯一DeviceToken */
	public static String getDeviceToken() {
		return getDeviceAndroidId(context);
	}

	/** 获取唯一DeviceToken */
	public static String getDeviceToken(Context mContext) {
		TelephonyManager tm = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		String deviceid = tm.getDeviceId();
		if (deviceid == null || deviceid.length() == 0) {
			WifiManager manager = (WifiManager) mContext
					.getSystemService(Context.WIFI_SERVICE);
			if (manager != null) {
				deviceid = manager.getConnectionInfo().getMacAddress();
			}
		}
		return deviceid;
	}

	/** 获取唯一android ID */
	public static String getDeviceAndroidId() {
		return getDeviceAndroidId(context);
	}

	/**
	 * 获取应用程序名称
	 */
	public static String getAppName(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			int labelRes = packageInfo.applicationInfo.labelRes;
			return context.getResources().getString(labelRes);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getAppName() {

		return getAppName(context);
	}

	/**
	 * [获取应用程序版本名称信息]
	 * 
	 * @param context
	 * @return 当前应用的版本名称
	 */
	public static String getVersionName(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			return packageInfo.versionName;

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取版本号(内部识别号)
	 * 
	 * @return
	 */
	public static int getVersionCode(Context context) {
		try {
			return context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionCode;

		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return 0;
	}

	/**
	 * 获取版本号(内部识别号)
	 * 
	 * @return
	 */
	public static int getVersionCode() {
		return getVersionCode(context);
	}

	public static String getVersionName() {

		return getVersionName(context);
	}
}