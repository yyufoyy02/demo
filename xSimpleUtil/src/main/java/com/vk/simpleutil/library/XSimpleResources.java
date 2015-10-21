package com.vk.simpleutil.library;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

import java.io.InputStream;

/**
 * 资源文件工具类
 *
 * @author Administrator
 */
public class XSimpleResources {
    private XSimpleResources() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    private static Context mContext;

    public static void init(Context context) {
        mContext = context.getApplicationContext();
    }

    public static int getColor(int id) {
        return mContext.getResources().getColor(id);
    }

    public static String getString(int id) {
        return mContext.getResources().getString(id);
    }

    public static String getString(int id, Object... formatArgs) {
        return mContext.getResources().getString(id, formatArgs);
    }

    public static Drawable getDrawable(int id) {
        return mContext.getResources().getDrawable(id);
    }

    public static ColorStateList getColorStateList(int id) {
        return mContext.getResources().getColorStateList(id);
    }

    public static int getDimensionPixelSize(int id) {
        return mContext.getResources().getDimensionPixelSize(id);
    }

    public static float getDimension(int id) {
        return mContext.getResources().getDimension(id);
    }

    public static DisplayMetrics getDisplayMetrics() {
        return mContext.getResources().getDisplayMetrics();
    }

    public static InputStream openRawResource(int id) {
        return mContext.getResources().openRawResource(id);
    }
}
