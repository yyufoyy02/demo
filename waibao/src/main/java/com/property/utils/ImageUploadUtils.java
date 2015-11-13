package com.property.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.ArrayMap;
import android.view.View;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.vk.simpleutil.http.XSimpleHttpUtil;
import com.vk.simpleutil.library.XSimpleExecutor;
import com.vk.simpleutil.library.XSimpleImage;

import java.util.Map;

public class ImageUploadUtils extends Handler implements ImageLoadingListener {
    public static final int HTTP_FAIL = -1;
    public static final int LOCAL_FAIL = -2;
    public static final int JSON_FAIL = -3;
    private ImageUpLoadListener mImageUpLoadListener;
    /**
     * 网络请求数据
     */
    private Map<String, String> mMap = new ArrayMap<>();
    private Map<String, UploadValues> uploadMap = new ArrayMap<>();
    private Context mContent;

    class UploadValues {
        public String usage;
        public int tag;

        public UploadValues(String usage, int tag) {
            this.usage = usage;
            this.tag = tag;
        }
    }

    private static class Holder {
        private static ImageUploadUtils instance = new ImageUploadUtils();
    }

    private ImageUploadUtils() {

    }

    public static ImageUploadUtils getInstance() {
        return Holder.instance;
    }

    public void initImageCode(Context mContent, String path, String usage, int tag, ImageUpLoadListener mImageUpLoadListener) {
        this.mContent = mContent;
        this.mImageUpLoadListener = mImageUpLoadListener;
        String src;
        if (path.indexOf("file://") != 0) {
            src = "file://" + path;
        } else {
            src = path;
        }

        uploadMap.put(src, new UploadValues(usage, tag));
        XSimpleImage.getInstance().loadImage(src, new ImageSize(480, 800), DisplayImageOptionsUtils.CompressOptions, ImageUploadUtils.this);
    }

    @Override
    public void onLoadingStarted(String s, View view) {

    }

    @Override
    public void onLoadingFailed(String s, View view, FailReason failReason) {

    }

    @Override
    public void onLoadingComplete(final String s, View view, final Bitmap bitmap) {
        if (bitmap == null) {
            if (mImageUpLoadListener != null)
                mImageUpLoadListener.imageUploadFail(LOCAL_FAIL, "找不到图片", uploadMap.containsKey(s) ? uploadMap.get(s).tag : 0);
            return;
        }
        XSimpleExecutor.getInstance().getFixedPool().execute(new Runnable() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                Bundle data = new Bundle();
                data.putString("base64", XSimpleImage.getBase64FromBitmap(bitmap));
                data.putString("url", s);
                msg.setData(data);
                ImageUploadUtils.this.sendMessage(msg);
            }
        });
    }

    @Override
    public void onLoadingCancelled(String s, View view) {

    }

    @Override
    public void handleMessage(Message msg) {
        Bundle data = msg.getData();
        if (data == null) {
            if (mImageUpLoadListener != null)
                mImageUpLoadListener.imageUploadFail(LOCAL_FAIL, "data数据错误", 0);
            return;
        }
        UploadValues mUploadValues = uploadMap.containsKey(data.get("url")) ?
                uploadMap.get(data.get("url")) : null;
        if (mUploadValues == null) {
            if (mImageUpLoadListener != null)
                mImageUpLoadListener.imageUploadFail(LOCAL_FAIL, "loadValues数据错误", 0);
            return;
        }
        if (mImageUpLoadListener != null)
            mImageUpLoadListener.imageUploadSuccess("data:image/png;base64," + data.get("base64"), mUploadValues.tag);
    }

    public void cancle(int tag) {
        XSimpleHttpUtil.getInstance().cancleRequest(mContent, tag);
    }

    public void onDestroy() {
        XSimpleHttpUtil.getInstance().cancleRequest(mContent);
        mContent = null;
        mImageUpLoadListener = null;
        mMap.clear();
        uploadMap.clear();
    }

    public interface ImageUpLoadListener {
        /**
         * 上传图片成功
         */
        void imageUploadSuccess(String base64, int tag);

        /**
         * 上传图片失败
         */
        void imageUploadFail(int statusFail, String error, int tag);

    }

}
