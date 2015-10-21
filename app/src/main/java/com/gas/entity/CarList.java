package com.property.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Heart on 2015/8/22.
 */
public class CarList {
    public List<Car> car_no = new ArrayList();

    public static class Car implements Parcelable {
        public String id;
        public String car_no;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.car_no);
        }

        public Car() {
        }

        protected Car(Parcel in) {
            this.id = in.readString();
            this.car_no = in.readString();
        }

        public static final Parcelable.Creator<Car> CREATOR = new Parcelable.Creator<Car>() {
            public Car createFromParcel(Parcel source) {
                return new Car(source);
            }

            public Car[] newArray(int size) {
                return new Car[size];
            }
        };

        @Override
        public String toString() {
            return "Car{" +
                    "car_no='" + car_no + '\'' +
                    ", id='" + id + '\'' +
                    '}';
        }
    }
}
