package com.property.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class CommonUtil {
	// 渠道包id
	private static String packageID;
	/**
	 * Get the current time format yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static String getTime() {
		Date date = new Date();
		SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return localSimpleDateFormat.format(date);
	}

	/**
	 * Judge wifi is available
	 * 
	 * @param inContext
	 * @return
	 */
	public static boolean isWiFiActive(Context inContext) {
		if (checkPermissions(inContext, "android.permission.ACCESS_WIFI_STATE")) {
			Context context = inContext.getApplicationContext();
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				NetworkInfo[] info = connectivity.getAllNetworkInfo();
				if (info != null) {
					for (int i = 0; i < info.length; i++) {
						if (info[i].getTypeName().equals("WIFI")
								&& info[i].isConnected()) {
							return true;
						}
					}
				}
			}
			return false;
		} else {
			Utils.log("lost permission",
					"lost--->android.permission.ACCESS_WIFI_STATE");
			return false;
		}
	}

	/**
	 * Testing equipment networking and networking WIFI
	 * 
	 * @param context
	 * @return true or false
	 */
	public static boolean isNetworkAvailable(Context context) {
		if (checkPermissions(context, "android.permission.INTERNET")) {

			ConnectivityManager cManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = cManager.getActiveNetworkInfo();
			if (info != null && info.isAvailable()) {
				return true;
			} else {

				Utils.log("error", "Network error");
				return false;
			}
		} else {
			Utils
					.log("lost permission", "android.permission.INTERNET");
			return false;
		}
	}

	/**
	 * checkPermissions
	 */
	public static boolean checkPermissions(Context context, String permission) {
		PackageManager localPackageManager = context.getPackageManager();
		return localPackageManager.checkPermission(permission,
				context.getPackageName()) == PackageManager.PERMISSION_GRANTED;
	}

	/**
	 * get currnet activity's name
	 */

	public static String getActivityName(Context context) {

		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		if (checkPermissions(context, "android.permission.GET_TASKS")) {
			ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
			return cn.getShortClassName();
		} else {
			Utils.log("lost permission",
					"android.permission.GET_TASKS");
			return null;
		}

	}

	/**
	 * get packageName
	 */
	public static String getPackageName(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		if (checkPermissions(context, "android.permission.GET_TASKS")) {
			ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
			return cn.getPackageName();
		} else {
			Utils.log("lost permission",
					"android.permission.GET_TASKS");
			return null;
		}

	}

	/**
	 * check phone _state is readied
	 */

	public static boolean checkPhoneState(Context context) {
		PackageManager packageManager = context.getPackageManager();
		if (packageManager
				.checkPermission("android.permission.READ_PHONE_STATE",
						context.getPackageName()) != 0) {
			return false;
		}
		return true;
	}

	/**
	 * get SDK version
	 */
	public static String getSdkVersion(Context context) {
		String osVersion = "";
		if (checkPhoneState(context)) {
			osVersion = android.os.Build.VERSION.RELEASE;
			Utils.log("android_osVersion", "OsVerson" + osVersion);
			return osVersion;
		} else {
			Utils.log("android_osVersion", "OsVerson get failed");
			return null;
		}
	}

	/**
	 * Get the current application version number
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersion(Context context) {
		String versionName = "";
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
			if (versionName == null || versionName.length() <= 0) {
				return "";
			}
			Utils.log("versionName", versionName);
		} catch (Exception e) {
			Utils.log("sAgent", "Exception", e);
		}
		return versionName;
	}

	/**
	 * get deviceid
	 */

	public static String getDeviceID(Context context) {
		if (checkPermissions(context, "android.permission.READ_PHONE_STATE")) {
			String deviceId = "";
			if (checkPhoneState(context)) {
				TelephonyManager tm = (TelephonyManager) context
						.getSystemService(Context.TELEPHONY_SERVICE);
				deviceId = tm.getDeviceId();
			}
			if (deviceId != null) {
				Utils.log("commonUtil", "deviceId:" + deviceId);
				return deviceId;
			} else {
				Utils.log("commonUtil", "deviceId is null");
				return null;
			}
		} else {
			Utils.log("lost permissioin",
					"lost----->android.permission.READ_PHONE_STATE");
			return "";
		}
	}

	/**
	 * 读取meta-data数据
	 */
	public static Object getMetaData( Context context , String metaName )
	{
		ApplicationInfo appInfo;
		try
		{
			appInfo = context.getPackageManager( ).getApplicationInfo(
					context.getPackageName( ) , PackageManager.GET_META_DATA );
			Object metaValue = appInfo.metaData.get( metaName );
			return metaValue;
		}
		catch ( PackageManager.NameNotFoundException e )
		{
			e.printStackTrace( );
		}
		return "";
	}
	/**
	 * get uuid
	 */

	public static String getUuid(Context context) {
		if (checkPermissions(context, "android.permission.READ_PHONE_STATE")) {
			String deviceId = "";
			String tmSerial = "";
			String androidId = "";
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			deviceId = tm.getDeviceId();
			tmSerial = tm.getSimSerialNumber();
			androidId = ""
					+ android.provider.Settings.Secure.getString(
							context.getContentResolver(),
							android.provider.Settings.Secure.ANDROID_ID);
			UUID deviceUuid = new UUID(androidId.hashCode(), ((long)deviceId.hashCode() << 32) | tmSerial.hashCode());
			return deviceUuid.toString();
		} else {
			Utils.log("lost permissioin",
					"lost----->android.permission.READ_PHONE_STATE");
			return "";
		}
	}

	/**
	 * get the current network type
	 */
	public static String getNetworkType(Context context) {
		if (isNetworkTypeWifi(context)) {
			return "WIFI";
		}
		TelephonyManager manager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		int type = manager.getNetworkType();
		String typeString = "UNKOWN";
		if (type == TelephonyManager.NETWORK_TYPE_CDMA) {
			typeString = "CDMA";
		}
		if (type == TelephonyManager.NETWORK_TYPE_EDGE) {
			typeString = "EDGE";
		}
		if (type == TelephonyManager.NETWORK_TYPE_EVDO_0) {
			typeString = "EVDO_0";
		}
		if (type == TelephonyManager.NETWORK_TYPE_EVDO_A) {
			typeString = "EVDO_A";
		}
		if (type == TelephonyManager.NETWORK_TYPE_GPRS) {
			typeString = "GPRS";
		}
		if (type == TelephonyManager.NETWORK_TYPE_HSDPA) {
			typeString = "HSDPA";
		}
		if (type == TelephonyManager.NETWORK_TYPE_HSPA) {
			typeString = "HSPA";
		}
		if (type == TelephonyManager.NETWORK_TYPE_HSUPA) {
			typeString = "HSUPA";
		}
		if (type == TelephonyManager.NETWORK_TYPE_UMTS) {
			typeString = "UMTS";
		}
		if (type == TelephonyManager.NETWORK_TYPE_UNKNOWN) {
			typeString = "UNKNOWN";
		}

		return typeString;
	}

	/**
	 * Determine the current network type
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkTypeWifi(Context context) {
		ConnectivityManager cManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cManager.getActiveNetworkInfo();

		if (info != null && info.isAvailable()
				&& info.getTypeName().equals("WIFI")) {
			return true;
		} else {
			Utils.log("error", "Network not wifi");
			return false;
		}
	}

	/**
	 * To determine whether it contains a gyroscope
	 * 
	 * @return
	 */
	public static boolean isHaveGravity(Context context) {
		SensorManager manager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);
		if (manager == null) {
			return false;
		}
		return true;
	}
}
