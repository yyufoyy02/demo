package com.vk.simpleutil.library;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.Configuration;

import java.util.List;

/**
 * 普通工具类
 * 
 * @author Administrator
 * 
 */
public class XSimpleUtil {
	private XSimpleUtil() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");

	}

	private static Context context;

	public static void init(Context contexts) {
		context = contexts.getApplicationContext();
	}

	public static boolean isPad(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	public static boolean isPad() {
		return isPad(context);

	}

	/** 判断当前应用程序处于前台还是后台 */
	public static boolean isApplicationBroughtToBackground(final Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (!tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			if (!topActivity.getPackageName().equals(context.getPackageName())) {
				return true;
			}
		}
		return false;

	}

	/** 判断当前应用程序处于前台还是后台 */
	public static boolean isApplicationBroughtToBackground() {
		return isApplicationBroughtToBackground(context);

	}
	private static long lastClickTime;
	/**
	 * 控件是否被快速点击
	 *
	 * @return
	 */
	public static boolean isFastClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < 800) {
			return true;
		}
		lastClickTime = time;
		return false;
	}
}
