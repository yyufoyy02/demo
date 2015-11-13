package com.property.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.property.api.FaultApi;
import com.property.api.MaintenanceApi;
import com.property.base.BaseActivity;
import com.property.enumbase.MessageType;
import com.property.http.MySimpleJsonDataResponseCacheHandler;
import com.property.utils.ImageUploadUtils;
import com.vk.simpleutil.library.XSimpleAlertDialog;
import com.vk.simpleutil.library.XSimpleImage;
import com.vk.simpleutil.library.XSimplePhotoChoose;
import com.vk.simpleutil.library.XSimpleToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;
import butterknife.annotation.event.OnClick;

public class DetailEditActivity extends BaseActivity implements ImageUploadUtils.ImageUpLoadListener {
    @InjectView(R.id.iv_detailedit_photo)
    ImageView ivDetaileditPhoto;
    @InjectView(R.id.iv_detailedit_imageView1)
    ImageView ivDetaileditImageView1;
    @InjectView(R.id.iv_detailedit_imageView2)
    ImageView ivDetaileditImageView2;
    @InjectView(R.id.iv_detailedit_imageView3)
    ImageView ivDetaileditImageView3;
    List<String> list = new ArrayList<>();
    List<String> base64List = new ArrayList<>();
    MessageType messageType;
    String id;

    @Override
    public int onCreateViewLayouId() {
        return R.layout.detailedit_activity;
    }

    @Override
    public void initAllData() {
        setTitle("确认");
        messageType = (MessageType) getIntent().getSerializableExtra("messageType");
        id = getIntent().getStringExtra("id");

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
            ImageUploadUtils.getInstance().initImageCode(mContext, list.get(i), "", i, this);
        }
    }

    @OnClick(R.id.tv_detailedit_submit)
    void submit(View view) {
        showProgressDialog(mContext);
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < base64List.size(); i++) {
            map.put("img[" + i + "]", base64List.get(i));
        }
        MySimpleJsonDataResponseCacheHandler mySimpleJsonDataResponseCacheHandler = new MySimpleJsonDataResponseCacheHandler(new MySimpleJsonDataResponseCacheHandler.OnJsonCallBack() {
            @Override
            public void success() {
                if (messageType == MessageType.repair) {
                    startActivity(new Intent(mContext, DetailEditCompleteActivity.class).putExtra("id", id));
                }
                dismissProgressDialog();
                finish();
            }

            @Override
            public void fail() {
                dismissProgressDialog();
            }
        });
        if (messageType == MessageType.repair) {
            FaultApi.getInstance().putBeg(mContext, id, map, mySimpleJsonDataResponseCacheHandler);
        } else {
            MaintenanceApi.getInstance().putEng(mContext, id, map, mySimpleJsonDataResponseCacheHandler);
        }
    }

    @Override
    public void imageUploadSuccess(String base64, int tag) {
        if (tag < base64List.size()) {
            base64List.set(tag, base64);
        } else {
            base64List.add(base64);
        }
    }

    @Override
    public void imageUploadFail(int statusFail, String error, int tag) {
        XSimpleToast.showToast("上传失败");
    }
}
