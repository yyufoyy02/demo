package com.property.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Heart on 2015/8/17.
 */
public class RepairOrder implements Parcelable {
    private int id;
    private String order_no;
    private String client_name;
    private String openid;
    private String gas_card;
    private String area_id;
    private String depot_id;
    private String telphone;
    private String address;
    private long repair_date;
    private String repair_time;
    private String repair_type;
    private String remark;
    private int status;
    private String total_cost;
    private String pay_time;
    private long ctime;
    private long utime;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public long getCtime() {
        return ctime;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }

    public String getDepot_id() {
        return depot_id;
    }

    public void setDepot_id(String depot_id) {
        this.depot_id = depot_id;
    }

    public String getGas_card() {
        return gas_card;
    }

    public void setGas_card(String gas_card) {
        this.gas_card = gas_card;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getPay_time() {
        return pay_time;
    }

    public void setPay_time(String pay_time) {
        this.pay_time = pay_time;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public long getRepair_date() {
        return repair_date;
    }

    public void setRepair_date(long repair_date) {
        this.repair_date = repair_date;
    }

    public String getRepair_time() {
        return repair_time;
    }

    public void setRepair_time(String repair_time) {
        this.repair_time = repair_time;
    }

    public String getRepair_type() {
        return repair_type;
    }

    public void setRepair_type(String repair_type) {
        this.repair_type = repair_type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public String getTotal_cost() {
        return total_cost;
    }

    public void setTotal_cost(String total_cost) {
        this.total_cost = total_cost;
    }

    public long getUtime() {
        return utime;
    }

    public void setUtime(long utime) {
        this.utime = utime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.order_no);
        dest.writeString(this.client_name);
        dest.writeString(this.openid);
        dest.writeString(this.gas_card);
        dest.writeString(this.area_id);
        dest.writeString(this.depot_id);
        dest.writeString(this.telphone);
        dest.writeString(this.address);
        dest.writeLong(this.repair_date);
        dest.writeString(this.repair_time);
        dest.writeString(this.repair_type);
        dest.writeString(this.remark);
        dest.writeInt(this.status);
        dest.writeString(this.total_cost);
        dest.writeString(this.pay_time);
        dest.writeLong(this.ctime);
        dest.writeLong(this.utime);
    }

    public RepairOrder() {
    }

    protected RepairOrder(Parcel in) {
        this.id = in.readInt();
        this.order_no = in.readString();
        this.client_name = in.readString();
        this.openid = in.readString();
        this.gas_card = in.readString();
        this.area_id = in.readString();
        this.depot_id = in.readString();
        this.telphone = in.readString();
        this.address = in.readString();
        this.repair_date = in.readLong();
        this.repair_time = in.readString();
        this.repair_type = in.readString();
        this.remark = in.readString();
        this.status = in.readInt();
        this.total_cost = in.readString();
        this.pay_time = in.readString();
        this.ctime = in.readLong();
        this.utime = in.readLong();
    }

    public static final Creator<RepairOrder> CREATOR = new Creator<RepairOrder>() {
        public RepairOrder createFromParcel(Parcel source) {
            return new RepairOrder(source);
        }

        public RepairOrder[] newArray(int size) {
            return new RepairOrder[size];
        }
    };
}
