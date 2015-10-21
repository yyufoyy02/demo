package com.vk.simpleutil.library;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 打开或关闭软键盘工具类
 *
 * @author Administrator
 */
public class XSimpleKeyBoard {
    private XSimpleKeyBoard() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 打开软键盘
     */
    public static void openKeybord(final EditText mEditText) {
        mEditText.setFocusable(true);
        mEditText.setFocusableInTouchMode(true);
        mEditText.requestFocus();
        final boolean isFocus = mEditText.isFocused();
        (new Handler()).postDelayed(new Runnable() {
            public void run() {
                InputMethodManager imm = (InputMethodManager) mEditText
                        .getContext().getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                if (isFocus) {
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                } else {
                    imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
                }
            }
        }, 200);
    }

    /**
     * 关闭软键盘
     */
    public static void closeKeybord(Context mContext) {
        if (mContext == null)
            return;
        try {
            ((InputMethodManager) mContext
                    .getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(((Activity) mContext)
                                    .getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (NullPointerException e) {
            return;
        }

    }

    public static boolean isOpenKeybord(EditText mEditText) {
        InputMethodManager imm = (InputMethodManager) mEditText.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        return imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    /**
     * 监听软键盘状态    * @param activity    * @param listener
     */
    static boolean sLastVisiable = false;

    public static void addOnSoftKeyBoardVisibleListener(Activity activity, final OnSoftKeyBoardVisibleListener listener, boolean sLastVisiables) {
        final View decorView = activity.getWindow().getDecorView();
        sLastVisiable = sLastVisiables;
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                decorView.getWindowVisibleDisplayFrame(rect);
                int displayHight = rect.bottom - rect.top;
                int hight = decorView.getHeight();
                boolean visible = (double) displayHight / hight < 0.8;

                if (visible != sLastVisiable) {
                    listener.onSoftKeyBoardVisible(visible);
                }
                sLastVisiable = visible;
            }
        });
    }

    public interface OnSoftKeyBoardVisibleListener {
        void onSoftKeyBoardVisible(boolean visible);
    }
}