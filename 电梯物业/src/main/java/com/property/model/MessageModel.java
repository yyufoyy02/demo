package com.property.model;

import com.property.base.BaseModel;
import com.vk.simpleutil.library.XSimpleText;

public class MessageModel extends BaseModel {
    int status;
    ImageModel icon;
    String address;
    String name;
    double time;
    int message_type;

    /**
     * 状态: （正在处理：0  完成：1）
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
