package com.vk.simpleutil.view.swipebacklayout.lib.app;

import com.vk.simpleutil.library.R;
import com.vk.simpleutil.view.swipebacklayout.lib.SwipeBackLayout;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

/**
 * @author Yrom
 * 
 */
public class SwipeBackActivityHelper {
	private Activity mActivity;
	private SwipeBackLayout mSwipeBackLayout;

	public SwipeBackActivityHelper(Activity activity) {
		mActivity = activity;
	}

	@SuppressWarnings("deprecation")
	public void onActivtyCreate() {
		mActivity.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		mActivity.getWindow().getDecorView().setBackgroundDrawable(null);
		mSwipeBackLayout = (SwipeBackLayout) LayoutInflater.from(mActivity)
				.inflate(R.layout.swipeback_layout,
						null);
	}

	public void onPostCreate() {
		mSwipeBackLayout.attachToActivity(mActivity);
	}

	public View findViewById(int id) {
		if (mSwipeBackLayout != null) {
			return mSwipeBackLayout.findViewById(id);
		}
		return null;
	}

	public SwipeBackLayout getSwipeBackLayout() {
		return mSwipeBackLayout;
	}

}
