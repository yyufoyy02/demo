package com.vk.simpleutil.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.vk.simpleutil.adapter.XSimpleRecyclerAdapter;
import com.vk.simpleutil.adapter.recycler.RecyclerArrayAdapter;
import com.vk.simpleutil.library.R;
import com.vk.simpleutil.view.pulltorefresh.internal.EmptyViewMethodAccessor;
import com.vk.simpleutil.view.pulltorefresh.internal.LoadingLayout;
import com.vk.simpleutil.view.pulltorefresh.internal.XListViewFooter;
import com.vk.simpleutil.view.pulltorefresh.lib.LoadingLayoutProxy;
import com.vk.simpleutil.view.pulltorefresh.lib.OverscrollHelper;
import com.vk.simpleutil.view.pulltorefresh.lib.PullToRefreshAdapterRecyclerViewBase;
import com.vk.simpleutil.view.pulltorefresh.lib.PullToRefreshBase;
import com.vk.simpleutil.view.pulltorefresh.lib.extras.IXListViewListener;

import java.util.ArrayList;
import java.util.List;

public class PullToRefreshRecyclerView extends PullToRefreshAdapterRecyclerViewBase<RecyclerView> implements PullToRefreshView {
    // the interface to trigger refresh and load more.
    private IXListViewListener mListViewListener;
    private LoadingLayout mHeaderLoadingView;
    private XListViewFooter mXListViewFooter;
    private FrameLayout mLvFooterLoadingFrame, mLvHeaderLoadingFrame;
    private RecyclerView.OnScrollListener mOnScrollListener;
    private boolean mListViewExtrasEnabled;
    private boolean topLayout, footLayout;
    private boolean isRefreshCompleteRunnable = true;
    private final List<View> mHeaders = new ArrayList<>();
    private final List<View> mFooters = new ArrayList<>();
    GridLayoutManager.SpanSizeLookup spanSizeLookup;

    public PullToRefreshRecyclerView(Context context) {
        super(context);
        init();
    }

    public PullToRefreshRecyclerView(Context context,
                                     AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PullToRefreshRecyclerView(Context context, PullToRefreshBase.Mode mode) {
        super(context, mode);
        init();
    }

    public PullToRefreshRecyclerView(Context context, PullToRefreshBase.Mode mode,
                                     PullToRefreshBase.AnimationStyle style) {
        super(context, mode, style);
        init();
    }

    void init() {
        addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mOnScrollListener != null)
                    mOnScrollListener.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mOnScrollListener != null)
                    mOnScrollListener.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    @Override
    public final PullToRefreshBase.Orientation getPullToRefreshScrollDirection() {
        return PullToRefreshBase.Orientation.VERTICAL;
    }

    @Override
    protected void onRefreshing(final boolean doScroll) {
        /**
         * If we're not showing the Refreshing view, or the list is empty, the
         * the header/footer views won't show so we use the normal method.
         */

        if (!mListViewExtrasEnabled || !getShowViewWhileRefreshing()
                || getItemCount() == 0) {
            super.onRefreshing(doScroll);
            return;
        }

        super.onRefreshing(false);

        final LoadingLayout origLoadingView;
        final int scrollToY;

        switch (getCurrentMode()) {
            case MANUAL_REFRESH_ONLY:
            case PULL_FROM_END:
                origLoadingView = getFooterLayout();
                scrollToY = getScrollY() - getFooterSize();
                break;
            case PULL_FROM_START:
            default:
                origLoadingView = getHeaderLayout();
                scrollToY = getScrollY() + getHeaderSize();
                break;
        }

        // Hide our original Loading View
        origLoadingView.reset();
        origLoadingView.hideAllViews();

        // Make sure the opposite end is hidden too
        switch (getCurrentMode()) {
            case MANUAL_REFRESH_ONLY:
            case PULL_FROM_END:
                if (footLayout) {
                    mXListViewFooter.setState(XListViewFooter.STATE_LOADING);
                }
                break;
            case PULL_FROM_START:
            default:
                if (topLayout) {

                    mHeaderLoadingView.setVisibility(View.VISIBLE);
                    mHeaderLoadingView.refreshing();

                }
                break;
        }

        // Show the ListView Loading View and set it to refresh.

        if (doScroll) {
            // We need to disable the automatic visibility changes for now
            disableLoadingLayoutVisibilityChanges();

            // We scroll slightly so that the ListView's header/footer is at the
            // same Y position as our normal header/footer

            setHeaderScroll(scrollToY);

            // Make sure the ListView is scrolled to show the loading
            // header/footer
            // scrollTop=listViewLoadingView
            // Smooth scroll as normal
            smoothScrollTo(0);

        }
    }


