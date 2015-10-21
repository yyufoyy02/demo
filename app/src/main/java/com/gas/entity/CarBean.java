package com.property.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Heart on 2015/8/22.
 */
public class CarBean implements Parcelable {

    private String id;
    private String car_no;
    private String brand;
    private String color;
    private String type; // 电动车,
    private String driver_id; // 26,
    private String depot_id;  //9,
    private String last_check_time; // 2015-07-21 22 43 56,
    private String in_time;// 2015-07-20 22 30 05,
    private String create_time;// 2015-07-20 20 52 13,
    private String update_time; //2015-08-13 01 08 02


    @Override
    public String toString() {
        return "CarBean{" +
                "brand='" + brand + '\'' +
                ", id='" + id + '\'' +
                ", car_no='" + car_no + '\'' +
                ", color='" + color + '\'' +
                ", type='" + type + '\'' +
                ", driver_id='" + driver_id + '\'' +
                ", depot_id='" + depot_id + '\'' +
                ", last_check_time='" + last_check_time + '\'' +
                ", in_time='" + in_time + '\'' +
                ", create_time='" + create_time + '\'' +
                ", update_time='" + update_time + '\'' +
                '}';
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCar_no() {
        return car_no;
    }

    public void setCar_no(String car_no) {
        this.car_no = car_no;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getDepot_id() {
        return depot_id;
    }

    public void setDepot_id(String depot_id) {
        this.depot_id = depot_id;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIn_time() {
        return in_time;
    }

    public void setIn_time(String in_time) {
        this.in_time = in_time;
    }

    public String getLast_check_time() {
        return last_check_time;
    }

    public void setLast_check_time(String last_check_time) {
        this.last_check_time = last_check_time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.car_no);
        dest.writeString(this.brand);
        dest.writeString(this.color);
        dest.writeString(this.type);
        dest.writeString(this.driver_id);
        dest.writeString(this.depot_id);
        dest.writeString(this.last_check_time);
        dest.writeString(this.in_time);
        dest.writeString(this.create_time);
        dest.writeString(this.update_time);
    }

    public CarBean() {
    }

    protected CarBean(Parcel in) {
        this.id = in.readString();
        this.car_no = in.readString();
        this.brand = in.readString();
        this.color = in.readString();
        this.type = in.readString();
        this.driver_id = in.readString();
        this.depot_id = in.readString();
        this.last_check_time = in.readString();
        this.in_time = in.readString();
        this.create_time = in.readString();
        this.update_time = in.readString();
    }

    public static final Parcelable.Creator<CarBean> CREATOR = new Parcelable.Creator<CarBean>() {
        public CarBean createFromParcel(Parcel source) {
            return new CarBean(source);
        }

        public CarBean[] newArray(int size) {
            return new CarBean[size];
        }
    };
}
