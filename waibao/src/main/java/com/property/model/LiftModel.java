package com.property.model;

import com.property.base.BaseModel;
import com.vk.simpleutil.library.XSimpleText;

public class LiftModel extends BaseModel {
    String lift_id;
    String fault_id;
    String maintenance_id;
    String lift_code;
    String lift_brand;
    String lift_address;
    String customer;
    String company;
    String phone;
    String fault_type;
    double start_time;
    double last_time;

    public String getMaintenance_id() {
        return XSimpleText.isEmpty(maintenance_id, "");
    }

    public void setMaintenance_id(String maintenance_id) {
        this.maintenance_id = maintenance_id;
    }

    public double getStart_time() {
        return start_time;
    }

    public void setStart_time(double start_time) {
        this.start_time = start_time;
    }

    public double getLast_time() {
        return last_time;
    }

    public void setLast_time(double last_time) {
        this.last_time = last_time;
    }

    public String getLift_id() {
        return XSimpleText.isEmpty(lift_id, "");
    }

    public void setLift_id(String lift_id) {
        this.lift_id = lift_id;
    }

    public String getFault_id() {
        return XSimpleText.isEmpty(fault_id, "");
    }

    public void setFault_id(String fault_id) {
        this.fault_id = fault_id;
    }

    public String getLift_code() {
        return XSimpleText.isEmpty(lift_code, "");
    }

    public void setLift_code(String lift_code) {
        this.lift_code = lift_code;
    }

    public String getLift_brand() {
        return XSimpleText.isEmpty(lift_brand, "");
    }

    public void setLift_brand(String lift_brand) {
        this.lift_brand = lift_brand;
    }

    public String getLift_address() {
        return XSimpleText.isEmpty(lift_address, "");
    }

    public void setLift_address(String lift_address) {
        this.lift_address = lift_address;
    }

    public String getCustomer() {
        return XSimpleText.isEmpty(customer, "");
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getCompany() {
        return XSimpleText.isEmpty(company, "");
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPhone() {
        return XSimpleText.isEmpty(phone, "");
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFault_type() {
        return XSimpleText.isEmpty(fault_type, "");
    }

    public void setFault_type(String fault_type) {
        this.fault_type = fault_type;
    }
}
