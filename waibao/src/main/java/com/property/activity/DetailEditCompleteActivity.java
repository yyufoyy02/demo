package com.property.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.property.ActivityForResult;
import com.property.api.FaultApi;
import com.property.base.BaseActivity;
import com.property.http.MySimpleJsonDataResponseCacheHandler;
import com.property.utils.ImageUploadUtils;
import com.vk.simpleutil.library.XSimpleAlertDialog;
import com.vk.simpleutil.library.XSimpleImage;
import com.vk.simpleutil.library.XSimpleLogger;
import com.vk.simpleutil.library.XSimplePhotoChoose;
import com.vk.simpleutil.library.XSimpleToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.annotation.event.OnClick;

/**
 * Created by Administrator on 2015/11/7.
 */
public class DetailEditCompleteActivity extends BaseActivity implements ImageUploadUtils.ImageUpLoadListener {
    @InjectView(R.id.edt_detail_say)
    EditText edtDetailSay;
    @InjectView(R.id.tv_complete_yes)
    TextView tvCompleteYes;
    @InjectView(R.id.tv_complete_no)
    TextView tvCompleteNo;
    @InjectView(R.id.edt_complete_name)
    EditText edtCompleteName;
    @InjectView(R.id.edt_complete_price)
    EditText edtCompletePrice;
    String id;
    @InjectView(R.id.iv_detailedit_fault_imageView1)
    ImageView ivDetaileditFaultImageView1;
    @InjectView(R.id.iv_detailedit_fault_imageView2)
    ImageView ivDetaileditFaultImageView2;
    @InjectView(R.id.iv_detailedit_fault_imageView3)
    ImageView ivDetaileditFaultImageView3;
    @InjectView(R.id.iv_detailedit_complete_imageView1)
    ImageView ivDetaileditCompleteImageView1;
    @InjectView(R.id.iv_detailedit_complete_imageView2)
    ImageView ivDetaileditCompleteImageView2;
    @InjectView(R.id.iv_detailedit_complete_imageView3)
    ImageView ivDetaileditCompleteImageView3;
    List<String> list1 = new ArrayList<>();
    List<String> list2 = new ArrayList<>();
    List<String> codeList1 = new ArrayList<>();
    List<String> codeList2 = new ArrayList<>();
    int imageType = 0;
    static final int AFTER = 1;
    static final int BEFORE = 2;


    @Override
    public int onCreateViewLayouId() {
        return R.layout.complete_activity;
    }

    @Override
    public void initAllData() {
        setTitle("确认完成");
        id = getIntent().getStringExtra("id");
        tvCompleteNo.setSelected(true);
    }

    @Override
    public void initListener() {

    }

    void complete() {
        showProgressDialog(mContext);
        MySimpleJsonDataResponseCacheHandler mySimpleJsonDataResponseCacheHandler = new MySimpleJsonDataResponseCacheHandler(new MySimpleJsonDataResponseCacheHandler.OnJsonCallBack() {
            @Override
            public void success() {
                XSimpleToast.showToast("上传成功");
                dismissProgressDialog();
                ActivityForResult.MaintenanceListRefresh = true;
                ActivityForResult.FaultListRefresh = true;
                finish();
            }

            @Override
            public void fail() {
                dismissProgressDialog();
            }
        });
        int fault_parts = 0;
        if (tvCompleteYes.isSelected()) {
            fault_parts = 1;
        }
        FaultApi.getInstance().putDeal(mContext, id, edtDetailSay.getText().toString(), fault_parts,
                edtCompleteName.getText().toString(), edtCompletePrice.getText().toString(), mySimpleJsonDataResponseCacheHandler);
    }

    @OnClick({R.id.tv_complete_no, R.id.tv_complete_yes, R.id.iv_detailedit_photo, R.id.iv_detailedit_complete_photo,
            R.id.tv_complete_submit, R.id.edt_detail_say})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_complete_yes:
                XSimpleLogger.Log().e("tv_complete_yes");
                tvCompleteYes.setSelected(true);
                tvCompleteNo.setSelected(false);
                break;
            case R.id.tv_complete_no:
                tvCompleteYes.setSelected(false);
                tvCompleteNo.setSelected(true);
                break;
            case R.id.iv_detailedit_photo:
                pohoto(AFTER);
                break;
            case R.id.iv_detailedit_complete_photo:
                pohoto(BEFORE);
                break;
            case R.id.tv_complete_submit:
                complete();
                break;
            case R.id.edt_detail_say:
                startActivityForResult(new Intent(mContext, CommonLanguageActivity.class), ActivityForResult.REASONFORRESULT);
                break;
        }
    }

    void pohoto(int type) {
        imageType = type;
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
        if (requestCode == ActivityForResult.REASONFORRESULT) {
            if (resultCode == RESULT_OK && data != null) {
                XSimpleLogger.Log().e(data.getStringExtra("reason"));
            }
        } else {
            if (imageType == AFTER) {
                if (list1.size() == 3)
                    return;
                list1.add("file://" + XSimplePhotoChoose.onActivityResult(mActivity, requestCode, resultCode, data));
                for (int i = 0; i < list1.size(); i++) {
                    if (i == 0) {
                        XSimpleImage.getInstance().displayImage(list1.get(i), ivDetaileditFaultImageView1);
                    } else if (i == 1) {
                        XSimpleImage.getInstance().displayImage(list1.get(i), ivDetaileditFaultImageView2);
                    } else if (i == 2) {
                        XSimpleImage.getInstance().displayImage(list1.get(i), ivDetaileditFaultImageView3);
                    }
                    ImageUploadUtils.getInstance().initImageCode(mContext, list1.get(i), "", i, this);
                }
            } else if (imageType == BEFORE) {
                if (list2.size() == 3)
                    return;
                list2.add("file://" + XSimplePhotoChoose.onActivityResult(mActivity, requestCode, resultCode, data));
                for (int i = 0; i < list2.size(); i++) {
                    if (i == 0) {
                        XSimpleImage.getInstance().displayImage(list2.get(i), ivDetaileditCompleteImageView1);
                    } else if (i == 1) {
                        XSimpleImage.getInstance().displayImage(list2.get(i), ivDetaileditCompleteImageView2);
                    } else if (i == 2) {
                        XSimpleImage.getInstance().displayImage(list2.get(i), ivDetaileditCompleteImageView3);
                    }
                    ImageUploadUtils.getInstance().initImageCode(mContext, list2.get(i), "", 3 + i, this);
                }
            }
        }
    }

    @Override
    public void imageUploadSuccess(String base64, int tag) {
        if (-1 < tag && tag < 3) {
            if (tag < codeList1.size()) {
                codeList1.set(tag, base64);
            } else {
                codeList1.add(base64);
            }
        } else {
            tag = tag - 3;
            if (tag < codeList2.size()) {
                codeList2.set(tag, base64);
            } else {
                codeList2.add(base64);
            }
        }
    }

    @Override
    public void imageUploadFail(int statusFail, String error, int tag) {
        XSimpleToast.showToast("上传失败");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImageUploadUtils.getInstance().onDestroy();
    }
}
