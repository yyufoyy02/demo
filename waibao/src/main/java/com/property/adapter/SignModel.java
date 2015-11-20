package com.property.adapter;


import com.property.base.BaseModel;
import com.vk.simpleutil.library.XSimpleText;

public class SignModel extends BaseModel {
    String maintenance_id;
    String new_rule_id;
    int type;
    int type2;

    /**
     * 生成的故障单id,
     */
    public String getMaintenance_id() {
        return XSimpleText.isEmpty(maintenance_id, "");
    }

    public void setMaintenance_id(String maintenance_id) {
        this.maintenance_id = maintenance_id;
    }

    /**
     * 维保细则id
     */
    public String getNew_rule_id() {
        return XSimpleText.isEmpty(new_rule_id, "");
    }

    public void setNew_rule_id(String new_rule_id) {
        this.new_rule_id = new_rule_id;
    }

    /**
     * 1为电子维保单，2为纸质维保单
     */
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /**
     * 1为无最后一步确定维保，2为有
     */
    public int getType2() {
        return type2;
    }

    public void setType2(int type2) {
        this.type2 = type2;
    }
}
