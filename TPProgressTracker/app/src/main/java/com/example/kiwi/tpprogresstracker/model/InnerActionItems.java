package com.example.kiwi.tpprogresstracker.model;

/**
 * Created by kiwi on 10/12/2016.
 */

public class InnerActionItems {
    public InnerActionItems(String id, String day, String item) {
        this.id = id;
        this.day = day;
        this.item = item;
    }

    private String id;
    private String day;
    private String item;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
