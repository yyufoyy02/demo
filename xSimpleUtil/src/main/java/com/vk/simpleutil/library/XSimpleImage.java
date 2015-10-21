package com.vk.simpleutil.library;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.AbsListView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.vk.simpleutil.view.pulltorefresh.huewu.pla.lib.internal.PLA_AbsListView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图片工具类
 *
 * @author Administrator
 */
public class XSimpleImage extends ImageLoader {
    private static Context context;

    public static void init(Context contexts) {
        context = contexts.getApplicationContext();

    }

    /**
     * ImageLoader默认配置
     */
    public static void initDefaultImageLoaderConfig(Context contexts) {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                // 图片存本地
                // .showImageOnLoading(R.drawable.loading_bg)
                // .showImageForEmptyUri(R.drawable.loading_bg)
                // .showStubImage(R.drawable.loading_bg)
                // .showImageOnFail(R.drawable.loading_fail)
                .displayer(new FadeInBitmapDisplayer(300, true, false, false))
                .bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true)
                .cacheInMemory(true).resetViewBeforeLoading(true)
                .imageScaleType(ImageScaleType.EXACTLY) // default
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                contexts.getApplicationContext())
                .threadPoolSize(3)
                        // equal to default value
                .threadPriority(Thread.NORM_PRIORITY)
                .memoryCache(new WeakMemoryCache())
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024)
                .tasksProcessingOrder(QueueProcessingType.LIFO).diskCacheFileCount(100).
                        denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .defaultDisplayImageOptions(defaultOptions).build();
        XSimpleImage.getInstance().init(config);
    }

    /**
     * 图片压缩
     *
     * @param srcPath
     * @return
     */
    public static Bitmap compressImage(String srcPath) {
        return compressImage(getBitmapFromPath(srcPath, 480, 800));

    }

    public static void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == 1) {
            XSimpleImage.getInstance().pause();
        } else if (scrollState == 0) {
            XSimpleImage.getInstance().resume();
        }
    }

    public static void onScrollStateChanged(View view, int scrollState) {
        if (scrollState == 1) {
            XSimpleImage.getInstance().pause();
        } else if (scrollState == 0) {
            XSimpleImage.getInstance().resume();
        }
    }


    static void onScrollStateChanged(PLA_AbsListView view, int scrollState) {
        if (scrollState == 1) {
            XSimpleImage.getInstance().pause();
        } else if (scrollState == 0) {
            XSimpleImage.getInstance().resume();
        }
    }

    /**
     * 图片压缩
     *
     * @param image
     * @return
     */

    public static Bitmap compressImage(Bitmap image) {
//        Bitmap.CompressFormat mCompressFormat;
//        if (Build.VERSION.SDK_INT > 13) {
//            mCompressFormat=Bitmap.CompressFormat.WEBP;
//        }else{
//            mCompressFormat=Bitmap.CompressFormat.WEBP;
//        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100
                && options > 0) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            options -= 10;// 每次都减少10
            image.compress(CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中

        }
        XSimpleLogger.Log().e("options:" + options);
        Bitmap bitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(baos.toByteArray()), null, null);// 把ByteArrayInputStream数据生成图片

        if (!image.isRecycled()) {
            image.recycle(); // 回收图片所占的内存
            System.gc(); // 提醒系统及时回收
        }

        return bitmap;
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                    bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */

    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 将图片变为圆角
     *
     * @param bitmap 原Bitmap图片
     * @param pixels 图片圆角的弧度(单位:像素(px))
     * @return 带有圆角的图片(Bitmap 类型)
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_4444);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * 保存图片到文件夹
     *
     * @param bm
     * @param imageName       图片名称
     * @param pathSrc         文件夹 "/xxx/xx/"
     * @param mCompressFormat 图片类型
     * @param scannerImage    是否扫描显示在图册上
     * @return
     */

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static String saveToSDCard(Bitmap bm, String imageName,
                                      String pathSrc, Bitmap.CompressFormat mCompressFormat, boolean scannerImage) {
        if (bm == null)
            return null;
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            XSimpleToast.showToast("SD不可用");
            return null;
        }
        FileOutputStream fileOutputStream = null;
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + pathSrc);
        if (!file.exists()) {
            file.mkdirs();
        }
        String fileName;
        String mimeType;
        switch (mCompressFormat) {
            case PNG:
                fileName = imageName + ".png";
                mimeType = "image/png";
                break;
            case WEBP:
                fileName = imageName + ".webp";
                mimeType = "image/webp";
                break;
            case JPEG:
            default:
                fileName = imageName + ".jpg";
                mimeType = "image/jpeg";
                break;
        }
        String filePath = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + pathSrc + fileName;
        try {
            File f = new File(filePath);
            if (!f.exists()) {
                f.createNewFile();
                fileOutputStream = new FileOutputStream(filePath);
                bm.compress(mCompressFormat, 100, fileOutputStream);
                if (scannerImage) {
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.DATA, filePath);
                    values.put(MediaStore.Images.Media.MIME_TYPE, mimeType);
                    context.getContentResolver().insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            values);
                    context.sendBroadcast(new Intent(
                            Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri
                            .parse("file://" + filePath)));
                }
            }

        } catch (IllegalArgumentException e) {
            return null;
        } catch (IOException e) {
            return null;
        } catch (OutOfMemoryError e) {
            return null;
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                return null;
            }
        }

        return filePath;

    }

    /**
     * 通过文件路径获取到bitmap
     *
     * @param path
     * @param w
     * @param h
     * @return
     */
    public static Bitmap getBitmapFromPath(String path, int w, int h) {
        String src = null;
        if (path.indexOf("file://") != 0) {
            src = "file://" + path;
        } else {
            src = path;
        }
        return getInstance().loadImageSync(src, new ImageSize(w, h));
    }

    public static Bitmap getBitmapFromPath(String path) {
        String src = null;
        if (path.indexOf("file://") != 0) {
            src = "file://" + path;
        } else {
            src = path;
        }
        return getInstance().loadImageSync(src);
    }

    /**
     * 缩放/裁剪图片
     *
     * @param bm
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        if (bm == null)
            return null;
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
                true);
        return newbm;
    }

    /**
     * 通过路径生成Base64文件
     *
     * @param path
     * @return
     */
    public static String getBase64FromPath(String path) {
        String base64 = "";
        try {
            File file = new File(path);
            byte[] buffer = new byte[(int) file.length() + 100];
            @SuppressWarnings("resource")
            int length = new FileInputStream(file).read(buffer);
            base64 = Base64.encodeToString(buffer, 0, length, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return base64;
    }

    /**
     * 把bitmap转换成base64
     *
     * @param bitmap
     * @param bitmapQuality
     * @return
     */
    public static String getBase64FromBitmap(Bitmap bitmap, int bitmapQuality) {
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, bitmapQuality, bStream);
        byte[] bytes = bStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    public static String getBase64FromBitmap(Bitmap bitmap) {
        return getBase64FromBitmap(bitmap, 100);
    }

    /**
     * 把base64转换成bitmap
     *
     * @param string
     * @return
     */
    public static Bitmap getBitmapFromBase64(String string) {
        byte[] bitmapArray = null;
        try {
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BitmapFactory
                .decodeByteArray(bitmapArray, 0, bitmapArray.length);
    }

    /**
     * 获取资源图片
     *
     * @param id
     * @return
     */
    public static Bitmap getResourceToBitmap(int id) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
        opt.inPurgeable = true;
        opt.inSampleSize = 1;
        opt.inInputShareable = true;
        return BitmapFactory.decodeStream(XSimpleResources.openRawResource(id),
                null, opt);
    }
}