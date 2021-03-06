package com.example.kiwi.tpprogresstracker.model;

/**
 * Created by kiwi on 10/12/2016.
 */

public class InnerActionItems extends ListItem {
    public InnerActionItems(String id, int day, String item, boolean isCurrent, String projectID) {
        this.id = id;
        this.day = day;
        this.item = item;
        this.isCurrent = isCurrent;
        this.projectID = projectID;
    }

    private String id;
    private int day;
    private String item;
    private boolean isCurrent;
    private String projectID;

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
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

    @Override
    public int getType() {
        return TYPE_CHILD_ITEM;
    }

    public boolean isCurrent() {
        return isCurrent;
    }

    public void setCurrent(boolean current) {
        isCurrent = current;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }
}
