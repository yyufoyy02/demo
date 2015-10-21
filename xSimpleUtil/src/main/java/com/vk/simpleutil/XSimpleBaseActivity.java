package com.vk.simpleutil;

import com.vk.simpleutil.view.swipebacklayout.lib.SwipeBackLayout;
import com.vk.simpleutil.view.swipebacklayout.lib.app.SwipeBackActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;

public class XSimpleBaseActivity extends SwipeBackActivity {
	private SwipeBackLayout mSwipeBackLayout;
	private static final int VIBRATE_DURATION = 20;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSwipeBackLayout = getSwipeBackLayout();
		mSwipeBackLayout.setSwipeListener(new SwipeBackLayout.SwipeListener() {
			@Override
			public void onScrollStateChange(int state, float scrollPercent) {

			}

			@Override
			public void onEdgeTouch(int edgeFlag) {
				vibrate(VIBRATE_DURATION);
			}

			@Override
			public void onScrollOverThreshold() {
				vibrate(VIBRATE_DURATION);
			}
		});
	}

	private void vibrate(long duration) {
		Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		long[] pattern = { 0, duration };
		vibrator.vibrate(pattern, -1);
	}

	@Override
	protected void onResume() {
		super.onResume();
		restoreTrackingMode();
	}

	private void restoreTrackingMode() {

		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);

	}
}
