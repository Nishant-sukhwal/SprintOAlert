package com.example.kiwi.tpprogresstracker.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.kiwi.tpprogresstracker.R;
import com.example.kiwi.tpprogresstracker.adapter.ActionItemAdapter;
import com.example.kiwi.tpprogresstracker.database.DBManager;
import com.example.kiwi.tpprogresstracker.database.DBTableStructure;
import com.example.kiwi.tpprogresstracker.model.ActionItemInfo;
import com.example.kiwi.tpprogresstracker.model.HeaderItem;
import com.example.kiwi.tpprogresstracker.model.InnerActionItems;
import com.example.kiwi.tpprogresstracker.model.ListItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.example.kiwi.tpprogresstracker.model.ActionItemInfo.actionItems;
import static com.example.kiwi.tpprogresstracker.model.ActionItemInfo.actionListItems;

public class ToDOList extends AppCompatActivity {

    RecyclerView rvActionItem;
    ActionItemAdapter actionItemAdapter;
    String currentDay, projectID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_dolist);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabPlus);
        rvActionItem = (RecyclerView) findViewById(R.id.rvActionItem);
        actionItems.clear();
        actionListItems.clear();
        Intent intent = getIntent();
        currentDay = intent.getStringExtra("day");
        projectID = intent.getStringExtra("project_id");
        String[] args = new String[]{projectID};
        Cursor cursor = DBManager.getInstance(this).fetchData(null, DBTableStructure.ActionItemsTable.KEY_PROJECT_ID + "=?", args, null, null, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Log.d("Fetch data", "onCreate: " + cursor);
            }
        } else {
            for (int i = 1; i <= Integer.parseInt(currentDay); i++) {
                if (i == Integer.parseInt(currentDay)) {
                    InnerActionItems innerItems = new InnerActionItems("1", i, "", true);
                    ActionItemInfo.actionItems.add(innerItems);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DBTableStructure.ActionItemsTable.KEY_DAY, i);
                    contentValues.put(DBTableStructure.ActionItemsTable.KEY_ITEM_ID, "1");
                    contentValues.put(DBTableStructure.ActionItemsTable.KEY_ITEM, "");
                    contentValues.put(DBTableStructure.ActionItemsTable.KEY_IS_CURRENT, true);
                    contentValues.put(DBTableStructure.ActionItemsTable.KEY_PROJECT_ID, projectID);
                    contentValues.put(DBTableStructure.ActionItemsTable.KEY_ITEM_TYPE, ListItem.TYPE_CHILD_ITEM);
                    long isInserted = DBManager.getInstance(this).insert(contentValues);
                    Log.d("Insert Items Current", "onCreate: " + isInserted);
                } else {
                    InnerActionItems innerItems = new InnerActionItems("1", i, "Action to be taken", false);
                    ActionItemInfo.actionItems.add(innerItems);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DBTableStructure.ActionItemsTable.KEY_DAY, i);
                    contentValues.put(DBTableStructure.ActionItemsTable.KEY_ITEM_ID, "1");
                    contentValues.put(DBTableStructure.ActionItemsTable.KEY_ITEM, "Action to be taken");
                    contentValues.put(DBTableStructure.ActionItemsTable.KEY_IS_CURRENT, false);
                    contentValues.put(DBTableStructure.ActionItemsTable.KEY_PROJECT_ID, projectID);
                    contentValues.put(DBTableStructure.ActionItemsTable.KEY_ITEM_TYPE, ListItem.TYPE_CHILD_ITEM);
                    long isInserted = DBManager.getInstance(this).insert(contentValues);
                    Log.d("Insert Action Items", "onCreate: " + isInserted);
                }
            }
        }
//        for (int i = 1; i <= Integer.parseInt(currentDay); i++) {
//            for (int j = 1; j < 5; j++) {
//                boolean isCurrent = false;
//                if (currentDay.equals(String.valueOf(i))) {
//                    isCurrent = true;
//                }
//                InnerActionItems innerItems = new InnerActionItems(String.valueOf(j), i, "item" + j, isCurrent);
//                ActionItemInfo.actionItems.add(innerItems);
//            }
//        }
        CreateConsolidateList();
        actionItemAdapter = new ActionItemAdapter(this, ActionItemInfo.getInstance().getActionListItems());
        rvActionItem.setAdapter(actionItemAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvActionItem.setLayoutManager(linearLayoutManager);

        actionItemAdapter.notifyDataSetChanged();
        rvActionItem.setClickable(true);
        actionItemAdapter.setOnItemClickListener(new ActionItemAdapter.ActionItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (v.getId() == R.id.imgMinus) {
                    ListItem listItem = ActionItemInfo.actionListItems.get(position - 1);
                    if (listItem.getType() == ListItem.TYPE_HEADER && position == ActionItemInfo.actionListItems.size() - 1) {
                        InnerActionItems innerItems = new InnerActionItems("1", ((HeaderItem) listItem).getDay(), "", true);
                        ActionItemInfo.actionListItems.add(innerItems);
                    }
                    ActionItemInfo.actionListItems.remove(position);
                    actionItemAdapter.notifyDataSetChanged();
                }
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                ListItem listItem = ActionItemInfo.actionListItems.get(ActionItemInfo.actionListItems.size() - 1);
                if (listItem.getType() == ListItem.TYPE_CHILD_ITEM && !((InnerActionItems) listItem).getItem().equals("")) {
                    InnerActionItems innerItems = new InnerActionItems(String.valueOf(Integer.parseInt(((InnerActionItems) listItem).getId()) + 1), ((InnerActionItems) listItem).getDay(), "", true);
                    ActionItemInfo.actionListItems.add(innerItems);
                } else if (listItem.getType() == ListItem.TYPE_CHILD_ITEM && ((InnerActionItems) listItem).getItem().equals("")) {
                    showSnackbarMessageBox("Please fill the previous note.");
                }
                actionItemAdapter.notifyDataSetChanged();
                rvActionItem.getLayoutManager().scrollToPosition(ActionItemInfo.actionListItems.size() - 1);
            }
        });
    }

    private void CreateConsolidateList() {
        Map<Integer, List<InnerActionItems>> groupedHashMap = groupDataIntoHashMap(ActionItemInfo.getInstance().getActionItems());
        for (int day : groupedHashMap.keySet()) {
            HeaderItem headerItem = new HeaderItem();
            headerItem.setDay(day);
            ActionItemInfo.actionListItems.add(headerItem);
            for (InnerActionItems items : groupedHashMap.get(day)) {
                ActionItemInfo.actionListItems.add(items);
            }
        }
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

    private void showSnackbarMessageBox(String message) {
        Snackbar.make(findViewById(R.id.activity_to_dolist), message, Snackbar.LENGTH_SHORT).show();
    }

}
