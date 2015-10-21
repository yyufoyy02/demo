package com.vk.simpleutil.view.pulltorefresh.lib;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;

/**
 * 瀑布流下啦刷新
 * Created by sam on 15-8-30.
 */
public abstract class PullToRefreshAdapterRecyclerViewBase<T extends RecyclerView>
        extends PullToRefreshBase<T> {
    OnScrollListener ll;
    OnLastItemVisibleListener mOnLastItemVisibleListener;
    private boolean mLastItemVisible;
    private int[] lastScrollPositions;
    public LAYOUT_MANAGER_TYPE layoutManagerType;

    public PullToRefreshAdapterRecyclerViewBase(Context context) {
        super(context);
        init();
    }

    public PullToRefreshAdapterRecyclerViewBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PullToRefreshAdapterRecyclerViewBase(Context context, Mode mode) {
        super(context, mode);
        init();
    }

    public PullToRefreshAdapterRecyclerViewBase(Context context, Mode mode, AnimationStyle animStyle) {
        super(context, mode, animStyle);
        init();
    }

    @Override
    protected T createRefreshableView(Context context, AttributeSet attrs) {
        return null;
    }

    public void addOnScrollListener(OnScrollListener ll) {
        this.ll = ll;
    }

    public void setOnScrollListener(OnScrollListener ll) {
        addOnScrollListener(ll);
    }


    private int newStates;

    private void init() {
        getRefreshableView().setOverScrollMode(View.OVER_SCROLL_NEVER);
        getRefreshableView().addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView,
                                             int newState) {
                newStates = newState;
                if (ll != null)
                    ll.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && null != mOnLastItemVisibleListener && mLastItemVisible) {
                    mOnLastItemVisibleListener.onLastItemVisible();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (ll != null)
                    ll.onScrolled(recyclerView, dx, dy);
                if (null != mOnLastItemVisibleListener) {
                    mLastItemVisible = (getItemCount() > 0)
                            && (getFirstVisiblePosition() + getVisibleItemCount() >= getItemCount() - 1);
                }
            }
        });
    }

    public boolean isFirstItemVisible() {
//        XSimpleLogger.Log().e("getFirstCompletelyVisibleItemPosition() " + getFirstCompletelyVisibleItemPosition());
        if (getMode() == Mode.PULL_FROM_START || getMode() == Mode.BOTH) {
            if (getFirstCompletelyVisibleItemPosition() == 1 || getFirstCompletelyVisibleItemPosition() == 0) {
                return true;
            }
        } else {
            if (getFirstCompletelyVisibleItemPosition() == 0) {
                return true;
            }
        }

        return false;
    }

    public boolean isLastItemVisible() {
        boolean isLast = getItemCount() - 1 == getLastCompletelyVisibleItemPosition()
                || getItemCount() == getLastCompletelyVisibleItemPosition();
        return newStates == RecyclerView.SCROLL_STATE_IDLE && isLast;
    }

    @Override
    protected void onReset() {
        super.onReset();

    }

    protected void onRefreshing(boolean doScroll) {
        super.onRefreshing(doScroll);

    }

    @Override
    protected boolean isReadyForPullStart() {
        return isFirstItemVisible();
    }

    public final void setOnLastItemVisibleListener(
            OnLastItemVisibleListener listener) {
        mOnLastItemVisibleListener = listener;
    }

    @Override
    protected boolean isReadyForPullEnd() {

        return isLastItemVisible();
    }

    public int getFirstVisiblePosition() {
        return getFirstVisibleItemPosition(mRefreshableView.getLayoutManager());
    }

    public int getLastVisiblePosition() {
        return getLastVisibleItemPosition(mRefreshableView.getLayoutManager());
    }

    public int getVisibleItemCount() {
        return mRefreshableView.getLayoutManager().getChildCount();
    }

    public int getItemCount() {
        return mRefreshableView.getLayoutManager().getItemCount();
    }

    public int getFirstCompletelyVisibleItemPosition() {
        return getFirstCompletelyVisibleItemPosition(mRefreshableView.getLayoutManager());
    }

    public int getLastCompletelyVisibleItemPosition() {
        return getLastCompletelyVisibleItemPosition(mRefreshableView.getLayoutManager());
    }

    private int getLastVisibleItemPosition(RecyclerView.LayoutManager layoutManager) {
        int lastVisibleItemPosition = -1;
        initLayoutManagerType(layoutManager);
        switch (layoutManagerType) {
            case LINEAR:
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                break;
            case GRID:
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                break;
            case STAGGERED_GRID:
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                if (lastScrollPositions == null)
                    lastScrollPositions = new int[staggeredGridLayoutManager.getSpanCount()];

                staggeredGridLayoutManager.findLastVisibleItemPositions(lastScrollPositions);
                lastVisibleItemPosition = findMax(lastScrollPositions);
                break;
        }
        return lastVisibleItemPosition;
    }

    public void setLayoutManager(RecyclerView.LayoutManager layout) {
        initLayoutManagerType(layout);
        mRefreshableView.setLayoutManager(layout);
    }

    private int getFirstVisibleItemPosition(RecyclerView.LayoutManager layoutManager) {
        int lastVisibleItemPosition = -1;
        initLayoutManagerType(layoutManager);

        switch (layoutManagerType) {
            case LINEAR:
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                break;
            case GRID:
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
                break;
            case STAGGERED_GRID:
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                if (lastScrollPositions == null)
                    lastScrollPositions = new int[staggeredGridLayoutManager.getSpanCount()];

                staggeredGridLayoutManager.findFirstVisibleItemPositions(lastScrollPositions);
                lastVisibleItemPosition = findMin(lastScrollPositions);
                break;
        }
        return lastVisibleItemPosition;
    }

    private int getFirstCompletelyVisibleItemPosition(RecyclerView.LayoutManager layoutManager) {
        int visibleItemPosition = -1;
        initLayoutManagerType(layoutManager);
        switch (layoutManagerType) {
            case LINEAR:
                visibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
                break;
            case GRID:
                visibleItemPosition = ((GridLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
                break;
            case STAGGERED_GRID:
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                if (lastScrollPositions == null)
                    lastScrollPositions = new int[staggeredGridLayoutManager.getSpanCount()];

                staggeredGridLayoutManager.findFirstCompletelyVisibleItemPositions(lastScrollPositions);
                visibleItemPosition = findMin(lastScrollPositions);
                break;
        }
        return visibleItemPosition;
    }

    private int getLastCompletelyVisibleItemPosition(RecyclerView.LayoutManager layoutManager) {
        int visibleItemPosition = -1;
        initLayoutManagerType(layoutManager);
        switch (layoutManagerType) {
            case LINEAR:
                visibleItemPosition = ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
                break;
            case GRID:
                visibleItemPosition = ((GridLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
                break;
            case STAGGERED_GRID:
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                if (lastScrollPositions == null)
                    lastScrollPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(lastScrollPositions);
                visibleItemPosition = findMin(lastScrollPositions);
                break;
        }
        return visibleItemPosition;
    }

    private void initLayoutManagerType(RecyclerView.LayoutManager layoutManager) {
        if (layoutManagerType == null) {
            if (layoutManager instanceof GridLayoutManager) {
                layoutManagerType = LAYOUT_MANAGER_TYPE.GRID;
            } else if (layoutManager instanceof LinearLayoutManager) {
                layoutManagerType = LAYOUT_MANAGER_TYPE.LINEAR;
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                layoutManagerType = LAYOUT_MANAGER_TYPE.STAGGERED_GRID;
            } else {
                throw new RuntimeException("Unsupported LayoutManager used. Valid ones are LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager");
            }
        }
    }


    private int findMin(int[] lastPositions) {
        int min = Integer.MIN_VALUE;
        for (int value : lastPositions) {
            if (value < min)
                min = value;
        }
        return min;
    }

    private int findMax(int[] lastPositions) {
        int max = Integer.MIN_VALUE;
        for (int value : lastPositions) {
            if (value > max)
                max = value;
        }
        return max;
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return mRefreshableView.getLayoutManager();
    }

    public void addOnLayoutManagerTypeListener(RecyclerView.LayoutManager mLayoutManager, LayoutManagerTypeListener mLayoutManagerTypeListener) {
        if (mLayoutManager instanceof GridLayoutManager) {
            mLayoutManagerTypeListener.GRID((GridLayoutManager) mLayoutManager);
        } else if (mLayoutManager instanceof LinearLayoutManager) {
            mLayoutManagerTypeListener.LINEAR((LinearLayoutManager) mLayoutManager);
        } else if (mLayoutManager instanceof StaggeredGridLayoutManager) {
            mLayoutManagerTypeListener.STAGGERED_GRID((StaggeredGridLayoutManager) mLayoutManager);
        } else {
            throw new RuntimeException("Unsupported LayoutManager used. Valid ones are LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager");
        }
    }

    public void addOnLayoutManagerTypeListener(LayoutManagerTypeListener mLayoutManagerTypeListener) {
        switch (layoutManagerType) {
            case LINEAR:
                mLayoutManagerTypeListener.LINEAR((LinearLayoutManager) getLayoutManager());
                break;
            case GRID:
                mLayoutManagerTypeListener.GRID((GridLayoutManager) getLayoutManager());
                break;
            case STAGGERED_GRID:
                mLayoutManagerTypeListener.STAGGERED_GRID((StaggeredGridLayoutManager) getLayoutManager());
                break;
        }
    }

   public  interface LayoutManagerTypeListener {
        void LINEAR(LinearLayoutManager mLinearLayoutManager);

        void GRID(GridLayoutManager mGridLayoutManager);

        void STAGGERED_GRID(StaggeredGridLayoutManager mStaggeredGridLayoutManager);
    }

    public enum LAYOUT_MANAGER_TYPE {
        LINEAR,
        GRID,
        STAGGERED_GRID
    }

   public interface OnScrollListener {
        void onScrollStateChanged(RecyclerView recyclerView, int newState);

        void onScrolled(RecyclerView recyclerView, int dx, int dy);
    }
}