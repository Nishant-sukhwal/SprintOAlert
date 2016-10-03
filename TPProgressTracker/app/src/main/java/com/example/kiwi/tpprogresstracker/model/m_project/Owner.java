package com.example.kiwi.tpprogresstracker.model.m_project;

/**
 * Created by kiwi on 9/23/2016.
 */
public class Owner {
    private String Login;

    private String ResourceType;

    private String FirstName;

    private String Id;

    private String LastName;

    public String getLogin ()
    {
        return Login;
    }

    public void setLogin (String Login)
    {
        this.Login = Login;
    }

    public String getResourceType ()
    {
        return ResourceType;
    }

    public void setResourceType (String ResourceType)
    {
        this.ResourceType = ResourceType;
    }

    public String getFirstName ()
    {
        return FirstName;
    }

    public void setFirstName (String FirstName)
    {
        this.FirstName = FirstName;
    }

    public String getId ()
    {
        return Id;
    }

    public void setId (String Id)
    {
        this.Id = Id;
    }

    public String getLastName ()
    {
        return LastName;
    }

    public void setLastName (String LastName)
    {
        this.LastName = LastName;
    }
}