    @Override
    protected void onReset() {
        /**
         * If the extras are not enabled, just call up to super and return.
         */
        if (!mListViewExtrasEnabled) {
            super.onReset();
            return;
        }

        final LoadingLayout originalLoadingLayout;
        final int scrollToHeight, selection;
        final boolean scrollLvToEdge;

        switch (getCurrentMode()) {
            case MANUAL_REFRESH_ONLY:
            case PULL_FROM_END:
                originalLoadingLayout = getFooterLayout();
                selection = mRefreshableView.getChildCount() - 1;
                scrollToHeight = getFooterSize();
                scrollLvToEdge = Math.abs(getLastVisiblePosition()
                        - selection) <= 1;
                break;
            case PULL_FROM_START:
            default:
                originalLoadingLayout = getHeaderLayout();
                scrollToHeight = -getHeaderSize();
                selection = 0;
                scrollLvToEdge = Math.abs(getFirstVisiblePosition() - selection) <= 1;
                break;
        }

        // If the ListView header loading layout is showing, then we need to
        // flip so that the original one is showing instead
        if (footLayout) {
            mXListViewFooter.setState(XListViewFooter.STATE_NORMAL);
        }
        if (mHeaderLoadingView.getVisibility() == View.VISIBLE) {

            // Set our Original View to Visible
            originalLoadingLayout.showInvisibleViews();

            // Hide the ListView Header/Footer
            switch (getCurrentMode()) {
                case MANUAL_REFRESH_ONLY:
                case PULL_FROM_END:
                    break;
                case PULL_FROM_START:
                default:

                    mHeaderLoadingView.setVisibility(View.GONE);
                    break;
            }
            /**
             * Scroll so the View is at the same Y as the ListView
             * header/footer, but only scroll if: we've pulled to refresh, it's
             * positioned correctly
             */
            if (scrollLvToEdge && getState() != PullToRefreshBase.State.MANUAL_REFRESHING) {
                if (getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_START) {

                }
                setHeaderScroll(scrollToHeight);
            }
        }

        // Finally, call up to super
        super.onReset();
    }

    @Override
    protected LoadingLayoutProxy createLoadingLayoutProxy(
            final boolean includeStart, final boolean includeEnd) {
        LoadingLayoutProxy proxy = super.createLoadingLayoutProxy(includeStart,
                includeEnd);

        if (mListViewExtrasEnabled) {
            final PullToRefreshBase.Mode mode = getMode();

            if (includeStart && mode.showHeaderLoadingLayout()) {
                proxy.addLayout(mHeaderLoadingView);
            }
        }

        return proxy;
    }

    protected RecyclerView createListView(Context context,
                                          AttributeSet attrs) {
        final RecyclerView lv;
        if (VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD) {
            lv = new InternalListViewSDK9(context, attrs);
        } else {
            lv = new InternalListView(context, attrs);
        }
        return lv;
    }

    @Override
    protected RecyclerView createRefreshableView(Context context,
                                                 AttributeSet attrs) {
        RecyclerView lv = createListView(context, attrs);

        // Set it to this so it can be used in ListActivity/ListFragment
        lv.setId(android.R.id.list);
        return lv;
    }


