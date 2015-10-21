package com.property.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Heart on 2015/8/20.
 */
public class Bottle implements Parcelable {
    private String g_id;
    private String admin_id;
    private String user_id;
    private String code;
    private long date;
    private long detection_one_time;
    private long detection_two_time;
    private String gasstate;
    private String spec;
    private String fa;
    private String inout;
    private String detection;
    private String addr_id;
    private String status;
    private String add_time;
    private String mod_time;

    @Override
    public String toString() {
        return "Bottle{" +
                "add_time='" + add_time + '\'' +
                ", g_id='" + g_id + '\'' +
                ", admin_id='" + admin_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", code='" + code + '\'' +
                ", date='" + date + '\'' +
                ", detection_one_time='" + detection_one_time + '\'' +
                ", detection_two_time='" + detection_two_time + '\'' +
                ", gasstate='" + gasstate + '\'' +
                ", spec='" + spec + '\'' +
                ", fa='" + fa + '\'' +
                ", inout='" + inout + '\'' +
                ", detection='" + detection + '\'' +
                ", addr_id='" + addr_id + '\'' +
                ", status='" + status + '\'' +
                ", mod_time='" + mod_time + '\'' +
                '}';
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getAddr_id() {
        return addr_id;
    }

    public void setAddr_id(String addr_id) {
        this.addr_id = addr_id;
    }

    public String getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(String admin_id) {
        this.admin_id = admin_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getDetection() {
        return detection;
    }

    public void setDetection(String detection) {
        this.detection = detection;
    }

    public long getDetection_one_time() {
        return detection_one_time;
    }

    public void setDetection_one_time(long detection_one_time) {
        this.detection_one_time = detection_one_time;
    }

    public long getDetection_two_time() {
        return detection_two_time;
    }

    public void setDetection_two_time(long detection_two_time) {
        this.detection_two_time = detection_two_time;
    }

    public String getFa() {
        return fa;
    }

    public void setFa(String fa) {
        this.fa = fa;
    }

    public String getG_id() {
        return g_id;
    }

    public void setG_id(String g_id) {
        this.g_id = g_id;
    }

    public String getGasstate() {
        return gasstate;
    }

    public void setGasstate(String gasstate) {
        this.gasstate = gasstate;
    }

    public String getInout() {
        return inout;
    }

    public void setInout(String inout) {
        this.inout = inout;
    }

    public String getMod_time() {
        return mod_time;
    }

    public void setMod_time(String mod_time) {
        this.mod_time = mod_time;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.g_id);
        dest.writeString(this.admin_id);
        dest.writeString(this.user_id);
        dest.writeString(this.code);
        dest.writeLong(this.date);
        dest.writeLong(this.detection_one_time);
        dest.writeLong(this.detection_two_time);
        dest.writeString(this.gasstate);
        dest.writeString(this.spec);
        dest.writeString(this.fa);
        dest.writeString(this.inout);
        dest.writeString(this.detection);
        dest.writeString(this.addr_id);
        dest.writeString(this.status);
        dest.writeString(this.add_time);
        dest.writeString(this.mod_time);
    }

    public Bottle() {
    }

    protected Bottle(Parcel in) {
        this.g_id = in.readString();
        this.admin_id = in.readString();
        this.user_id = in.readString();
        this.code = in.readString();
        this.date = in.readLong();
        this.detection_one_time = in.readLong();
        this.detection_two_time = in.readLong();
        this.gasstate = in.readString();
        this.spec = in.readString();
        this.fa = in.readString();
        this.inout = in.readString();
        this.detection = in.readString();
        this.addr_id = in.readString();
        this.status = in.readString();
        this.add_time = in.readString();
        this.mod_time = in.readString();
    }

    public static final Creator<Bottle> CREATOR = new Creator<Bottle>() {
        public Bottle createFromParcel(Parcel source) {
            return new Bottle(source);
        }

        public Bottle[] newArray(int size) {
            return new Bottle[size];
        }
    };
}
