package com.vk.simpleutil.util;

import android.view.View;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by Administrator on 2015/7/21.
 */
public abstract class XSimpleImageLoadingListener implements ImageLoadingListener {
    @Override
    public void onLoadingStarted(String imageUri, View view) {
        // Empty implementation
    }

    @Override
    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
        // Empty implementation
    }


    @Override
    public void onLoadingCancelled(String imageUri, View view) {
        // Empty implementation
    }
}
