package com.property.activity;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.property.api.MaintenanceApi;
import com.property.base.BaseActivity;
import com.property.http.MyJsonDataResponseCacheHandler;
import com.property.model.RuleModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.annotation.event.OnClick;

public class MaintenancePolicyCriterionActivity extends BaseActivity {
    String ruleID;
    @InjectView(R.id.ll_paper_main)
    LinearLayout llPaperMain;
    List<RuleModel> ruleModelList = new ArrayList<>();

    @Override
    public int onCreateViewLayouId() {
        return R.layout.maintenancepolicypaperactivity_main;
    }

    @Override
    public void initAllData() {
        setTitle("维保细则");
        ruleID = getIntent().getStringExtra("ruleID");
        getRuleList();
    }

    @Override
    public void initListener() {

    }

    @OnClick(R.id.tv_maintenancepolicypaper_submit)
    void submit(View v) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < llPaperMain.getChildCount(); i++) {
            if (llPaperMain.getChildAt(i).findViewById(R.id.iv_maintenancepolicypaperitem_switch).isSelected())
                list.add(ruleModelList.get(i).getId());
        }
        setResult(RESULT_OK, new Intent().putExtra("ruleIDs", (Serializable) list));
    }

    void initView(List<RuleModel> list) {
        if (list == null)
            return;
        ruleModelList.clear();
        ruleModelList.addAll(list);
        llPaperMain.removeAllViews();
        for (int i = 0; i < list.size(); i++) {
            RuleModel ruleModel = list.get(i);
            View v = LayoutInflater.from(mContext).inflate(R.layout.maintenancepolicypaperactivity_item, null);
            ((TextView) v.findViewById(R.id.tv_maintenancepolicypaperitem_name)).setText(ruleModel.getName());
            View view = v.findViewById(R.id.iv_maintenancepolicypaperitem_switch);
            view.setSelected(true);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setSelected(!v.isSelected());
                }
            });
            llPaperMain.addView(v);
        }
    }

    void getRuleList() {
        MaintenanceApi.getInstance().getRuleList(mContext, ruleID, new MyJsonDataResponseCacheHandler<List<RuleModel>>(RuleModel.class, false) {
            @Override
            public void onJsonDataSuccess(List<RuleModel> object) {
                initView(object);
            }

            @Override
            public boolean onJsonCacheData(boolean has) {
                return false;
            }
        });
    }
}
