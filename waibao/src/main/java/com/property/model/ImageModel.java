package com.property.model;

import com.property.base.BaseModel;
import com.vk.simpleutil.library.XSimpleText;

public class ImageModel extends BaseModel {
    String url;
    String code;
    public ImageModel() {
    }

    public String getCode() {
        return XSimpleText.isEmpty(code,"");
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ImageModel(String url) {
        this.url = url;
    }

    public String getUrl() {
        return XSimpleText.isEmpty(url, "");
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
