package com.vk.simpleutil.library;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class XSimplePhotoChoose {

    public enum PhotoChooseType {
        PHOTO_GRAPH, PHOTO_ALBUM
    }

    private static final int PHOTO_GRAPH_ACTIVITYFORRESULT = 1001;
    private static final int PHOTO_ALBUM_ACTIVITYFORRESULT = 1002;
    private static String mPhotoPath = null;
    private static String PHOTOACACHE = "PHOTOACACHE";

    public static void open(Activity mActivity, PhotoChooseType mPhotoChooseType) {
        Intent intent = new Intent();
        switch (mPhotoChooseType) {
            /** 拍照 */
            case PHOTO_GRAPH:
                mPhotoPath = null;
                XSimpleACache.get(mActivity).remove(PHOTOACACHE);
                File file = new File(Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + "/temps/");
                if (!file.exists()) {
                    file.mkdirs();
                }
                mPhotoPath = Environment.getExternalStorageDirectory()
                        .getAbsolutePath()
                        + "/temps/temp_"
                        + UUID.randomUUID().toString() + ".jpg";
                XSimpleACache.get(mActivity).put(PHOTOACACHE, mPhotoPath);
                File picture = new File(mPhotoPath);
                if (!picture.getParentFile().mkdirs()) {
                    picture.getParentFile().mkdirs();
                    try {
                        picture.getParentFile().createNewFile();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(picture));
                mActivity.startActivityForResult(intent,
                        PHOTO_GRAPH_ACTIVITYFORRESULT);
                break;
            /** 相册 */
            case PHOTO_ALBUM:
                intent.setAction(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        "image/*");
                mActivity.startActivityForResult(intent,
                        PHOTO_ALBUM_ACTIVITYFORRESULT);
                break;
        }

    }

    @SuppressWarnings("static-access")
    public static String onActivityResult(Activity mActivity, int requestCode,
                                          int resultCode, Intent data) {
        switch (requestCode) {
            case PHOTO_GRAPH_ACTIVITYFORRESULT:

                if (resultCode == mActivity.RESULT_OK) {
                    if (!Environment.getExternalStorageState().equals(
                            Environment.MEDIA_MOUNTED)) {
                        XSimpleToast.showToast(R.string.no_SD);
                        return null;
                    }
                    mPhotoPath = XSimpleACache.get(mActivity).getAsString(PHOTOACACHE);
                    if (mPhotoPath != null)
                        return mPhotoPath;
                    Uri uri = null;
                    if (data != null && data.getData() != null) {
                        uri = data.getData();
                    }
                    if (uri != null)
                        return uri.toString();
                    XSimpleToast.showToast(R.string.cenal_upload);
                } else {
                    XSimpleToast.showToast(R.string.no_photo);
                }
                break;
            case PHOTO_ALBUM_ACTIVITYFORRESULT:

                if (data == null) {
                    XSimpleToast.showToast(R.string.cenal_upload);
                    return null;
                }
                if (resultCode == mActivity.RESULT_OK) {
                    if (!Environment.getExternalStorageState().equals(
                            Environment.MEDIA_MOUNTED)) {
                        XSimpleToast.showToast(R.string.no_SD);
                        return null;
                    }
                    String[] proj = {MediaStore.Images.Media.DATA};
                    Cursor cursor = mActivity.getContentResolver().query(
                            data.getData(), proj, null, null, null);
                    String res = null;
                    if (cursor != null) {
                        if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                            int column_index = cursor
                                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                            res = cursor.getString(column_index);
                        }
                        cursor.close();
                        return res;
                    }

                } else {
                    XSimpleToast.showToast(R.string.no_photo);
                }
                break;
            default:
                break;
        }
        return null;
    }
}
