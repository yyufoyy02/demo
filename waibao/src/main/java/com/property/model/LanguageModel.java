package com.property.model;

import com.property.base.BaseModel;
import com.vk.simpleutil.library.XSimpleText;

/**
 * Created by Administrator on 2015/11/15.
 */
public class LanguageModel extends BaseModel {
    boolean check;
    String shortname;
    String solution;

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    /**
     * 故障短语
     */
    public String getShortname() {
        return XSimpleText.isEmpty(shortname, "");
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    /**
     * 简单解决方法
     */
    public String getSolution() {
        return XSimpleText.isEmpty(solution, "");
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }
}