    @Override
    protected void handleStyledAttributes(TypedArray a) {
        super.handleStyledAttributes(a);
        // mTypedArray = a;
        mListViewExtrasEnabled = a.getBoolean(
                R.styleable.PullToRefresh_ptrListViewExtrasEnabled, true);

        if (mListViewExtrasEnabled) {
            final FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER_HORIZONTAL);
            mHeaderLoadingView = createLoadingLayout(getContext(),
                    PullToRefreshBase.Mode.PULL_FROM_START, a);
            mLvHeaderLoadingFrame = new FrameLayout(getContext());
            mLvHeaderLoadingFrame.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mLvHeaderLoadingFrame.addView(mHeaderLoadingView, lp);
            mHeaderLoadingView.setVisibility(View.GONE);
            mLvFooterLoadingFrame = new FrameLayout(getContext());
            mLvFooterLoadingFrame.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mXListViewFooter = new XListViewFooter(getContext());
            mXListViewFooter.setVisibility(View.VISIBLE);
            mLvFooterLoadingFrame.addView(mXListViewFooter, lp);
            /**
             * If the value for Scrolling While Refreshing hasn't been
             * explicitly set via XML, enable Scrolling While Refreshing.
             */
            if (!a.hasValue(R.styleable.PullToRefresh_ptrScrollingWhileRefreshingEnabled)) {
                setScrollingWhileRefreshingEnabled(true);
            }

        }
    }

    @TargetApi(9)
    final class InternalListViewSDK9 extends InternalListView {

        public InternalListViewSDK9(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
                                       int scrollY, int scrollRangeX, int scrollRangeY,
                                       int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

            final boolean returnValue = super.overScrollBy(deltaX, deltaY,
                    scrollX, scrollY, scrollRangeX, scrollRangeY,
                    maxOverScrollX, maxOverScrollY, isTouchEvent);

            // Does all of the hard work...
            OverscrollHelper.overScrollBy(
                    PullToRefreshRecyclerView.this, deltaX,
                    scrollX, deltaY, scrollY, isTouchEvent);

            return returnValue;
        }
    }

    protected class InternalListView extends RecyclerView
            implements EmptyViewMethodAccessor {


        public InternalListView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected void dispatchDraw(Canvas canvas) {
            /**
             * This is a bit hacky, but Samsung's ListView has got a bug in it
             * when using Header/Footer Views and the list is empty. This masks
             * the issue so that it doesn't cause an FC. See Issue #66.
             */
            try {
                super.dispatchDraw(canvas);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent ev) {
            /**
             * This is a bit hacky, but Samsung's ListView has got a bug in it
             * when using Header/Footer Views and the list is empty. This masks
             * the issue so that it doesn't cause an FC. See Issue #66.
             */
            try {
                return super.dispatchTouchEvent(ev);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        public void setAdapter(Adapter mRecyclerArrayAdapter) {
            // Add the Footer View at the last possible moment
//            final Bookends adapter = new Bookends<>(mRecyclerArrayAdapter);
            if (mRecyclerArrayAdapter instanceof XSimpleRecyclerAdapter) {
                final XSimpleRecyclerAdapter adapter = (XSimpleRecyclerAdapter) mRecyclerArrayAdapter;
                if (null != mLvHeaderLoadingFrame && topLayout)
                    adapter.addHeader(mLvHeaderLoadingFrame);
                for (View v : mHeaders)
                    adapter.addHeader(v);
                for (View v : mFooters)
                    adapter.addFooter(v);
                if (null != mLvFooterLoadingFrame && footLayout)
                    adapter.addFooter(mLvFooterLoadingFrame);
                if (layoutManagerType == LAYOUT_MANAGER_TYPE.GRID)
                    ((GridLayoutManager) getLayoutManager()).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                        @Override
                        public int getSpanSize(int position) {
                            if (position < adapter.getHeaderCount() || position > (getItemCount() - adapter.getFooterCount() - 1)) {
                                return ((GridLayoutManager) getLayoutManager()).getSpanCount();
                            } else {
                                if (spanSizeLookup != null)
                                    return spanSizeLookup.getSpanSize(position - adapter.getHeaderCount());
                                return 1;
                            }
                        }
                    });
                super.setAdapter(adapter);
            }
            super.setAdapter(mRecyclerArrayAdapter);

        }

        @Override
        public void setEmptyView(View emptyView) {
//            MyPullToRefreshStickyGridHeadersGridView.this.setEmptyView(emptyView);
        }

        @Override
        public void setEmptyViewInternal(View emptyView) {
//            super.setEmptyView(emptyView);
        }

    }


    public void addHeaderView(View v) {
        if (v != null)
            mHeaders.add(v);
    }

    @Override
    public void addHeaderView(View v, Object data, boolean isSelectable) {
        addHeaderView(v);
    }


    public void addFooterView(View v) {
        if (v != null)
            mFooters.add(v);
    }

    public void notifyDataSetChanged() {
        if (mRefreshableView.getAdapter() != null)
            mRefreshableView.getAdapter().notifyDataSetChanged();
    }

    public void setAdapter(RecyclerArrayAdapter adapter) {
        mRefreshableView.setAdapter(adapter);

    }

    public RecyclerView.Adapter getAdapter() {
        return mRefreshableView.getAdapter();

    }

    public void setOnXListViewListener(IXListViewListener l) {
        if (l == null) {
            return;
        }
        mListViewListener = l;
        setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {

            @Override
            public void onPullDownToRefresh(
                    final PullToRefreshBase<RecyclerView> refreshView) {
                // TODO Auto-generated method stub
                topAction();
            }

            @Override
            public void onPullUpToRefresh(
                    final PullToRefreshBase<RecyclerView> refreshViews) {
                // TODO Auto-generated method stub
                footAction();
            }
        });

        mXListViewFooter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                mXListViewFooter.setState(XListViewFooter.STATE_LOADING);
                if (footLayout && mListViewListener != null) {
                    mListViewListener.onLoadMore();
                    postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            mXListViewFooter
                                    .setState(XListViewFooter.STATE_NORMAL);
                        }
                    }, 1000);
                }
            }
        });
    }

    private void topAction() {
        if (topLayout && mListViewListener != null) {
            postDelayed(new Runnable() {
                public void run() {
                    mListViewListener.onRefresh();
                }
            }, 1000);

        } else {
            if (isRefreshCompleteRunnable) {
                post(mRunnable);
            } else {
                onRefreshComplete();
            }
        }
    }

    private void footAction() {
        if (footLayout && mListViewListener != null) {
            postDelayed(new Runnable() {
                public void run() {
                    mListViewListener.onLoadMore();
                }
            }, 1000);

        } else {
            if (isRefreshCompleteRunnable) {
                post(mRunnable);
            } else {
                onRefreshComplete();
            }
        }
    }

    /**
     * 某些状态时,滑动是否延迟完成
     */
    public void setIsRefreshCompleteRunnable(boolean b) {
        isRefreshCompleteRunnable = b;
    }

    Runnable mRunnable = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            onRefreshComplete();
        }
    };

    /**
     * 是否有刷新加载
     */
    public void setPullRefreshLoadEnable(final boolean topLayout,
                                         final boolean footLayout, PullToRefreshBase.Mode mode) {
        this.topLayout = topLayout;
        this.footLayout = footLayout;
        setMode(mode);
        if (topLayout && !footLayout) {
            setRefreshType(PullToRefreshBase.RefreshType.down);
        } else if (!topLayout && footLayout) {
            setRefreshType(PullToRefreshBase.RefreshType.up);
        } else {
            setRefreshType(PullToRefreshBase.RefreshType.both);
        }
        getFooterLayout().hideAllViews();
        getFooterLayout().setProgressVisibity(false);
        getFooterLayout().setLoadingDrawable(
                getResources().getDrawable(R.drawable.transparent));
        getFooterLayout().setPullLabel("");
        getFooterLayout().setRefreshingLabel("");
        getFooterLayout().setReleaseLabel("");
        if (!footLayout || mode == PullToRefreshBase.Mode.PULL_FROM_START
                || mode == PullToRefreshBase.Mode.DISABLED) {
            mXListViewFooter.setVisibility(View.GONE);
        } else {
            mXListViewFooter.setVisibility(View.VISIBLE);
        }
        if (!topLayout) {
            /** 不显示 */
            mHeaderLoadingView.hideAllViews();
            mHeaderLoadingView.setPullLabel("");
            mHeaderLoadingView.setRefreshingLabel("");
            mHeaderLoadingView.setReleaseLabel("");
            mHeaderLoadingView.setProgressVisibity(false);
            getHeaderLayout().hideAllViews();
            getHeaderLayout().setProgressVisibity(false);
            getHeaderLayout().setPullLabel("");
            getHeaderLayout().setRefreshingLabel("");
            getHeaderLayout().setReleaseLabel("");
        } else {
            /** 显示 */
            getHeaderLayout().showInvisibleViews();
            getHeaderLayout().setProgressVisibity(true);
            getHeaderLayout().setLoadingImageView(R.drawable.updata_image);
            mHeaderLoadingView.setProgressVisibity(true);
        }
        if (!footLayout) {
            /** 不显示 */
            mXListViewFooter.setVisibility(View.GONE);
        } else {
            /** 显示 */
            mXListViewFooter.setVisibility(View.VISIBLE);
            setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
                @Override
                public void onLastItemVisible() {
                    setCurrentMode(PullToRefreshBase.Mode.PULL_FROM_END);
                    setRefreshing(false);
                }
            });
        }
        if (getAdapter() != null && getAdapter() instanceof RecyclerArrayAdapter)
            setAdapter((RecyclerArrayAdapter) getAdapter());
    }


    //    public void setStackFromEnd(final boolean b) {
