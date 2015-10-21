package com.vk.simpleutil.library;

import android.content.Context;
import android.util.TypedValue;

/**
 * 常用单位转换的辅助类
 * 
 * @author Administrator
 * 
 */
public class XSimpleDensity {
	private XSimpleDensity() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	private static Context mContext;

	public static void init(Context context, boolean pad) {
		mContext = context.getApplicationContext();
		isPad = pad;
	}

	private static boolean isPad = false;

	/**
	 * dp转px
	 * 
	 * @param context
	 * @param val
	 * @return
	 */
	public static int dp2px(Context context, float dpVal) {
		if (isPad)
			dpVal = (float) (dpVal * 1.5);
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dpVal, context.getResources().getDisplayMetrics());
	}

	public static int dp2px(float dpVal) {
		return dp2px(mContext, dpVal);
	}

	/**
	 * sp转px
	 * 
	 * @param context
	 * @param val
	 * @return
	 */
	public static int sp2px(Context context, float spVal) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
				spVal, context.getResources().getDisplayMetrics());
	}

	public static int sp2px(float spVal) {
		return sp2px(mContext, spVal);
	}

	/**
	 * px转dp
	 * 
	 * @param context
	 * @param pxVal
	 * @return
	 */

	public static int px2dp(Context context, float pxVal) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxVal / scale + 0.5f);
	}

	public static int px2dp(float pxVal) {
		return px2dp(mContext, pxVal);
	}

	/**
	 * px转sp
	 * 
	 * @param fontScale
	 * @param pxVal
	 * @return
	 */
	public static float px2sp(Context context, float pxVal) {
		return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
	}

	public static float px2sp(float pxVal) {
		return px2sp(mContext, pxVal);
	}
}