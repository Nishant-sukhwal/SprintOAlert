package com.example.kiwi.tpprogresstracker.database;

/**
 * Created by kiwi on 10/18/2016.
 */

public final class DBTableStructure {

    public final class ActionItemsTable
    {
        //Table Name
        public static final String TABLE_NAME = "action_items";

        //Column Name
        public static final String KEY_ID = "id";
        public static final String KEY_PROJECT_ID = "project_id";
        public static final String KEY_DAY = "day";
        public static final String KEY_ITEM_ID = "item_id";
        public static final String KEY_ITEM = "item";
        public static final String KEY_ITEM_TYPE = "item_type";
        public static final String KEY_IS_CURRENT = "is_current";
    }
}