//        // TODO Auto-generated method stub
//        if (getLayoutManager() == null)
//            return;
//        addOnLayoutManagerTypeListener(new LayoutManagerTypeListener() {
//            @Override
//            public void LINEAR(LinearLayoutManager mLinearLayoutManager) {
//                mLinearLayoutManager.setStackFromEnd(b);
//            }
//
//            @Override
//            public void GRID(GridLayoutManager mGridLayoutManager) {
//                mGridLayoutManager.setStackFromEnd(b);
//            }
//
//            @Override
//            public void STAGGERED_GRID(StaggeredGridLayoutManager mStaggeredGridLayoutManager) {
//            }
//        });
//    }
    public void scrollToPositionWithOffset(final int position, final int offset) {
        if (getLayoutManager() == null)
            return;
        addOnLayoutManagerTypeListener(new LayoutManagerTypeListener() {
            @Override
            public void LINEAR(LinearLayoutManager mLinearLayoutManager) {
                mLinearLayoutManager.scrollToPositionWithOffset(position, offset);
            }

            @Override
            public void GRID(GridLayoutManager mGridLayoutManager) {
                mGridLayoutManager.scrollToPositionWithOffset(position, offset);
            }

            @Override
            public void STAGGERED_GRID(StaggeredGridLayoutManager mStaggeredGridLayoutManager) {
            }
        });
    }

    public void setSpanSizeLookup(GridLayoutManager.SpanSizeLookup spanSizeLookup) {
        this.spanSizeLookup = spanSizeLookup;
    }

    /**
     * 滑动监听
     */
    public void addOnScrollListener(RecyclerView.OnScrollListener l) {
        mOnScrollListener = l;
    }

//    public Object getItemAtPosition(int position) {
//        return getAdapter().getItem(position);
//    }
}
