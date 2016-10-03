package com.example.kiwi.tpprogresstracker.model.iteration;

import com.example.kiwi.tpprogresstracker.model.m_project.Items;

/**
 * Created by kiwi on 9/26/2016.
 */
public class Iterations {
    private String Next;
    private Items[] Items;

    public String getNext() {
        return Next;
    }

    public void setNext(String next) {
        Next = next;
    }

    public Items[] getItems() {
        return Items;
    }

    public void setItems(Items[] items) {
        Items = items;
    }
}
