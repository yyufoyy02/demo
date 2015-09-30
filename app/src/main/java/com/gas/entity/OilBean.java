package com.gas.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Heart on 2015/8/23.
 */
public class OilBean {

    public List<Oil> oil_log = new ArrayList<>();

    public static class Oil {
        private String id;
        private String car_id;
        private String oil;
        private String oil_cost;
        private long add_date;
        private String car_no;

        public long getAdd_date() {
            return add_date;
        }

        public void setAdd_date(long add_date) {
            this.add_date = add_date;
        }

        public String getCar_id() {
            return car_id;
        }

        public void setCar_id(String car_id) {
            this.car_id = car_id;
        }

        public String getCar_no() {
            return car_no;
        }

        public void setCar_no(String car_no) {
            this.car_no = car_no;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOil() {
            return oil;
        }

        public void setOil(String oil) {
            this.oil = oil;
        }

        public String getOil_cost() {
            return oil_cost;
        }

        public void setOil_cost(String oil_cost) {
            this.oil_cost = oil_cost;
        }

        @Override
        public String toString() {
            return "Oil{" +
                    "add_date='" + add_date + '\'' +
                    ", id='" + id + '\'' +
                    ", car_id='" + car_id + '\'' +
                    ", oil='" + oil + '\'' +
                    ", oil_cost='" + oil_cost + '\'' +
                    ", car_no='" + car_no + '\'' +
                    '}';
        }
    }
}
