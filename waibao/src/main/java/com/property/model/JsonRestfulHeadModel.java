package com.property.model;

import com.google.gson.JsonElement;
import com.property.base.BaseModel;
import com.vk.simpleutil.library.XSimpleText;

/**
 * Created by Administrator on 2015/11/6.
 */
public class JsonRestfulHeadModel extends BaseModel {
    String flag;
    String msg;
    JsonElement data;

    public String getFlag() {
        return XSimpleText.isEmpty(flag, "");
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getMsg() {
        return XSimpleText.isEmpty(msg, "");

    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public JsonElement getData() {
        return data;
    }

    public void setData(JsonElement data) {
        this.data = data;
    }

    public void addOnErrorListener(errorCodeListener merrorCodeListener) {
        if (merrorCodeListener != null) {
            if (flag != null && flag.equals("success")) {
                merrorCodeListener.codeSuccess(data);
            } else {
                merrorCodeListener.codeError(msg);
            }
        }
    }

    public interface errorCodeListener {
        void codeSuccess(JsonElement data);

        void codeError(String error_description);
    }
}
