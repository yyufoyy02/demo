package com.property.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.property.base.BaseModel;
import com.vk.simpleutil.library.XSimpleText;

public class SignModel extends BaseModel implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.maintenance_id);
        dest.writeString(this.new_rule_id);
        dest.writeInt(this.type);
        dest.writeInt(this.type2);
    }

    public SignModel() {
    }

    protected SignModel(Parcel in) {
        this.maintenance_id = in.readString();
        this.new_rule_id = in.readString();
        this.type = in.readInt();
        this.type2 = in.readInt();
    }

    public static final Parcelable.Creator<SignModel> CREATOR = new Parcelable.Creator<SignModel>() {
        public SignModel createFromParcel(Parcel source) {
            return new SignModel(source);
        }

        public SignModel[] newArray(int size) {
            return new SignModel[size];
        }
    };
}
