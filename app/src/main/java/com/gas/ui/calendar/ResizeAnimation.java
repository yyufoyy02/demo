package com.property.ui.calendar;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.property.BaseApplication;
import com.property.utils.Utils;

public class ResizeAnimation extends Animation {
    final int startHeight;
    final int targetHeight;
    final int defHeight;
    View view;

    public ResizeAnimation(View view, int targetHeight) {
        this.view = view;
        this.targetHeight = targetHeight;
        startHeight = view.getHeight();
        defHeight = Utils.dipToPx(BaseApplication.mContext,275);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        int newHeight = (int) (startHeight + ((targetHeight<defHeight?defHeight:targetHeight) - startHeight) * interpolatedTime);
        view.getLayoutParams().height = newHeight;
        view.requestLayout();
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}
