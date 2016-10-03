package com.example.kiwi.tpprogresstracker.model.m_project;

/**
 * Created by kiwi on 9/26/2016.
 */
public class Release {
    private String ResourceType;
    private String Id;
    private String Name;

    public String getResourceType() {
        return ResourceType;
    }

    public void setResourceType(String resourceType) {
        ResourceType = resourceType;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
