package com.property.model;

import com.property.base.BaseModel;

public class StatisticsModel extends BaseModel {
    ImageModel icon;
    String title;
    int this_month;
    int three_month;
    int this_year;

    public ImageModel getIcon() {
        if (icon == null)
            icon = new ImageModel();
        return icon;
    }

    public void setIcon(ImageModel icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getThis_month() {
        return this_month;
    }

    public void setThis_month(int this_month) {
        this.this_month = this_month;
    }

    public int getThree_month() {
        return three_month;
    }
    public void setThree_month(int three_month) {
        this.three_month = three_month;
    }

    public int getThis_year() {
        return this_year;
    }

    public void setThis_year(int this_year) {
        this.this_year = this_year;
    }
}
