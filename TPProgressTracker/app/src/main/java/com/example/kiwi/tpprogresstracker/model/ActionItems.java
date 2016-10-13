package com.example.kiwi.tpprogresstracker.model;

import java.util.List;

/**
 * Created by kiwi on 10/10/2016.
 */

public class ActionItems {
    private String day;
    private boolean isCurrent;
    private List<InnerActionItems> items;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public boolean isCurrent() {
        return isCurrent;
    }

    public void setCurrent(boolean current) {
        isCurrent = current;
    }

    public List<InnerActionItems> getItems() {
        return items;
    }

    public void setItems(List<InnerActionItems> items) {
        this.items = items;
    }

}
