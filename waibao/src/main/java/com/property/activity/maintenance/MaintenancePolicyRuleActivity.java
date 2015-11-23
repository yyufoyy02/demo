package com.property.activity.maintenance;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.property.activity.R;
import com.property.api.MaintenanceApi;
import com.property.base.BaseActivity;
import com.property.http.MyJsonDataResponseCacheHandler;
import com.property.model.RuleModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.annotation.event.OnClick;

public class MaintenancePolicyRuleActivity extends BaseActivity {
    String ruleID;
    @InjectView(R.id.ll_rule_main)
    LinearLayout llRuleMain;
    List<RuleModel> ruleModelList = new ArrayList<>();
    List<RuleModel> rules;

    @Override
    public int onCreateViewLayouId() {
        return R.layout.maintenancepolicyruleactivity_main;
    }

    @Override
    public void initAllData() {
        setTitle("维保细则");
        ruleID = getIntent().getStringExtra("ruleID");
        rules = (List<RuleModel>) getIntent().getSerializableExtra("rules");
        if (ruleID != null)
            getRuleList();
        if (rules != null)
            initView(rules);
    }

    @Override
    public void initListener() {

    }

    @OnClick(R.id.tv_maintenancepolicyrule_submit)
    void submit(View v) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < llRuleMain.getChildCount(); i++) {
            if (llRuleMain.getChildAt(i).findViewById(R.id.iv_maintenancepolicyruleitem_switch).isSelected())
                list.add(ruleModelList.get(i).getId());
        }
        setResult(RESULT_OK, new Intent().putExtra("ruleIDs", (Serializable) list));
        finish();
    }

    void initView(List<RuleModel> list) {
        if (list == null)
            return;
        ruleModelList.clear();
        ruleModelList.addAll(list);
        llRuleMain.removeAllViews();
        for (int i = 0; i < list.size(); i++) {
            RuleModel ruleModel = list.get(i);
            View v = LayoutInflater.from(mContext).inflate(R.layout.maintenancepolicyruleactivity_item, null);
            ((TextView) v.findViewById(R.id.tv_maintenancepolicyruleitem_name)).setText(ruleModel.getName());
            View view = v.findViewById(R.id.iv_maintenancepolicyruleitem_switch);
            if (ruleID != null) {
                view.setSelected(true);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.setSelected(!v.isSelected());
                    }
                });
            } else {
                if (ruleModel.getType() == 0) {
                    view.setSelected(false);
                } else {
                    view.setSelected(true);
                }
            }
            llRuleMain.addView(v);
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
