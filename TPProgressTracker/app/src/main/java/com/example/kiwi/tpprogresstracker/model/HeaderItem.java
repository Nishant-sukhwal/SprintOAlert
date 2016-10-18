package com.example.kiwi.tpprogresstracker.model;

/**
 * Created by kiwi on 10/17/2016.
 */

public class HeaderItem extends ListItem {

    private int day;

    @Override
    public int getType() {
        return TYPE_HEADER;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
