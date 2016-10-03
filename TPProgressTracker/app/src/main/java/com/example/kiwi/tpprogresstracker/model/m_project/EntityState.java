package com.example.kiwi.tpprogresstracker.model.m_project;

/**
 * Created by kiwi on 9/23/2016.
 */
public class EntityState {
    private String Name;

    private String Id;

    private String NumericPriority;

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getNumericPriority() {
        return NumericPriority;
    }

    public void setNumericPriority(String NumericPriority) {
        this.NumericPriority = NumericPriority;
    }
}