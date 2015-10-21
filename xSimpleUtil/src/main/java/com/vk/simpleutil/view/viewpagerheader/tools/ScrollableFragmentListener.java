package com.vk.simpleutil.view.viewpagerheader.tools;

public interface ScrollableFragmentListener {

    void onFragmentAttached(ScrollableListener fragment, int position);

    void onFragmentDetached(ScrollableListener fragment, int position);
}
