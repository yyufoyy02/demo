package com.gas.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Heart on 2015/8/7.
 */
public class DeliveryOrder implements Parcelable {
    private int id;
    private String order_no;
    private String gas_card;
    private String client_name;
    private String telphone;
    private String address;
    private int area_id;
    private String depot_id;
    private long send_date;
    private String send_time;
    /**
     * 默认状态为0；表示未付款；
     * 状态1：下单成功（付款成功）
     * 状态2：匹配中
     * 状态3：配送中
     * 状态4：确定收货（收货付款）
     * 状态5：订单评论
     */
    private String status;
    private int total_count;
    private double total_cost;
    private double total_yj;
    private String order_list;
    private int pay_type;
    private long add_time;


    public long getAdd_time() {
        return add_time;
    }

    public void setAdd_time(long add_time) {
        this.add_time = add_time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getArea_id() {
        return area_id;
    }

    public void setArea_id(int area_id) {
        this.area_id = area_id;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
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

    public String getOrder_list() {
        return order_list;
    }

    public void setOrder_list(String order_list) {
        this.order_list = order_list;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public int getPay_type() {
        return pay_type;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }

    public long getSend_date() {
        return send_date;
    }

    public void setSend_date(long send_date) {
        this.send_date = send_date;
    }

    public String getSend_time() {
        return send_time;
    }

    public void setSend_time(String send_time) {
        this.send_time = send_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public double getTotal_cost() {
        return total_cost;
    }

    public void setTotal_cost(double total_cost) {
        this.total_cost = total_cost;
    }

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public double getTotal_yj() {
        return total_yj;
    }

    public void setTotal_yj(double total_yj) {
        this.total_yj = total_yj;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.order_no);
        dest.writeString(this.gas_card);
        dest.writeString(this.client_name);
        dest.writeString(this.telphone);
        dest.writeString(this.address);
        dest.writeInt(this.area_id);
        dest.writeString(this.depot_id);
        dest.writeLong(this.send_date);
        dest.writeString(this.send_time);
        dest.writeString(this.status);
        dest.writeInt(this.total_count);
        dest.writeDouble(this.total_cost);
        dest.writeDouble(this.total_yj);
        dest.writeString(this.order_list);
        dest.writeInt(this.pay_type);
        dest.writeLong(this.add_time);
    }

    public DeliveryOrder() {
    }

    protected DeliveryOrder(Parcel in) {
        this.id = in.readInt();
        this.order_no = in.readString();
        this.gas_card = in.readString();
        this.client_name = in.readString();
        this.telphone = in.readString();
        this.address = in.readString();
        this.area_id = in.readInt();
        this.depot_id = in.readString();
        this.send_date = in.readLong();
        this.send_time = in.readString();
        this.status = in.readString();
        this.total_count = in.readInt();
        this.total_cost = in.readDouble();
        this.total_yj = in.readDouble();
        this.order_list = in.readString();
        this.pay_type = in.readInt();
        this.add_time = in.readLong();
    }

    public static final Parcelable.Creator<DeliveryOrder> CREATOR = new Parcelable.Creator<DeliveryOrder>() {
        public DeliveryOrder createFromParcel(Parcel source) {
            return new DeliveryOrder(source);
        }

        public DeliveryOrder[] newArray(int size) {
            return new DeliveryOrder[size];
        }
    };
}
