package com.property.model;


import com.property.base.BaseModel;
import com.vk.simpleutil.library.XSimpleText;

/**
 * 用户
 */
public class UserModel extends BaseModel {
    String staff_id;
    String name;
    String phone;
    String department;

    /**
     * 员工id
     */
    public String getStaff_id() {
        return XSimpleText.isEmpty(staff_id, "");
    }

    public void setStaff_id(String staff_id) {
        this.staff_id = staff_id;
    }

    /**
     * 员工姓名
     */
    public String getName() {
        return XSimpleText.isEmpty(name, "");
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 员工电话
     */
    public String getPhone() {
        return XSimpleText.isEmpty(phone, "");
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 员工部门
     */
    public String getDepartment() {
        return XSimpleText.isEmpty(department, "");
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
