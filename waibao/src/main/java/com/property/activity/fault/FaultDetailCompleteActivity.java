package com.property.activity.fault;


import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.property.activity.R;
import com.property.api.FaultApi;
import com.property.base.BaseActivity;
import com.property.http.MyJsonDataResponseCacheHandler;
import com.property.model.RepairModel;
import com.vk.simpleutil.library.XSimpleImage;

import butterknife.InjectView;

public class FaultDetailCompleteActivity extends BaseActivity {

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
    @InjectView(R.id.iv_detailedit_photo)
    ImageView ivEditPhoto;
    @InjectView(R.id.iv_detailedit_complete_photo)
    ImageView ivCompletePhoto;
    @InjectView(R.id.tv_complete_submit)
    TextView tvSubmit;
    @InjectView(R.id.iv_detail_say_arrow)
    ImageView ivArrow;
    @InjectView(R.id.tv_detail_say_reason)
    TextView tvSayReason;


    @Override
    public int onCreateViewLayouId() {
        return R.layout.completeedit_activity;
    }

    @Override
    public void initAllData() {
        setTitle("维修详情");
        id = getIntent().getStringExtra("id");
        ivEditPhoto.setVisibility(View.GONE);
        ivCompletePhoto.setVisibility(View.GONE);
        tvSubmit.setVisibility(View.GONE);
        ivArrow.setVisibility(View.GONE);
        edtCompleteName.setFocusable(false);
        edtCompleteName.setEnabled(false);
        edtCompletePrice.setFocusable(false);
        edtCompletePrice.setEnabled(false);
        getDetail();
    }

    @Override
    public void initListener() {

    }

    void initView(RepairModel repairModel) {
        for (int i = 0; i < repairModel.getB_img().size(); i++) {
            if (i == 0) {
                XSimpleImage.getInstance().displayImage(repairModel.getB_img().get(i), ivDetaileditFaultImageView1);
            } else if (i == 1) {
                XSimpleImage.getInstance().displayImage(repairModel.getB_img().get(i), ivDetaileditFaultImageView2);
            } else if (i == 2) {
                XSimpleImage.getInstance().displayImage(repairModel.getB_img().get(i), ivDetaileditFaultImageView3);
            }
        }
        for (int i = 0; i < repairModel.getE_img().size(); i++) {
            if (i == 0) {
                XSimpleImage.getInstance().displayImage(repairModel.getE_img().get(i), ivDetaileditCompleteImageView1);
            } else if (i == 1) {
                XSimpleImage.getInstance().displayImage(repairModel.getE_img().get(i), ivDetaileditCompleteImageView2);
            } else if (i == 2) {
                XSimpleImage.getInstance().displayImage(repairModel.getE_img().get(i), ivDetaileditCompleteImageView3);
            }
        }
        tvSayReason.setText(repairModel.getFault_describe());
        if (repairModel.isFault_parts()) {
            tvCompleteYes.setSelected(true);
            tvCompleteNo.setSelected(false);
        } else {
            tvCompleteYes.setSelected(false);
            tvCompleteNo.setSelected(true);
        }
        edtCompleteName.setText(repairModel.getFault_parts_name());
        edtCompletePrice.setText(repairModel.getFault_cost());
    }

    void getDetail() {
        showProgressDialog();
        FaultApi.getInstance().getDeal(mContext, id, new MyJsonDataResponseCacheHandler<RepairModel>(RepairModel.class, false) {
            @Override
            public void onHttpComplete() {
                super.onHttpComplete();
                dismissProgressDialog();
            }

            @Override
            public void onJsonDataSuccess(RepairModel object) {
                initView(object);
            }

            @Override
            public boolean onJsonCacheData(boolean has) {
                return false;
            }
        });

    }
}
