package com.example.kiwi.tpprogresstracker.classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.kiwi.tpprogresstracker.database.DBManager;
import com.example.kiwi.tpprogresstracker.database.DBTableStructure;
import com.example.kiwi.tpprogresstracker.model.HeaderItem;
import com.example.kiwi.tpprogresstracker.model.InnerActionItems;
import com.example.kiwi.tpprogresstracker.model.ListItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by kiwi on 10/24/2016.
 */

public class ActionItems {

    Context mContext;
    String mCurrentDay, mProjectID;

    public ActionItems(Context context, String projectID, String currentDay) {
        mContext = context;
        mCurrentDay = currentDay;
        mProjectID = projectID;
    }

    public ArrayList<InnerActionItems> actionItems = new ArrayList<>();
    public ArrayList<ListItem> actionListItems = new ArrayList<>();

    public ArrayList<ListItem> getActionItems() {
        String[] args = new String[]{mProjectID};
        Cursor cursor = DBManager.getInstance(mContext).fetchData(null, DBTableStructure.ActionItemsTable.KEY_PROJECT_ID + "=?", args, null, null, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    String day = cursor.getString(cursor.getColumnIndex("day"));
                    String item = cursor.getString(cursor.getColumnIndex("item"));
                    String item_id = cursor.getString(cursor.getColumnIndex("item_id"));
                    String is_current = cursor.getString(cursor.getColumnIndex("is_current"));
                    boolean isCurrent = false;
                    if (is_current.equals("1")) {
                        isCurrent = true;
                    }
                    InnerActionItems innerItems = new InnerActionItems(item_id, Integer.parseInt(day), item, isCurrent, mProjectID);
                    actionItems.add(innerItems);
                    // do what ever you want here
                    cursor.moveToNext();
                }
            }
            cursor.close();
        } else {
            for (int i = 1; i <= Integer.parseInt(mCurrentDay); i++) {
                if (i == Integer.parseInt(mCurrentDay)) {
                    InnerActionItems innerItems = new InnerActionItems("1", i, "", true, mProjectID);
                    actionItems.add(innerItems);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DBTableStructure.ActionItemsTable.KEY_DAY, i);
                    contentValues.put(DBTableStructure.ActionItemsTable.KEY_ITEM_ID, "1");
                    contentValues.put(DBTableStructure.ActionItemsTable.KEY_ITEM, "");
                    contentValues.put(DBTableStructure.ActionItemsTable.KEY_IS_CURRENT, true);
                    contentValues.put(DBTableStructure.ActionItemsTable.KEY_PROJECT_ID, mProjectID);
                    contentValues.put(DBTableStructure.ActionItemsTable.KEY_ITEM_TYPE, ListItem.TYPE_CHILD_ITEM);
                    long isInserted = DBManager.getInstance(mContext).insert(contentValues);
                    Log.d("Insert Items Current", "onCreate: " + isInserted);
                } else {
                    InnerActionItems innerItems = new InnerActionItems("1", i, "Action to be taken", false, mProjectID);
                    actionItems.add(innerItems);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DBTableStructure.ActionItemsTable.KEY_DAY, i);
                    contentValues.put(DBTableStructure.ActionItemsTable.KEY_ITEM_ID, "1");
                    contentValues.put(DBTableStructure.ActionItemsTable.KEY_ITEM, "Action to be taken");
                    contentValues.put(DBTableStructure.ActionItemsTable.KEY_IS_CURRENT, false);
                    contentValues.put(DBTableStructure.ActionItemsTable.KEY_PROJECT_ID, mProjectID);
                    contentValues.put(DBTableStructure.ActionItemsTable.KEY_ITEM_TYPE, ListItem.TYPE_CHILD_ITEM);
                    long isInserted = DBManager.getInstance(mContext).insert(contentValues);
                    Log.d("Insert Action Items", "onCreate: " + isInserted);
                }
            }
        }

        createConsolidateList();
        return actionListItems;
    }

    private void createConsolidateList() {
        Log.d("Current Time", "CreateConsolidateList: " + System.currentTimeMillis());
        Map<Integer, List<InnerActionItems>> groupedHashMap = groupDataIntoHashMap(actionItems);
        for (int day : groupedHashMap.keySet()) {
            HeaderItem headerItem = new HeaderItem();
            headerItem.setDay(day);
            actionListItems.add(headerItem);
            for (InnerActionItems items : groupedHashMap.get(day)) {
                actionListItems.add(items);
            }
        }
        Log.d("Current Time ENDING TAG", "CreateConsolidateList: " + System.currentTimeMillis());
    }

    private Map<Integer, List<InnerActionItems>> groupDataIntoHashMap(ArrayList<InnerActionItems> listOfPojosOfJsonArray) {
        Map<Integer, List<InnerActionItems>> groupedHashMap = new TreeMap<Integer, List<InnerActionItems>>();
        for (InnerActionItems items : listOfPojosOfJsonArray) {
            int day = items.getDay();
            if (groupedHashMap.containsKey(day)) {
                // The key is already in the HashMap; add the pojo object
                // against the existing key.
                groupedHashMap.get(day).add(items);
            } else {
                // The key is not there in the HashMap; create a new key-value pair
                List<InnerActionItems> list = new ArrayList<>();
                list.add(items);
                groupedHashMap.put(day, list);
            }
        }
        return groupedHashMap;
    }
}
