package com.vk.simpleutil.view.viewpagerheader.tools;

import android.view.MotionEvent;

public interface ScrollableListener {
    enum ScrollableType {
        up, down
    }

    boolean isViewBeingDragged(MotionEvent event);

    ScrollableType scrollableType();
}
