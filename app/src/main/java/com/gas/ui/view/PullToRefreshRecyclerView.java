package com.gas.ui.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;

/**
 * Created by Heart on 2015/8/2.
 */
public class PullToRefreshRecyclerView extends PullToRefreshAdapterViewBase{
    public PullToRefreshRecyclerView(Context context) {
        super(context);
    }

    public PullToRefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToRefreshRecyclerView(Context context, Mode mode) {
        super(context, mode);
    }

    public PullToRefreshRecyclerView(Context context, Mode mode, AnimationStyle animStyle) {
        super(context, mode, animStyle);
    }

    @Override
    public Orientation getPullToRefreshScrollDirection() {
        return null;
    }

    @Override
    protected RecyclerView createRefreshableView(Context context, AttributeSet attrs) {

        return null;
    }
}
