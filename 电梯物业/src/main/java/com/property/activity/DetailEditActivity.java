package com.property.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.property.base.BaseActivity;
import com.vk.simpleutil.library.XSimpleAlertDialog;
import com.vk.simpleutil.library.XSimpleImage;
import com.vk.simpleutil.library.XSimplePhotoChoose;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.annotation.event.OnClick;

public class DetailEditActivity extends BaseActivity {
    @InjectView(R.id.iv_detailedit_photo)
    ImageView ivDetaileditPhoto;
    @InjectView(R.id.iv_detailedit_imageView1)
    ImageView ivDetaileditImageView1;
    @InjectView(R.id.iv_detailedit_imageView2)
    ImageView ivDetaileditImageView2;
    @InjectView(R.id.iv_detailedit_imageView3)
    ImageView ivDetaileditImageView3;
    List<String> list = new ArrayList<>();

    @Override
    public int onCreateViewLayouId() {
        return R.layout.detailedit_activity;
    }

    @Override
    public void initAllData() {
        setTitle("确认");
    }

    @Override
    public void initListener() {

    }

    @OnClick(R.id.iv_detailedit_photo)
    void photo(View view) {
        XSimpleAlertDialog.simpleBaseDialog(mContext, null, "拍照", new XSimpleAlertDialog.Callback() {
            @Override
            public void getCallback() {
                XSimplePhotoChoose.open(mActivity, XSimplePhotoChoose.PhotoChooseType.PHOTO_GRAPH);
            }
        }, "相册", new XSimpleAlertDialog.Callback() {
            @Override
            public void getCallback() {
                XSimplePhotoChoose.open(mActivity, XSimplePhotoChoose.PhotoChooseType.PHOTO_ALBUM);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (list.size() == 3)
            return;
        list.add("file://" + XSimplePhotoChoose.onActivityResult(mActivity, requestCode, resultCode, data));
        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                XSimpleImage.getInstance().displayImage(list.get(i), ivDetaileditImageView1);
            } else if (i == 1) {
                XSimpleImage.getInstance().displayImage(list.get(i), ivDetaileditImageView2);
            } else if (i == 2) {
                XSimpleImage.getInstance().displayImage(list.get(i), ivDetaileditImageView3);
            }
        }
    }

    @OnClick(R.id.tv_detailedit_submit)
    void submit(View view) {

    }

}
