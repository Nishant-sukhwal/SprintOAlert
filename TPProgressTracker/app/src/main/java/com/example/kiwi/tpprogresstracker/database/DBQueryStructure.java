package com.example.kiwi.tpprogresstracker.database;

/**
 * Created by kiwi on 10/18/2016.
 */

public class DBQueryStructure {

    public final static String CREATE_ACTION_ITEMS_TABLE = "CREATE TABLE "
            + DBTableStructure.ActionItemsTable.TABLE_NAME + "("
            + DBTableStructure.ActionItemsTable.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DBTableStructure.ActionItemsTable.KEY_PROJECT_ID + " TEXT,"
            + DBTableStructure.ActionItemsTable.KEY_DAY + " INTEGER,"
            + DBTableStructure.ActionItemsTable.KEY_ITEM_ID + " INTEGER,"
            + DBTableStructure.ActionItemsTable.KEY_ITEM + " TEXT,"
            + DBTableStructure.ActionItemsTable.KEY_ITEM_TYPE + " INTEGER,"
            + DBTableStructure.ActionItemsTable.KEY_IS_CURRENT + " INTEGER DEFAULT 0);";
}
