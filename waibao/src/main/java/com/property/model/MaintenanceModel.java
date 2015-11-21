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
    String customer;
    double time;
    String rule;
    int type;
    int type2;
    String new_rule_id;

    public String getNew_rule_id() {
        return XSimpleText.isEmpty(new_rule_id, "");
    }

    public void setNew_rule_id(String new_rule_id) {
        this.new_rule_id = new_rule_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType2() {
        return type2;
    }

    public void setType2(int type2) {
        this.type2 = type2;
    }

    /**
     * 月保类型
     */
    public String getRule() {
        return XSimpleText.isEmpty(rule, "");
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    /**
     * 物业公司名字,
     */
    public String getCustomer() {
        return XSimpleText.isEmpty(customer, "");
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    /**
     * 时间,
     */
    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    /**
     * 维保单id,
     */
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

    /**
     * 地址,
     */
    public String getAddress() {
        return XSimpleText.isEmpty(address, "");
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 梯号,
     */
    public String getElevetor_number() {
        return XSimpleText.isEmpty(elevetor_number, "");
    }

    public void setElevetor_number(String elevetor_number) {
        this.elevetor_number = elevetor_number;
    }

    /**
     * reg_code
     */
    public String getReg_code() {
        return XSimpleText.isEmpty(reg_code, "");
    }

    public void setReg_code(String reg_code) {
        this.reg_code = reg_code;
    }

    /**
     * 维保单号
     */
    public String getWb_order_no() {
        return XSimpleText.isEmpty(wb_order_no, "");
    }

    public void setWb_order_no(String wb_order_no) {
        this.wb_order_no = wb_order_no;
    }

    /**
     * 维保状态，1：正在维保，2：完成
     */
    public int getM_status() {
        return m_status;
    }

    public void setM_status(int m_status) {
        this.m_status = m_status;
    }

}
