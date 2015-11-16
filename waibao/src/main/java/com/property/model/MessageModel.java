package com.property.model;

import com.property.base.BaseModel;
import com.vk.simpleutil.library.XSimpleText;

public class MessageModel extends BaseModel {
    String fault_id;
    int status;
    ImageModel icon;
    String address;
    String name;
    double time;
    int message_type;
    int periods;
    int max_periods;

    public String getFault_id() {
        return XSimpleText.isEmpty(fault_id, "");
    }

    public void setFault_id(String fault_id) {
        this.fault_id = fault_id;
    }

    /**
     * 期数
     */
    public int getPeriods() {
        return periods;
    }

    public void setPeriods(int periods) {
        this.periods = periods;
    }

    /**
     * 最大期数
     */
    public int getMax_periods() {
        return max_periods;
    }

    public void setMax_periods(int max_periods) {
        this.max_periods = max_periods;
    }

    /**
     * 状态: （2：等待处理，3：已经签到正在处理，4：抢修完成）
     */
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * 图标
     */
    public ImageModel getIcon() {
        if (icon == null)
            icon = new ImageModel();
        return icon;
    }

    public void setIcon(ImageModel icon) {
        this.icon = icon;
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
     * 名称
     */
    public String getName() {
        return XSimpleText.isEmpty(name, "");
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 时间
     */
    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    /**
     * 消息类型(抢修：0  ，维保：1)
     */
    public int getMessage_type() {
        return message_type;
    }

    public void setMessage_type(int message_type) {
        this.message_type = message_type;
    }
}
