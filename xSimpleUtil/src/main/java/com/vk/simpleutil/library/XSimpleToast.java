package com.vk.simpleutil.library;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Toast工具类
 * 
 * @author Administrator
 * 
 */
public class XSimpleToast {
	private XSimpleToast() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	static Context context;

	public static void init(Context contexts) {
		context = contexts.getApplicationContext();
	}

	private static Toast mToast;
	private static Handler mHandler = new Handler();
	private static Runnable r = new Runnable() {
		public void run() {
			mToast.cancel();
		}
	};

	public static void showToast(Context mContext, String text, int duration) {

		if (!XSimpleText.isEmpty(text) && !text.equals("")) {

			mHandler.removeCallbacks(r);
			if (mToast != null)
				mToast.setText(text);
			else
				mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
			mToast.setGravity(Gravity.CENTER, 0, 0);
			mHandler.postDelayed(r, duration);
			mToast.show();
		}
	}

	public static void showToast(String text, int duration) {
		showToast(context, text, duration);
	}

	public static void showToast(Context mContext, int resId, int duration) {
		showToast(mContext, mContext.getResources().getString(resId), duration);
	}

	public static void showToast(int resId, int duration) {
		showToast(context, resId, duration);
	}

	public static void showToast(String string) {
		// TODO Auto-generated method stub
		showToast(string, 1500);
	}

	public static void showToast(int resId) {
		// TODO Auto-generated method stub
		showToast(resId, 1500);
	}

}