package com.example.kiwi.tpprogresstracker.model.m_project;

/**
 * Created by kiwi on 9/23/2016.
 */
public class Project {
    private Items[] Items;
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

    public Items[] getItems() {
        return Items;
    }

    public void setItems(Items[] Items) {
        this.Items = Items;
    }
}
