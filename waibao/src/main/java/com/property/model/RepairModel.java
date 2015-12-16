package com.property.model;


import com.property.base.BaseModel;
import com.vk.simpleutil.library.XSimpleText;

import java.util.ArrayList;
import java.util.List;

public class RepairModel extends BaseModel {
    String fault_number;
    List<String> b_img;
    List<String> e_img;
    String fault_describe;
    int fault_parts;
    String fault_parts_name;
    String fault_cost;
    double etime;
    String remarks;

    public String getRemarks() {
        return XSimpleText.isEmpty(remarks, "");
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }


    /**
     * 故障单号
     */
    public String getFault_number() {
        return XSimpleText.isEmpty(fault_number, "");
    }

    public void setFault_number(String fault_number) {
        this.fault_number = fault_number;
    }

    /**
     * 维修前的照片
     */
    public List<String> getB_img() {
        if (b_img == null)
            b_img = new ArrayList<>();
        return b_img;
    }

    public void setB_img(List<String> b_img) {
        this.b_img = b_img;
    }

    /**
     * 维修后的照片
     */
    public List<String> getE_img() {
        if (e_img == null)
            e_img = new ArrayList<>();
        return e_img;
    }

    public void setE_img(List<String> e_img) {
        this.e_img = e_img;
    }

    /**
     * 故障短语
     */
    public String getFault_describe() {
        return XSimpleText.isEmpty(fault_describe, "");
    }

    public void setFault_describe(String fault_describe) {
        this.fault_describe = fault_describe;
    }

    /**
     * 是否更换配件
     */
    public boolean isFault_parts() {
        return fault_parts == 1 ? true : false;
    }

    public void setFault_parts(int fault_parts) {
        this.fault_parts = fault_parts;
    }

    /**
     * 配件名字
     */
    public String getFault_parts_name() {
        return XSimpleText.isEmpty(fault_parts_name, "");
    }

    public void setFault_parts_name(String fault_parts_name) {
        this.fault_parts_name = fault_parts_name;
    }

    /**
     * 维修金额
     */
    public String getFault_cost() {
        return XSimpleText.isEmpty(fault_cost, "");
    }

    public void setFault_cost(String fault_cost) {
        this.fault_cost = fault_cost;
    }

    /**
     * 维修时间
     */
    public double getEtime() {
        return etime;
    }

    public void setEtime(double etime) {
        this.etime = etime;
    }
}
