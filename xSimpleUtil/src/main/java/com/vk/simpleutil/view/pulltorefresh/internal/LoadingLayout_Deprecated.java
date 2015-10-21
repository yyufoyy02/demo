///*******************************************************************************
// * Copyright 2011, 2012 Chris Banes.
// * <p/>
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// * <p/>
// * http://www.apache.org/licenses/LICENSE-2.0
// * <p/>
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// *******************************************************************************/
//package com.vk.simpleutil.view.pulltorefresh.internal;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.content.res.ColorStateList;
//import android.content.res.TypedArray;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Typeface;
//import android.graphics.drawable.AnimationDrawable;
//import android.graphics.drawable.Drawable;
//import android.text.TextUtils;
//import android.util.TypedValue;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.animation.Interpolator;
//import android.view.animation.LinearInterpolator;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
//import com.vk.simpleutil.library.R;
//import com.vk.simpleutil.view.pulltorefresh.lib.ILoadingLayout;
//import com.vk.simpleutil.view.pulltorefresh.lib.PullToRefreshBase.Mode;
//import com.vk.simpleutil.view.pulltorefresh.lib.PullToRefreshBase.Orientation;
//
//import java.io.InputStream;
//
//import pl.droidsonroids.gif.GifDrawable;
//import pl.droidsonroids.gif.GifImageView;
//
//@SuppressLint("ViewConstructor")
//public abstract class LoadingLayout_Deprecated extends FrameLayout implements
//        ILoadingLayout {
//
//    static final String LOG_TAG = "PullToRefresh-LoadingLayout";
//
//    static final Interpolator ANIMATION_INTERPOLATOR = new LinearInterpolator();
//
//    private FrameLayout mInnerLayout;
//    public boolean progressVisibi = false;
//    protected final ImageView mHeaderImage;
//    protected final ProgressBar mHeaderProgress;
//    protected final GifImageView mGifView;
//    protected boolean bGif = true;
//    private GifDrawable mGifDrawable;
//    private boolean mUseIntrinsicAnimation;
//
//    private final TextView mHeaderText;
//    private final TextView mSubHeaderText;
//
//    protected final Mode mMode;
//    protected final Orientation mScrollDirection;
//
//    private CharSequence mPullLabel;
//    private CharSequence mRefreshingLabel;
//    private CharSequence mReleaseLabel;
//
//    public LoadingLayout_Deprecated(Context context, final Mode mode,
//                                    final Orientation scrollDirection, TypedArray attrs) {
//        super(context);
//        mMode = mode;
//        mScrollDirection = scrollDirection;
//
//        switch (scrollDirection) {
//            case HORIZONTAL:
//                LayoutInflater.from(context).inflate(
//                        R.layout.pull_to_refresh_header_horizontal, this);
//                break;
//            case VERTICAL:
//            default:
//                LayoutInflater.from(context).inflate(
//                        R.layout.pull_to_refresh_header_vertical, this);
//                break;
//        }
//
//        mInnerLayout = (FrameLayout) findViewById(R.id.fl_inner);
//        mHeaderText = (TextView) mInnerLayout
//                .findViewById(R.id.pull_to_refresh_text);
//        mHeaderProgress = (ProgressBar) mInnerLayout
//                .findViewById(R.id.pull_to_refresh_progress);
//        mSubHeaderText = (TextView) mInnerLayout
//                .findViewById(R.id.pull_to_refresh_sub_text);
//        mHeaderImage = (ImageView) mInnerLayout
//                .findViewById(R.id.pull_to_refresh_image);
//        mGifView = (GifImageView) mInnerLayout
//                .findViewById(R.id.pull_to_refresh_gif);
//        mGifDrawable = ((GifDrawable) mGifView.getDrawable());
//        mGifView.setVisibility(View.GONE);
//        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mInnerLayout
//                .getLayoutParams();
//
//        switch (mode) {
//            case PULL_FROM_END:
//                lp.gravity = scrollDirection == Orientation.VERTICAL ? Gravity.TOP
//                        : Gravity.LEFT;
//
//                // Load in labels
//                mPullLabel = context.getString(R.string.footer_Refresh_stata);
//                mRefreshingLabel = context
//                        .getString(R.string.footer_Refresh_before);
//                mReleaseLabel = context.getString(R.string.footer_Refresh_stata);
//                break;
//
//            case PULL_FROM_START:
//            default:
//                lp.gravity = scrollDirection == Orientation.VERTICAL ? Gravity.BOTTOM
//                        : Gravity.RIGHT;
//
//                // Load in labels
//                mPullLabel = context.getString(R.string.ptr_pull_to_refresh);
//                mRefreshingLabel = context.getString(R.string.ptr_loading);
//                mReleaseLabel = context.getString(R.string.ptr_release_to_refresh);
//                break;
//        }
//
//        if (attrs.hasValue(R.styleable.PullToRefresh_ptrHeaderBackground)) {
//            Drawable background = attrs
//                    .getDrawable(R.styleable.PullToRefresh_ptrHeaderBackground);
//            if (null != background) {
//                ViewCompat.setBackground(this, background);
//            }
//        }
//
//        if (attrs.hasValue(R.styleable.PullToRefresh_ptrHeaderTextAppearance)) {
//            TypedValue styleID = new TypedValue();
//            attrs.getValue(R.styleable.PullToRefresh_ptrHeaderTextAppearance,
//                    styleID);
//            setTextAppearance(styleID.data);
//        }
//        if (attrs
//                .hasValue(R.styleable.PullToRefresh_ptrSubHeaderTextAppearance)) {
//            TypedValue styleID = new TypedValue();
//            attrs.getValue(
//                    R.styleable.PullToRefresh_ptrSubHeaderTextAppearance,
//                    styleID);
//            setSubTextAppearance(styleID.data);
//        }
//
//        // Text Color attrs need to be set after TextAppearance attrs
//        if (attrs.hasValue(R.styleable.PullToRefresh_ptrHeaderTextColor)) {
//            ColorStateList colors = attrs
//                    .getColorStateList(R.styleable.PullToRefresh_ptrHeaderTextColor);
//            if (null != colors) {
//                setTextColor(colors);
//            }
//        }
//        if (attrs.hasValue(R.styleable.PullToRefresh_ptrHeaderSubTextColor)) {
//            ColorStateList colors = attrs
//                    .getColorStateList(R.styleable.PullToRefresh_ptrHeaderSubTextColor);
//            if (null != colors) {
//                setSubTextColor(colors);
//            }
//        }
//
//        // Try and get defined drawable from Attrs
//        Drawable imageDrawable = null;
//        if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawable)) {
//            imageDrawable = attrs
//                    .getDrawable(R.styleable.PullToRefresh_ptrDrawable);
//        }
//
//        // Check Specific Drawable from Attrs, these overrite the generic
//        // drawable attr above
//        switch (mode) {
//            case PULL_FROM_START:
//            default:
//                if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawableStart)) {
//                    imageDrawable = attrs
//                            .getDrawable(R.styleable.PullToRefresh_ptrDrawableStart);
//                } else if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawableTop)) {
//                    Utils.warnDeprecation("ptrDrawableTop", "ptrDrawableStart");
//                    imageDrawable = attrs
//                            .getDrawable(R.styleable.PullToRefresh_ptrDrawableTop);
//                }
//                break;
//
//            case PULL_FROM_END:
//                if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawableEnd)) {
//                    imageDrawable = attrs
//                            .getDrawable(R.styleable.PullToRefresh_ptrDrawableEnd);
//                } else if (attrs
//                        .hasValue(R.styleable.PullToRefresh_ptrDrawableBottom)) {
//                    Utils.warnDeprecation("ptrDrawableBottom", "ptrDrawableEnd");
//                    imageDrawable = attrs
//                            .getDrawable(R.styleable.PullToRefresh_ptrDrawableBottom);
//                }
//                break;
//        }
//
//        // // If we don't have a user defined drawable, load the default
//        // if (null == imageDrawable) {
//        // imageDrawable = context.getResources().getDrawable(
//        // getDefaultDrawableResId());
//        // }
//        //
//        // // Set Drawable, and save width/height
//        setLoadingDrawable(imageDrawable);
//
//        reset();
//    }
//
//    public void setProgressVisibity(boolean b) {
//        progressVisibi = b;
//    }
//
//    public void setLoadingImageView(int drawable) {
//        if (drawable != 0) {
//            mHeaderImage.setImageBitmap(getResourceToBitmap(drawable));
//        }
//    }
//
//    private Bitmap getResourceToBitmap(int id) {
//        BitmapFactory.Options opt = new BitmapFactory.Options();
//        opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
//        opt.inPurgeable = true;
//        opt.inSampleSize = 1;
//        opt.inInputShareable = true;
//        // 获取资源图片
//        InputStream is = getResources().openRawResource(id);
//        return BitmapFactory.decodeStream(is, null, opt);
//    }
//
//    /**
//     * gif显示
//     */
//    public void setLoadingDrawableGif(boolean visibity, int drawable) {
//        this.bGif = visibity;
//
//        if (visibity) {
//            mGifView.setVisibility(View.VISIBLE);
//            if (drawable != 0) {
//                if (mGifDrawable == null) {
//                    mGifView.setImageResource(drawable);
//
//                }
//                mGifDrawable = (GifDrawable) mGifView.getDrawable();
//            }
//        } else {
//            mGifView.setVisibility(View.GONE);
//            if (mGifDrawable != null) {
//                mGifDrawable.stop();
//            }
//        }
//    }
//
//    public GifDrawable getGifDrawable() {
//        return mGifDrawable;
//
//    }
//
//    public final void setHeight(int height) {
//        ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) getLayoutParams();
//        lp.height = height;
//        requestLayout();
//    }
//
//    public final void setWidth(int width) {
//        ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) getLayoutParams();
//        lp.width = width;
//        requestLayout();
//    }
//
//    public final int getContentSize() {
//        switch (mScrollDirection) {
//            case HORIZONTAL:
//                return mInnerLayout.getWidth();
//            case VERTICAL:
//            default:
//                return mInnerLayout.getHeight();
//        }
//    }
//
//    public final void hideAllViews() {
//        if (View.VISIBLE == mHeaderText.getVisibility()) {
//            mHeaderText.setVisibility(View.INVISIBLE);
//        }
//        if (View.VISIBLE == mHeaderProgress.getVisibility()) {
//            mHeaderProgress.setVisibility(View.GONE);
//        }
//        if (View.VISIBLE == mGifView.getVisibility()) {
//            mGifView.setVisibility(View.GONE);
//            if (mGifDrawable != null) {
//                mGifDrawable.stop();
//            }
//        }
//
//        if (View.VISIBLE == mHeaderImage.getVisibility()) {
//            mHeaderImage.setVisibility(View.INVISIBLE);
//        }
//        if (View.VISIBLE == mSubHeaderText.getVisibility()) {
//            mSubHeaderText.setVisibility(View.INVISIBLE);
//        }
//    }
//
//    public final void onPull(float scaleOfLayout) {
//        if (!mUseIntrinsicAnimation) {
//            onPullImpl(scaleOfLayout);
//        }
//    }
//
//    public final void pullToRefresh() {
//        if (null != mHeaderText) {
//            mHeaderText.setText(mPullLabel);
//        }
//
//        // Now call the callback
//        pullToRefreshImpl();
//    }
//
//    public final void refreshing() {
//        if (null != mHeaderText) {
//            mHeaderText.setText(mRefreshingLabel);
//        }
//
//        if (mUseIntrinsicAnimation) {
//            // ((AnimationDrawable) mHeaderImage.getDrawable()).start();
//        } else {
//            // Now call the callback
//            refreshingImpl();
//        }
//
//        if (null != mSubHeaderText) {
//            mSubHeaderText.setVisibility(View.GONE);
//        }
//    }
//
//    public final void releaseToRefresh() {
//        if (null != mHeaderText) {
//            mHeaderText.setText(mReleaseLabel);
//        }
//
//        // Now call the callback
//        releaseToRefreshImpl();
//    }
//
//    public final void reset() {
//        if (null != mHeaderText) {
//            mHeaderText.setText(mPullLabel);
//        }
//        // mHeaderImage.setVisibility(View.VISIBLE);
//
//        if (mUseIntrinsicAnimation) {
//            // ((AnimationDrawable) mHeaderImage.getDrawable()).stop();
//        } else {
//            // Now call the callback
//            resetImpl();
//        }
//
//        if (null != mSubHeaderText) {
//            if (TextUtils.isEmpty(mSubHeaderText.getText())) {
//                mSubHeaderText.setVisibility(View.GONE);
//            } else {
//                mSubHeaderText.setVisibility(View.VISIBLE);
//            }
//        }
//    }
//
//    @Override
//    public void setLastUpdatedLabel(CharSequence label) {
//        setSubHeaderText(label);
//    }
//
//    public final void setLoadingDrawable(Drawable imageDrawable) {
//        // Set Drawable
//        // mHeaderImage.setImageDrawable(imageDrawable);
//        mUseIntrinsicAnimation = (imageDrawable instanceof AnimationDrawable);
//
//        // Now call the callback
//        onLoadingDrawableSet(imageDrawable);
//    }
//
//    public void setPullLabel(CharSequence pullLabel) {
//        mPullLabel = pullLabel;
//    }
//
//    public void setRefreshingLabel(CharSequence refreshingLabel) {
//        mRefreshingLabel = refreshingLabel;
//    }
//
//    public void setReleaseLabel(CharSequence releaseLabel) {
//        mReleaseLabel = releaseLabel;
//    }
//
//    @Override
//    public void setTextTypeface(Typeface tf) {
//        mHeaderText.setTypeface(tf);
//    }
//
//    public final void showInvisibleViews() {
//        if (View.INVISIBLE == mHeaderText.getVisibility()) {
//            mHeaderText.setVisibility(View.VISIBLE);
//        }
//        if (View.GONE == mHeaderProgress.getVisibility()) {
//            mHeaderProgress.setVisibility(View.VISIBLE);
//        }
//        if (View.GONE == mGifView.getVisibility() && bGif) {
//            mGifView.setVisibility(View.VISIBLE);
//            if (mGifDrawable != null) {
//                mGifDrawable.start();
//            }
//        }
//        if (View.INVISIBLE == mHeaderImage.getVisibility()) {
//            mHeaderImage.setVisibility(View.VISIBLE);
//        }
//        if (View.INVISIBLE == mSubHeaderText.getVisibility()) {
//            mSubHeaderText.setVisibility(View.VISIBLE);
//        }
//    }
//
//    /**
//     * Callbacks for derivative Layouts
//     */
//
//    protected abstract int getDefaultDrawableResId();
//
//    protected abstract void onLoadingDrawableSet(Drawable imageDrawable);
//
//    protected abstract void onPullImpl(float scaleOfLayout);
//
//    protected abstract void pullToRefreshImpl();
//
//    protected abstract void refreshingImpl();
//
//    protected abstract void releaseToRefreshImpl();
//
//    protected abstract void resetImpl();
//
//    private void setSubHeaderText(CharSequence label) {
//        if (null != mSubHeaderText) {
//            if (TextUtils.isEmpty(label)) {
//                mSubHeaderText.setVisibility(View.GONE);
//            } else {
//                mSubHeaderText.setText(label);
//
//                // Only set it to Visible if we're GONE, otherwise VISIBLE will
//                // be set soon
//                if (View.GONE == mSubHeaderText.getVisibility()) {
//                    mSubHeaderText.setVisibility(View.VISIBLE);
//                }
//            }
//        }
//    }
//
//    private void setSubTextAppearance(int value) {
//        if (null != mSubHeaderText) {
//            mSubHeaderText.setTextAppearance(getContext(), value);
//        }
//    }
//
//    private void setSubTextColor(ColorStateList color) {
//        if (null != mSubHeaderText) {
//            mSubHeaderText.setTextColor(color);
//        }
//    }
//
//    private void setTextAppearance(int value) {
//        if (null != mHeaderText) {
//            mHeaderText.setTextAppearance(getContext(), value);
//        }
//        if (null != mSubHeaderText) {
//            mSubHeaderText.setTextAppearance(getContext(), value);
//        }
//    }
//
//    private void setTextColor(ColorStateList color) {
//        if (null != mHeaderText) {
//            mHeaderText.setTextColor(color);
//        }
//        if (null != mSubHeaderText) {
//            mSubHeaderText.setTextColor(color);
//        }
//    }
//
//}
