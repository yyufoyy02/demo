/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.vk.simpleutil.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListAdapter;

import com.vk.simpleutil.library.R;
import com.vk.simpleutil.view.pulltorefresh.huewu.pla.lib.MultiColumnListView;
import com.vk.simpleutil.view.pulltorefresh.huewu.pla.lib.MultiColumnListView.XListViewItemLongListener;
import com.vk.simpleutil.view.pulltorefresh.huewu.pla.lib.internal.PLA_AbsListView;
import com.vk.simpleutil.view.pulltorefresh.huewu.pla.lib.internal.PLA_AbsListView.OnScrollListener;
import com.vk.simpleutil.view.pulltorefresh.internal.EmptyViewMethodAccessor;
import com.vk.simpleutil.view.pulltorefresh.internal.LoadingLayout;
import com.vk.simpleutil.view.pulltorefresh.internal.XListViewFooter;
import com.vk.simpleutil.view.pulltorefresh.lib.LoadingLayoutProxy;
import com.vk.simpleutil.view.pulltorefresh.lib.OverscrollHelper;
import com.vk.simpleutil.view.pulltorefresh.lib.PullToRefreshAdapterMultiColumnListViewBase;
import com.vk.simpleutil.view.pulltorefresh.lib.PullToRefreshBase;
import com.vk.simpleutil.view.pulltorefresh.lib.extras.IXListViewListener;

