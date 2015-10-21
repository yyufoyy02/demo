package com.vk.simpleutil.view;

import android.view.View;

import com.vk.simpleutil.view.pulltorefresh.lib.PullToRefreshBase.Mode;
import com.vk.simpleutil.view.pulltorefresh.lib.extras.IXListViewListener;

public interface PullToRefreshView {
	void setPullRefreshLoadEnable(final boolean topLayout,
								  final boolean footLayout, Mode mode);

	void setOnXListViewListener(IXListViewListener l);

	void addHeaderView(View v);

	void addHeaderView(View v, Object data, boolean isSelectable);

	void addFooterView(View v);
}
