package com.property.model;

import com.property.base.BaseModel;

public class CountModel extends BaseModel {
    int fault_count;
    int maintenance_count;

    public int getFault_count() {
        return fault_count;
    }

    public void setFault_count(int fault_count) {
        this.fault_count = fault_count;
    }

    public int getMaintenance_count() {
        return maintenance_count;
    }

    public void setMaintenance_count(int maintenance_count) {
        this.maintenance_count = maintenance_count;
    }
}
