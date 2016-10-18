package com.example.kiwi.tpprogresstracker.model;

import java.util.ArrayList;

/**
 * Created by kiwi on 10/10/2016.
 */

public class ActionItemInfo {
    private static ActionItemInfo instance = new ActionItemInfo();
    public static ArrayList<InnerActionItems> actionItems = new ArrayList<>();
    public static ArrayList<ListItem> actionListItems = new ArrayList<>();

    public static ActionItemInfo getInstance() {
        if (instance == null) {
            instance = new ActionItemInfo();
        }
        return instance;
    }

    private ActionItemInfo() {

    }

    public ArrayList<InnerActionItems> getActionItems() {
        return actionItems;
    }

    public ArrayList<ListItem> getActionListItems() {
        return actionListItems;
    }
}
