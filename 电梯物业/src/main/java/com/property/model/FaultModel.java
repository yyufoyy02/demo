package com.property.model;

import com.property.base.BaseModel;
import com.vk.simpleutil.library.XSimpleText;

/**
 * Created by Administrator on 2015/11/8.
 */
public class FaultModel extends BaseModel {
    String id;
    String fault_number;
    String lift_id;
    String fault_type;
    String address;
    String elevetor_number;
    String reg_code;
    int status;

    public String getId() {
        return XSimpleText.isEmpty(id, "");
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFault_number() {
        return XSimpleText.isEmpty(fault_number, "");
    }

    public void setFault_number(String fault_number) {
        this.fault_number = fault_number;
    }

    public String getLift_id() {
        return XSimpleText.isEmpty(lift_id, "");
    }

    public void setLift_id(String lift_id) {
        this.lift_id = lift_id;
    }

    public String getFault_type() {
        return XSimpleText.isEmpty(fault_type, "");
    }

    public void setFault_type(String fault_type) {
        this.fault_type = fault_type;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
