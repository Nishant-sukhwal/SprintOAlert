package com.example.kiwi.tpprogresstracker.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kiwi on 10/10/2016.
 */

public class ActionItemInfo {
    private static ActionItemInfo instance = new ActionItemInfo();
    public static ArrayList<ActionItems> actionItems = new ArrayList<>();

    public static ActionItemInfo getInstance() {
        if (instance == null) {
            instance = new ActionItemInfo();
        }
        return instance;
    }

    private ActionItemInfo() {

    }

    public ArrayList<ActionItems> getActionItems() {
        return actionItems;
    }
}
