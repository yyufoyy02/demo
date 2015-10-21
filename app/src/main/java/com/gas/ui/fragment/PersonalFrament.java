package com.property.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.property.conf.Common;
import com.property.connector.HttpCallBack;
import com.property.connector.protocol.BusinessHttpProtocol;
import com.property.database.SharedPreferenceUtil;
import com.property.entity.User;
import com.property.epiboly.R;
import com.property.ui.common.BaseFragment;
import com.property.utils.Utils;
import com.property.utils.wrapCarUtil;

import org.json.JSONObject;

/**
 * Created by Heart on 2015/7/22.
 */
public class PersonalFrament extends BaseFragment implements View.OnClickListener,HttpCallBack {

    private long GET_CAR_FLAG = -1;
    private User user =  Common.getInstance().user;
    private Activity mActivity;
    private View rootView;
    private LinearLayout ly_text_personal;
    private LinearLayout ly_edit_personal;
    private TextView user_name;
    private TextView user_phone;
    private TextView area;  //地址
    private TextView depot;      //性别
    private TextView position;  //备用电话
    private TextView user_service;   //服务项目
    private Switch shareSwitch;

    private boolean isSharePlaces=false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_personal, container,
                false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = this.getActivity();
        rootView = getView();
        init();
        initListener();
    }

    public void init() {
        ly_text_personal = (LinearLayout) rootView.findViewById(R.id.ly_text_personal);
        user_name = (TextView) rootView.findViewById(R.id.user_name);
        user_phone = (TextView) rootView.findViewById(R.id.user_phone);
        area = (TextView) rootView.findViewById(R.id.user_address);
        depot = (TextView) rootView.findViewById(R.id.user_sex);
        position = (TextView) rootView.findViewById(R.id.user_alternate_phone);
        user_service = (TextView) rootView.findViewById(R.id.user_service);
        shareSwitch = (Switch) rootView.findViewById(R.id.switch_button);

        user_name.setText(user.getName());
        user_phone.setText(user.getPhone());

        area.setText(user.getArea());
        depot.setText(user.getDepot());
        position.setText(user.getPosition());

        isSharePlaces = SharedPreferenceUtil.getInstance(getActivity()).getString(SharedPreferenceUtil.SHARED_PLACES).equals("1");
        shareSwitch.setChecked(isSharePlaces);
    }

    public void initListener() {
        shareSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked) {
                    if(!wrapCarUtil.startLocation(getActivity())) {
                        GET_CAR_FLAG = BusinessHttpProtocol.getCar(PersonalFrament.this, user.getId() + "", user.getDepot_id() + "");
                        showProgressDialog(GET_CAR_FLAG);
                    }
                }else {
                    wrapCarUtil.stopLocation(getActivity());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onGeneralSuccess(String result, long flag) {

        try {
            JSONObject json = new JSONObject(result);
            if(flag == GET_CAR_FLAG){
                dismissProgressDialog();
                if(json.optString("sign").equals("one")){
                    JSONObject carJson =  json.getJSONObject("car_one");
                    wrapCarUtil.startLocation(getActivity(),carJson.optString("id"));
                }else {
                    Utils.toastMsg(getActivity(),"请先绑定车号");
                    shareSwitch.setChecked(false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGeneralError(String e, long flag) {

    }
}
