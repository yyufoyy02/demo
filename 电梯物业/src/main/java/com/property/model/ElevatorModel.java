package com.property.model;

import com.property.base.BaseModel;
import com.vk.simpleutil.library.XSimpleText;

/**
 * Created by Administrator on 2015/10/26.
 */
public class ElevatorModel extends BaseModel {
    String id;
    String address;
    String brand;
    String property_company;
    String maintenance_company;
    String phone;
    double last_time;
    double this_time;
    String reason;

    /**
     * 原因
     */
    public String getReason() {
        return XSimpleText.isEmpty(reason, "");
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * 电话ID
     */
    public String getId() {
        return XSimpleText.isEmpty(id, "");
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * 地址
     */
    public String getAddress() {
        return XSimpleText.isEmpty(address, "");
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 品牌
     */
    public String getBrand() {
        return XSimpleText.isEmpty(brand, "");
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * 物业公司
     */
    public String getProperty_company() {
        return XSimpleText.isEmpty(property_company, "");
    }

    public void setProperty_company(String property_company) {
        this.property_company = property_company;
    }

    /**
     * 维保公司
     */
    public String getMaintenance_company() {
        return XSimpleText.isEmpty(maintenance_company, "");
    }

    public void setMaintenance_company(String maintenance_company) {
        this.maintenance_company = maintenance_company;
    }

    /**
     * 电话
     */
    public String getPhone() {
        return XSimpleText.isEmpty(phone, "");
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 上次维保时间
     */
    public double getLast_time() {
        return last_time;
    }

    public void setLast_time(double last_time) {
        this.last_time = last_time;
    }

    /**
     * 这次维保时间
     */
    public double getThis_time() {
        return this_time;
    }

    public void setThis_time(double this_time) {
        this.this_time = this_time;
    }
}
