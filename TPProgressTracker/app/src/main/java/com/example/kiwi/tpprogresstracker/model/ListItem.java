package com.example.kiwi.tpprogresstracker.model;

/**
 * Created by kiwi on 10/17/2016.
 */

public abstract class ListItem {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_CHILD_ITEM = 1;

    abstract public int getType();
}
