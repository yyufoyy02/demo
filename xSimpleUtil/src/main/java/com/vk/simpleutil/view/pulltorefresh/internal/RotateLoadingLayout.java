/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.vk.simpleutil.view.pulltorefresh.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.vk.simpleutil.library.R;
import com.vk.simpleutil.view.pulltorefresh.lib.PullToRefreshBase.Mode;
import com.vk.simpleutil.view.pulltorefresh.lib.PullToRefreshBase.Orientation;

public class RotateLoadingLayout extends LoadingLayout {

	static final int ROTATION_ANIMATION_DURATION = 1200;

	// private final Animation mRotateAnimation;
	// private final Matrix mHeaderImageMatrix;

	private float mRotationPivotX, mRotationPivotY;

	private final boolean mRotateDrawableWhilePulling;

	public RotateLoadingLayout(Context context, Mode mode,
			Orientation scrollDirection, TypedArray attrs) {
		super(context, mode, scrollDirection, attrs);

		mRotateDrawableWhilePulling = attrs.getBoolean(
				R.styleable.PullToRefresh_ptrRotateDrawableWhilePulling, true);

	}

	public void onLoadingDrawableSet(Drawable imageDrawable) {
		if (null != imageDrawable) {
			mRotationPivotX = Math
					.round(imageDrawable.getIntrinsicWidth() / 2f);
			mRotationPivotY = Math
					.round(imageDrawable.getIntrinsicHeight() / 2f);
		}
	}

	protected void onPullImpl(float scaleOfLayout) {
		float angle;
		if (mRotateDrawableWhilePulling) {
			angle = scaleOfLayout * 90f;
		} else {
			angle = Math.max(0f, Math.min(180f, scaleOfLayout * 360f - 180f));
		}

		// mHeaderImageMatrix.setRotate(angle, mRotationPivotX,
		// mRotationPivotY);
		// mHeaderImage.setImageMatrix(mHeaderImageMatrix);
		if (mHeaderProgress.getVisibility() != View.GONE) {
			mHeaderProgress.setVisibility(View.GONE);
		}
	}

	@Override
	protected void refreshingImpl() {
		// mHeaderImage.startAnimation(mRotateAnimation);
		if (progressVisibi) {
			mHeaderProgress.setVisibility(View.VISIBLE);
		}

	}

	@Override
	protected void resetImpl() {
		// mHeaderImage.clearAnimation();
		resetImageRotation();

	}

	private void resetImageRotation() {
		// if (null != mHeaderImageMatrix) {
		// mHeaderImageMatrix.reset();
		// mHeaderImage.setImageMatrix(mHeaderImageMatrix);
		// }
	}

	@Override
	protected void pullToRefreshImpl() {
		// NO-OP
	}

	@Override
	protected void releaseToRefreshImpl() {
		// NO-OP
	}

	@Override
	protected int getDefaultDrawableResId() {
		return R.drawable.default_ptr_rotate;
	}

}
