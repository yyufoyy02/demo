package com.property.activity;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.property.api.LanguageApi;
import com.property.base.BaseActivity;
import com.property.http.MyJsonDataResponseCacheHandler;
import com.property.model.LanguageModel;

import java.util.List;

import butterknife.InjectView;
import butterknife.annotation.event.OnClick;

public class CommonLanguageActivity extends BaseActivity {
    @InjectView(R.id.ll_reason_main)
    LinearLayout llMain;
    @InjectView(R.id.ib_reason_check5)
    ImageButton ibReasonCheck5;
    int postion = 0;

    @Override
    public int onCreateViewLayouId() {
        return R.layout.commonlanguageactivity_main;
    }

    @Override
    public void initAllData() {
        setTitle("选择故障常用语");
        getLanguage();
    }

    @Override
    public void initListener() {

    }

    @OnClick({R.id.tv_complete_submit, R.id.ib_reason_check5})
    void onClick(View v) {
        switch (v.getId()) {

            case R.id.ib_reason_check5:

                check(-1);
                if (ibReasonCheck5.isSelected())
                    return;
                ibReasonCheck5.setSelected(true);
                break;
            case R.id.tv_complete_submit:
                String reason = null;
                if (postion < llMain.getChildCount())
                    reason = ((TextView) llMain.getChildAt(postion).findViewById(R.id.ib_reason_text)).getText().toString();

                if (ibReasonCheck5.isSelected()) {
                    reason = ((TextView) findViewById(R.id.ib_reason_text5)).getText().toString();
                } else {
                    if (postion < llMain.getChildCount())
                        reason = ((TextView) llMain.getChildAt(postion).findViewById(R.id.ib_reason_text)).getText().toString();
                }
                if (reason == null)
                    return;
                setResult(RESULT_OK, new Intent().putExtra("reason", reason));
                finish();
                break;
        }
    }

    void check(int check) {
        for (int i = 0; i < llMain.getChildCount(); i++) {
            postion = i;
            if (check == i) {
                llMain.getChildAt(i).findViewById(R.id.ib_reason_content).setSelected(true);
            } else {
                llMain.getChildAt(i).findViewById(R.id.ib_reason_content).setSelected(false);
            }
        }
    }

    void initView(List<LanguageModel> list) {
        llMain.removeAllViews();
        for (int i = 0; i < list.size(); i++) {
            LanguageModel languageModel = list.get(i);
            View v = LayoutInflater.from(mContext).inflate(R.layout.language_item, null);
            ((TextView) v.findViewById(R.id.ib_reason_text)).setText(languageModel.getShortname());
            ((TextView) v.findViewById(R.id.ib_reason_content)).setText(languageModel.getSolution());
            final int finalI = i;
            v.findViewById(R.id.ib_reason_content).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    check(finalI);
                }
            });
            llMain.addView(llMain);
        }
    }

    void getLanguage() {
        LanguageApi.getInstance().getLanguage(mContext, new MyJsonDataResponseCacheHandler<List<LanguageModel>>(LanguageModel.class,
                true) {
            @Override
            public void onJsonDataSuccess(List<LanguageModel> object) {
                initView(object);
            }

            @Override
            public boolean onJsonCacheData(boolean has) {
                return false;
            }
        });
    }
}
