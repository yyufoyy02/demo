package com.gas.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Heart on 2015/8/21.
 */

public class BottleBean {


    public List<BottleLog> data = new ArrayList<>();
    public static class BottleLog {

        public String id;
        public String code;
        public String admin_id;
        public String inout;
        public long add_time;
        public String flag;
        public String msg;


        public long getAdd_time() {
            return add_time;
        }

        public void setAdd_time(long add_time) {
            this.add_time = add_time;
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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getInout() {
            return inout;
        }

        public void setInout(String inout) {
            this.inout = inout;
        }

        @Override
        public String toString() {
            return "BottleLog{" +
                    "add_time=" + add_time +
                    ", id='" + id + '\'' +
                    ", code='" + code + '\'' +
                    ", admin_id='" + admin_id + '\'' +
                    ", inout='" + inout + '\'' +
                    '}';
        }


    }
}