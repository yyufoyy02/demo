package com.property.model;

import com.property.base.BaseModel;
import com.vk.simpleutil.library.XSimpleText;

/**
 * Created by Administrator on 2015/11/8.
 */
public class MaintenanceModel extends BaseModel {
    String id;
    String wb_order_no;
    String lift_id;
    String address;
    String elevetor_number;
    String reg_code;
    int m_status;
    int sign_status;

    public String getId() {
        return XSimpleText.isEmpty(id, "");
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getLift_id() {
        return XSimpleText.isEmpty(lift_id, "");
    }

    public void setLift_id(String lift_id) {
        this.lift_id = lift_id;
    }


    public String getAddress() {
        return XSimpleText.isEmpty(address, "");
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getElevetor_number() {
        return XSimpleText.isEmpty(elevetor_number, "");
    }

    public void setElevetor_number(String elevetor_number) {
        this.elevetor_number = elevetor_number;
    }

    public String getReg_code() {
        return XSimpleText.isEmpty(reg_code, "");
    }

    public void setReg_code(String reg_code) {
        this.reg_code = reg_code;
    }

    public String getWb_order_no() {
        return XSimpleText.isEmpty(wb_order_no, "");
    }

    public void setWb_order_no(String wb_order_no) {
        this.wb_order_no = wb_order_no;
    }

    public int getM_status() {
        return m_status;
    }

    public void setM_status(int m_status) {
        this.m_status = m_status;
    }

    public int getSign_status() {
        return sign_status;
    }

    public void setSign_status(int sign_status) {
        this.sign_status = sign_status;
    }
}
