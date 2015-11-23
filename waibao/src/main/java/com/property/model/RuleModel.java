package com.property.model;


import com.property.base.BaseModel;
import com.vk.simpleutil.library.XSimpleText;

public class RuleModel extends BaseModel {
    String id;
    String name;
    int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return XSimpleText.isEmpty(id, "");
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return XSimpleText.isEmpty(name, "");
    }

    public void setName(String name) {
        this.name = name;
    }
}
