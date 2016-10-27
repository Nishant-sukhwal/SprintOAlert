package com.example.kiwi.tpprogresstracker.ui;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
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
import com.example.kiwi.tpprogresstracker.model.HeaderItem;
import com.example.kiwi.tpprogresstracker.model.InnerActionItems;
import com.example.kiwi.tpprogresstracker.model.ListItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
//import static com.example.kiwi.tpprogresstracker.model.ActionItemInfo.actionListItems;

public class ToDOList extends AppCompatActivity {

    RecyclerView rvActionItem;
    ActionItemAdapter actionItemAdapter;
    String currentDay, projectID;
    private ProgressDialog m_ProgressBar;
    FloatingActionButton fab;
    public ArrayList<InnerActionItems> actionItems = new ArrayList<>();
    public ArrayList<ListItem> actionListItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_dolist);
        fab = (FloatingActionButton) findViewById(R.id.fabPlus);
        rvActionItem = (RecyclerView) findViewById(R.id.rvActionItem);
        actionItems.clear();
        actionListItems.clear();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ToDOList.this);
        rvActionItem.setLayoutManager(linearLayoutManager);
        rvActionItem.setHasFixedSize(true);
        actionItemAdapter = new ActionItemAdapter(ToDOList.this, actionListItems);
        rvActionItem.setAdapter(actionItemAdapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 //Click action
                ListItem listItem = actionListItems.get(actionListItems.size() - 1);
                if (listItem.getType() == ListItem.TYPE_CHILD_ITEM && !((InnerActionItems) listItem).getItem().equals("")) {
                    InnerActionItems innerItems = new InnerActionItems(String.valueOf(Integer.parseInt(((InnerActionItems) listItem).getId()) + 1), ((InnerActionItems) listItem).getDay(), "", true, projectID);
                    actionListItems.add(innerItems);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DBTableStructure.ActionItemsTable.KEY_DAY, ((InnerActionItems) listItem).getDay());
                    contentValues.put(DBTableStructure.ActionItemsTable.KEY_ITEM_ID, String.valueOf(Integer.parseInt(((InnerActionItems) listItem).getId()) + 1));
                    contentValues.put(DBTableStructure.ActionItemsTable.KEY_ITEM, "");
                    contentValues.put(DBTableStructure.ActionItemsTable.KEY_IS_CURRENT, true);
                    contentValues.put(DBTableStructure.ActionItemsTable.KEY_PROJECT_ID, projectID);
                    contentValues.put(DBTableStructure.ActionItemsTable.KEY_ITEM_TYPE, ListItem.TYPE_CHILD_ITEM);
                    long isInserted = DBManager.getInstance(getApplicationContext()).insert(contentValues);
                } else if (listItem.getType() == ListItem.TYPE_CHILD_ITEM && ((InnerActionItems) listItem).getItem().equals("")) {
                    //showSnackbarMessageBox("Please fill the previous note.");
                }
                actionItemAdapter.notifyDataSetChanged();
                rvActionItem.getLayoutManager().scrollToPosition(actionListItems.size() - 1);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        new LongOperation().execute("");
    }

    private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            m_ProgressBar = new ProgressDialog(ToDOList.this, ProgressDialog.STYLE_SPINNER);
            m_ProgressBar.setCancelable(false);
            m_ProgressBar.setMessage("Please wait...");
            m_ProgressBar.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            Intent intent = getIntent();
            currentDay = intent.getStringExtra("day");
            projectID = intent.getStringExtra("project_id");
            String[] args = new String[]{projectID};
            Cursor cursor = DBManager.getInstance(ToDOList.this).fetchData(null, DBTableStructure.ActionItemsTable.KEY_PROJECT_ID + "=?", args, null, null, null);
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
                        InnerActionItems innerItems = new InnerActionItems(item_id, Integer.parseInt(day), item, isCurrent, projectID);
                        actionItems.add(innerItems);
                        // do what ever you want here
                        cursor.moveToNext();
                    }
                }
                cursor.close();
            } else {
                for (int i = 1; i <= Integer.parseInt(currentDay); i++) {
                    if (i == Integer.parseInt(currentDay)) {
                        InnerActionItems innerItems = new InnerActionItems("1", i, "", true, projectID);
                        actionItems.add(innerItems);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(DBTableStructure.ActionItemsTable.KEY_DAY, i);
                        contentValues.put(DBTableStructure.ActionItemsTable.KEY_ITEM_ID, "1");
                        contentValues.put(DBTableStructure.ActionItemsTable.KEY_ITEM, "");
                        contentValues.put(DBTableStructure.ActionItemsTable.KEY_IS_CURRENT, true);
                        contentValues.put(DBTableStructure.ActionItemsTable.KEY_PROJECT_ID, projectID);
                        contentValues.put(DBTableStructure.ActionItemsTable.KEY_ITEM_TYPE, ListItem.TYPE_CHILD_ITEM);
                        long isInserted = DBManager.getInstance(ToDOList.this).insert(contentValues);
                        Log.d("Insert Items Current", "onCreate: " + isInserted);
                    } else {
                        InnerActionItems innerItems = new InnerActionItems("1", i, "Action to be taken", false, projectID);
                        actionItems.add(innerItems);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(DBTableStructure.ActionItemsTable.KEY_DAY, i);
                        contentValues.put(DBTableStructure.ActionItemsTable.KEY_ITEM_ID, "1");
                        contentValues.put(DBTableStructure.ActionItemsTable.KEY_ITEM, "Action to be taken");
                        contentValues.put(DBTableStructure.ActionItemsTable.KEY_IS_CURRENT, false);
                        contentValues.put(DBTableStructure.ActionItemsTable.KEY_PROJECT_ID, projectID);
                        contentValues.put(DBTableStructure.ActionItemsTable.KEY_ITEM_TYPE, ListItem.TYPE_CHILD_ITEM);
                        long isInserted = DBManager.getInstance(ToDOList.this).insert(contentValues);
                        Log.d("Insert Action Items", "onCreate: " + isInserted);
                    }
                }
            }
            CreateConsolidateList();

            actionItemAdapter.setOnItemClickListener(new ActionItemAdapter.ActionItemClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    if (v.getId() == R.id.imgMinus) {
                        String itemID = ((InnerActionItems) actionListItems.get(position)).getId();
                        int day = ((InnerActionItems) actionListItems.get(position)).getDay();
                        String whereClause = DBTableStructure.ActionItemsTable.KEY_PROJECT_ID + "=? AND " + DBTableStructure.ActionItemsTable.KEY_ITEM_ID + "=? AND " + DBTableStructure.ActionItemsTable.KEY_DAY + "=?";
                        long deleted = DBManager.getInstance(getApplicationContext()).delete(whereClause, new String[]{projectID, itemID, String.valueOf(day)});
                        ListItem listItem = actionListItems.get(position - 1);
                        if (listItem.getType() == ListItem.TYPE_HEADER && position == actionListItems.size() - 1) {
                            InnerActionItems innerItems = new InnerActionItems("1", ((HeaderItem) listItem).getDay(), "", true, projectID);
                            actionListItems.add(innerItems);
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(DBTableStructure.ActionItemsTable.KEY_DAY, day);
                            contentValues.put(DBTableStructure.ActionItemsTable.KEY_ITEM_ID, "1");
                            contentValues.put(DBTableStructure.ActionItemsTable.KEY_ITEM, "");
                            contentValues.put(DBTableStructure.ActionItemsTable.KEY_IS_CURRENT, true);
                            contentValues.put(DBTableStructure.ActionItemsTable.KEY_PROJECT_ID, projectID);
                            contentValues.put(DBTableStructure.ActionItemsTable.KEY_ITEM_TYPE, ListItem.TYPE_CHILD_ITEM);
                            long isInserted = DBManager.getInstance(getApplicationContext()).insert(contentValues);
                        }
                        actionListItems.remove(position);
                        actionItemAdapter.notifyDataSetChanged();
                    }
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            rvActionItem.getLayoutManager().scrollToPosition(actionListItems.size() - 1);
            actionItemAdapter.notifyDataSetChanged();
            rvActionItem.setClickable(true);
            m_ProgressBar.dismiss();
        }

    }

    private void CreateConsolidateList() {
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

    static <K,V extends Comparable<? super V>>
    List<Map.Entry<K, V>> entriesSortedByValues(Map<K,V> map) {

        List<Map.Entry<K,V>> sortedEntries = new ArrayList<Map.Entry<K,V>>(map.entrySet());

        Collections.sort(sortedEntries,
                new Comparator<Map.Entry<K,V>>() {
                    @Override
                    public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
                        return e2.getValue().compareTo(e1.getValue());
                    }
                }
        );

        return sortedEntries;
    }

    private void showSnackbarMessageBox(String message) {
        Snackbar.make(findViewById(R.id.activity_to_dolist), message, Snackbar.LENGTH_SHORT).show();
    }

}
