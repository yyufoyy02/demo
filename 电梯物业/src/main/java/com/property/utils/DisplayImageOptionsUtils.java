package com.property.utils;

import android.graphics.Bitmap;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;
import com.property.activity.R;
import com.vk.simpleutil.library.XSimpleImage;
import com.vk.simpleutil.library.XSimpleResources;

/**
 * Created by Administrator on 2015/6/25.
 */
public class DisplayImageOptionsUtils {
    public static final DisplayImageOptions RoundedOptions = new DisplayImageOptions.Builder()
//            .showImageOnLoading(R.drawable.loading_bg)
//            .showImageForEmptyUri(R.drawable.loading_bg)
//            .showImageOnFail(R.drawable.loading_bg)
            .displayer(new RoundedBitmapDisplayer(XSimpleResources.getDimensionPixelSize(R.dimen.dp5)))
            .bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true)
            .cacheInMemory(true).resetViewBeforeLoading(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .build();
    public static final DisplayImageOptions RoundedOptions3dp = new DisplayImageOptions.Builder()
//            .showImageOnLoading(R.drawable.loading_bg)
//            .showImageForEmptyUri(R.drawable.loading_bg)
//            .showImageOnFail(R.drawable.loading_bg)
            .displayer(new RoundedBitmapDisplayer(XSimpleResources.getDimensionPixelSize(R.dimen.dp3)))
            .bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true)
            .cacheInMemory(true).resetViewBeforeLoading(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .build();

    public static final DisplayImageOptions CompressOptions = new DisplayImageOptions.Builder()
            .cacheOnDisk(false).cacheInMemory(false).resetViewBeforeLoading(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .postProcessor(new BitmapProcessor() {
                @Override
                public Bitmap process(Bitmap bitmap) {
                    return XSimpleImage.compressImage(bitmap);
                }
            })
            .build();
    public static final DisplayImageOptions ImageBackGroundOptions = new DisplayImageOptions.Builder()
//            .showImageOnLoading(R.drawable.loading_bg)
//            .showImageForEmptyUri(R.drawable.loading_bg)
//            .showImageOnFail(R.drawable.loading_bg)
            .displayer(new FadeInBitmapDisplayer(300, true, false, false))
            .bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true)
            .cacheInMemory(true).resetViewBeforeLoading(true)
            .imageScaleType(ImageScaleType.EXACTLY) // default
            .build();
}