public class PullToRefreshXListView extends
        PullToRefreshAdapterMultiColumnListViewBase<MultiColumnListView>
        implements PullToRefreshView {
    // the interface to trigger refresh and load more.
    private IXListViewListener mListViewListener;
    private LoadingLayout mHeaderLoadingView;
    private XListViewFooter mXListViewFooter;
    private FrameLayout mLvFooterLoadingFrame;
    private OnScrollListener mOnScrollListener;
    private boolean mListViewExtrasEnabled;
    private boolean topLayout, footLayout;
    private boolean isRefreshCompleteRunnable = true;

    public PullToRefreshXListView(Context context) {

        super(context);
    }

    public PullToRefreshXListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToRefreshXListView(Context context, Mode mode) {
        super(context, mode);
    }

    public PullToRefreshXListView(Context context, Mode mode,
                                  AnimationStyle style) {
        super(context, mode, style);
    }

    @Override
    public final Orientation getPullToRefreshScrollDirection() {
        return Orientation.VERTICAL;
    }

    @Override
    protected void onRefreshing(final boolean doScroll) {
        /**
         * If we're not showing the Refreshing view, or the list is empty, the
         * the header/footer views won't show so we use the normal method.
         */
        ListAdapter adapter = mRefreshableView.getAdapter();
        if (!mListViewExtrasEnabled || !getShowViewWhileRefreshing()
                || null == adapter || adapter.isEmpty()) {
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
                mHeaderLoadingView.setVisibility(View.GONE);
                if (footLayout) {
                    mXListViewFooter.setState(XListViewFooter.STATE_LOADING);
                }
                break;
            case PULL_FROM_START:
            default:
                if (topLayout) {

                    mHeaderLoadingView.setVisibility(View.VISIBLE);
                    mHeaderLoadingView.refreshing();
//                    if (mHeaderLoadingView.getGifDrawable() != null) {
//                        mHeaderLoadingView.getGifDrawable().start();
//                    }
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
            if (getCurrentMode() == Mode.PULL_FROM_START) {
                mRefreshableView.setSelectionFromTop(0, 0);
            }
            // scrollTop=listViewLoadingView
            // mRefreshableView.setSelectionFromTop(0, 0);
            // Smooth scroll as normal
            smoothScrollTo(0);

        }
    }

    @Override
    public void onScrollStateChanged(PLA_AbsListView view, int scrollState) {
        // TODO Auto-generated method stub
        super.onScrollBaseonScrollBase(view, scrollState);
        if (mOnScrollListener != null) {
            mOnScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(PLA_AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        // TODO Auto-generated method stub
        super.onScrollBase(view, firstVisibleItem, visibleItemCount, totalItemCount);
        if (mRefreshableView.getAdapter() != null
                && !mRefreshableView.getAdapter().isEmpty()) {
            if (mLvFooterLoadingFrame.getVisibility() != View.VISIBLE) {
                mLvFooterLoadingFrame.setVisibility(View.VISIBLE);
            }
        } else {
            if (mLvFooterLoadingFrame.getVisibility() != View.GONE) {
                mLvFooterLoadingFrame.setVisibility(View.GONE);
            }
        }
        if (mOnScrollListener != null) {
            mOnScrollListener.onScroll(view, firstVisibleItem,
                    visibleItemCount, totalItemCount);
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
                selection = mRefreshableView.getCount() - 1;
                scrollToHeight = getFooterSize();
                scrollLvToEdge = Math.abs(mRefreshableView.getLastVisiblePosition()
                        - selection) <= 1;
                break;
            case PULL_FROM_START:
            default:
                originalLoadingLayout = getHeaderLayout();
                scrollToHeight = -getHeaderSize();
                selection = 0;
                scrollLvToEdge = Math.abs(mRefreshableView
                        .getFirstVisiblePosition() - selection) <= 1;
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
//                    if (mHeaderLoadingView.getGifDrawable() != null) {
//                        mHeaderLoadingView.getGifDrawable().stop();
//                    }
                    break;
            }
            /**
             * Scroll so the View is at the same Y as the ListView
             * header/footer, but only scroll if: we've pulled to refresh, it's
             * positioned correctly
             */
            if (scrollLvToEdge && getState() != State.MANUAL_REFRESHING) {
                if (getCurrentMode() == Mode.PULL_FROM_START) {
                    mRefreshableView.setSelectionFromTop(0, 0);
                    if (mRefreshableView.getIntItems() != null)
                        mRefreshableView.getIntItems().clear();
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
            final Mode mode = getMode();

            if (includeStart && mode.showHeaderLoadingLayout()) {
                proxy.addLayout(mHeaderLoadingView);
            }
            // if (includeEnd && mode.showFooterLoadingLayout()) {
            // proxy.addLayout(mFooterLoadingView);
            // }
        }

        return proxy;
    }

    protected MultiColumnListView createListView(Context context,
                                                 AttributeSet attrs) {
        final MultiColumnListView lv;
        if (VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD) {
            lv = new InternalListViewSDK9(context, attrs);
        } else {
            lv = new InternalListView(context, attrs);
        }
        return lv;
    }

    @Override
    protected MultiColumnListView createRefreshableView(Context context,
                                                        AttributeSet attrs) {
        MultiColumnListView lv = createListView(context, attrs);

        // Set it to this so it can be used in ListActivity/ListFragment
        lv.setId(android.R.id.list);
        return lv;
    }

    @Override
    protected void handleStyledAttributes(TypedArray a) {
        super.handleStyledAttributes(a);

        mListViewExtrasEnabled = a.getBoolean(
                R.styleable.PullToRefresh_ptrListViewExtrasEnabled, true);

        if (mListViewExtrasEnabled) {
            final FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER_HORIZONTAL);

            // Create Loading Views ready for use later
            FrameLayout frame = new FrameLayout(getContext());
            mHeaderLoadingView = createLoadingLayout(getContext(),
                    Mode.PULL_FROM_START, a);
            mHeaderLoadingView.setVisibility(View.GONE);
            frame.addView(mHeaderLoadingView, lp);
            mRefreshableView.addHeaderView(frame, null, false);

            mLvFooterLoadingFrame = new FrameLayout(getContext());

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
            OverscrollHelper.overScrollBy(PullToRefreshXListView.this, deltaX,
                    scrollX, deltaY, scrollY, isTouchEvent);

            return returnValue;
        }
    }

    protected class InternalListView extends MultiColumnListView implements
            EmptyViewMethodAccessor {

        private boolean mAddedLvFooter = false;

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
        public void setAdapter(ListAdapter adapter) {
            // Add the Footer View at the last possible moment
            if (null != mLvFooterLoadingFrame && !mAddedLvFooter) {
                addFooterView(mLvFooterLoadingFrame, null, false);
                mAddedLvFooter = true;
            }
            super.setAdapter(adapter);
        }

        @Override
        public void setEmptyView(View emptyView) {
            PullToRefreshXListView.this.setEmptyView(emptyView);
        }

        @Override
        public void setEmptyViewInternal(View emptyView) {
            super.setEmptyView(emptyView);
        }

    }

    public void addHeaderView(View v) {
        mRefreshableView.addHeaderView(v);
    }

    public void addHeaderView(View v, Object data, boolean isSelectable) {
        mRefreshableView.addHeaderView(v, data, isSelectable);
    }

    public void addFooterView(View v) {
        mRefreshableView.addFooterView(v);
    }

    public void setOnXListViewListener(IXListViewListener l) {
        if (l == null) {
            return;
        }
        mListViewListener = l;
        setOnRefreshListener(new OnRefreshListener2<MultiColumnListView>() {

            @Override
            public void onPullDownToRefresh(
                    final PullToRefreshBase<MultiColumnListView> refreshView) {
                // TODO Auto-generated method stub
                topAction();
            }

            @Override
            public void onPullUpToRefresh(
                    final PullToRefreshBase<MultiColumnListView> refreshViews) {
                // TODO Auto-generated method stub
                footAction();
            }
        });

        mXListViewFooter.setOnClickListener(new OnClickListener() {

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
     * 滑动到指定位置
     */
    public void setSelectionFromTop(int position, int y) {
        mRefreshableView.setSelectionFromTop(position, y);
    }

    /**
     * 是否有刷新加载
     */
    public void setPullRefreshLoadEnable(final boolean topLayout,
                                         final boolean footLayout, Mode mode) {
        this.topLayout = topLayout;
        this.footLayout = footLayout;
        setMode(mode);
        if (topLayout && !footLayout) {
            setRefreshType(RefreshType.down);
        } else if (!topLayout && footLayout) {
            setRefreshType(RefreshType.up);
        } else {
            setRefreshType(RefreshType.both);
        }
        getFooterLayout().hideAllViews();
        getFooterLayout().setProgressVisibity(false);
        getFooterLayout().setLoadingDrawable(
                getResources().getDrawable(R.drawable.transparent));
        getFooterLayout().setPullLabel("");
        getFooterLayout().setRefreshingLabel("");
        getFooterLayout().setReleaseLabel("");
//        getFooterLayout().setLoadingDrawableGif(false, R.drawable.updata_gif);

        if (!footLayout || mode == Mode.PULL_FROM_START
                || mode == Mode.DISABLED) {
            mXListViewFooter.setVisibility(View.GONE);
        } else {
            mXListViewFooter.setVisibility(View.VISIBLE);
        }
        if (!topLayout) {
            /** 不显示 */
            mHeaderLoadingView.hideAllViews();
            // mHeaderLoadingView.setLoadingDrawable(getResources().getDrawable(
            // R.drawable.transparent));
            mHeaderLoadingView.setPullLabel("");
            mHeaderLoadingView.setRefreshingLabel("");
            mHeaderLoadingView.setReleaseLabel("");
            mHeaderLoadingView.setProgressVisibity(false);
//            mHeaderLoadingView.setLoadingDrawableGif(false,
//                    R.drawable.updata_gif);
            getHeaderLayout().hideAllViews();
            getHeaderLayout().setProgressVisibity(false);
            // getHeaderLayout().setLoadingDrawable(
            // getResources().getDrawable(R.drawable.transparent));
            getHeaderLayout().setPullLabel("");
            getHeaderLayout().setRefreshingLabel("");
            getHeaderLayout().setReleaseLabel("");
//            getHeaderLayout().setLoadingDrawableGif(false,
//                    R.drawable.updata_gif);
        } else {
            /** 显示 */
            getHeaderLayout().showInvisibleViews();
            getHeaderLayout().setProgressVisibity(true);
//            getHeaderLayout().setLoadingDrawableGif(false,
//                    R.drawable.updata_gif);
            getHeaderLayout().setLoadingImageView(R.drawable.updata_image);
            mHeaderLoadingView.setProgressVisibity(true);
//            mHeaderLoadingView.setLoadingDrawableGif(true,
//                    R.drawable.updata_gif);
//            if (mHeaderLoadingView.getGifDrawable() != null) {
//                mHeaderLoadingView.getGifDrawable().stop();
//            }
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
                    if (mRefreshableView.getAdapter().getCount() > 20) {
                        setCurrentMode(Mode.PULL_FROM_END);
                        setRefreshing(false);
                    }
                }
            });
        }

    }

    public boolean isStackFromBottom() {
        // TODO Auto-generated method stub
        return mRefreshableView.isStackFromBottom();
    }

    public void setStackFromBottom(boolean b) {
        // TODO Auto-generated method stub
        mRefreshableView.setStackFromBottom(b);
    }

    /**
     * 滑动监听
     */
    public void setOnScrollListener(OnScrollListener l) {
        mOnScrollListener = l;
    }

    public Object getItemAtPosition(int position) {
        return mRefreshableView.getItemAtPosition(position);
    }

    /**
     * 列数
     */
    public void setColumnNum(int i) {
        mRefreshableView.setNum(i);
    }

    public void setColumnPadding(int mColumnPaddingLeft, int mColumnPaddingRight) {
        mRefreshableView.setColumnPadding(mColumnPaddingLeft,
                mColumnPaddingRight);
    }

    /**
     * 长按事件,先设置adapter:((MultiColumnListView) parent)
     * .setItemAdapterView(convertView, position);
     */
    public void setItemOnclickAndLongListener(
            XListViewItemLongListener mXListViewItemLongListener) {
        mRefreshableView
                .setItemOnclickAndLongListener(mXListViewItemLongListener);
    }
}
