package com.example.kiwi.tpprogresstracker.ui;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.kiwi.tpprogresstracker.R;
import com.example.kiwi.tpprogresstracker.adapter.ActionItemAdapter;
import com.example.kiwi.tpprogresstracker.database.DBManager;
import com.example.kiwi.tpprogresstracker.database.DBTableStructure;
import com.example.kiwi.tpprogresstracker.model.HeaderItem;
import com.example.kiwi.tpprogresstracker.model.InnerActionItems;
import com.example.kiwi.tpprogresstracker.model.ListItem;
import com.example.kiwi.tpprogresstracker.receiver.NotificationPublisher;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;
//import static com.example.kiwi.tpprogresstracker.model.ActionItemInfo.actionListItems;

public class ToDOList extends AppCompatActivity {

    private static final String TAG = "AlarmManager Result";
    RecyclerView rvActionItem;
    ActionItemAdapter actionItemAdapter;
    String currentDay, projectID, projectName;
    private ProgressDialog m_ProgressBar;
    FloatingActionButton fab;
    public ArrayList<InnerActionItems> actionItems = new ArrayList<>();
    public ArrayList<ListItem> actionListItems = new ArrayList<>();
    private int mHour, mMinute;
    int time = 0;
    long mSelectedMiliSeconds;
    int mSecond;
    int mMin;
    int mHours;
    boolean isFalse;
    long mFinalValue;
    String itemIDForAlaram, itemDescForAlaram;
    int dayForAlaram;

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
                int index = 0;
                for (int i = 1; i < actionListItems.size(); i++) {
                    ListItem listItem = actionListItems.get(i);
                    if (listItem.getType() == ListItem.TYPE_HEADER) {
                        index = i;
                        break;
                    }
                }
                //Click action
                ListItem listItem;
                if (index == 0) {
                    listItem = actionListItems.get(actionListItems.size() - 1);
                } else {
                    listItem = actionListItems.get(index - 1);
                }
                if (listItem.getType() == ListItem.TYPE_CHILD_ITEM && !((InnerActionItems) listItem).getItem().equals("")) {
                    InnerActionItems innerItems = new InnerActionItems(String.valueOf(Integer.parseInt(((InnerActionItems) listItem).getId()) + 1), ((InnerActionItems) listItem).getDay(), "", true, projectID);
                    if (index == 0) {
                        actionListItems.add(innerItems);
                    } else {
                        actionListItems.add(index, innerItems);
                    }
                    view.requestFocus();
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
                if (index == 0) {
                    rvActionItem.getLayoutManager().scrollToPosition(actionListItems.size() - 1);
                } else {
                    rvActionItem.getLayoutManager().scrollToPosition(index - 1);
                }
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
            projectName = intent.getStringExtra("project_name");
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
//                        boolean hasOneChild = false;
//                        if (actionListItems.size() >= 2 && actionListItems.get(2).getType() == ListItem.TYPE_HEADER))
//                        {
//                            hasOneChild = true;
//                        }
                        int count = 0;
                        for (int i = 1; i < actionListItems.size(); i++) {
                            ListItem result = actionListItems.get(i);
                            if (result.getType() == ListItem.TYPE_CHILD_ITEM) {
                                count = count + 1;
                            } else {
                                break;
                            }
                        }
                        actionListItems.remove(position);
                        if (listItem.getType() == ListItem.TYPE_HEADER && count == 1) {
                            InnerActionItems innerItems = new InnerActionItems("1", ((HeaderItem) listItem).getDay(), "", true, projectID);
                            actionListItems.add(1, innerItems);
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(DBTableStructure.ActionItemsTable.KEY_DAY, day);
                            contentValues.put(DBTableStructure.ActionItemsTable.KEY_ITEM_ID, "1");
                            contentValues.put(DBTableStructure.ActionItemsTable.KEY_ITEM, "");
                            contentValues.put(DBTableStructure.ActionItemsTable.KEY_IS_CURRENT, true);
                            contentValues.put(DBTableStructure.ActionItemsTable.KEY_PROJECT_ID, projectID);
                            contentValues.put(DBTableStructure.ActionItemsTable.KEY_ITEM_TYPE, ListItem.TYPE_CHILD_ITEM);
                            long isInserted = DBManager.getInstance(getApplicationContext()).insert(contentValues);
                        }
                        actionItemAdapter.notifyDataSetChanged();
                    } else if (v.getId() == R.id.imgAlarmClock) {
//                        addReminderInCalendar();
                        itemIDForAlaram = ((InnerActionItems) actionListItems.get(position)).getId();
                        dayForAlaram = ((InnerActionItems) actionListItems.get(position)).getDay();
                        itemDescForAlaram = ((InnerActionItems) actionListItems.get(position)).getItem();
                        final Calendar c = Calendar.getInstance();
                        mHour = c.get(Calendar.HOUR_OF_DAY);
                        mMinute = c.get(Calendar.MINUTE);
                        // Launch Time Picker Dialog
                        TimePickerDialog timePickerDialog = new TimePickerDialog(ToDOList.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                mHour = i;
                                mMinute = i1;
                                int requestCode = Integer.valueOf(dayForAlaram + itemIDForAlaram);
                                convertTimeInMillisecond(i, i1, requestCode, itemDescForAlaram);
                                // textView.setText( currentTime() + " --- " + i + " : " + i1);
                            }
                        }, mHour, mMinute, false);

                        timePickerDialog.show();
                    }
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            for (int i = 1; i < actionListItems.size(); i++) {
                ListItem listItem = actionListItems.get(i);
                if (listItem.getType() == ListItem.TYPE_HEADER) {
                    rvActionItem.getLayoutManager().scrollToPosition(i - 1);
                    break;
                }
            }
            actionItemAdapter.notifyDataSetChanged();
            rvActionItem.setClickable(true);
            m_ProgressBar.dismiss();
        }

    }

    private void convertTimeInMillisecond(final int hour, final int minute, final int requestCode, final String title) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.setTimeZone(TimeZone.getDefault());
        mSelectedMiliSeconds = cal.getTimeInMillis();

        //checking if alram is working with pendingIntent
        Intent intent = new Intent(getApplicationContext(), NotificationPublisher.class);//the same as up
        intent.setData(Uri.parse("custom://" + requestCode));
        intent.putExtra(NotificationPublisher.NOTIFICATION_ID, String.valueOf(requestCode));
        intent.putExtra(NotificationPublisher.NOTIFICATION, getNotification(title, "", "", ""));
        intent.setAction(String.valueOf(requestCode));//the same as up

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //and stopping
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), requestCode, intent, 0);
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent);//important
            //pendingIntent.cancel();
        }

//        Intent alarmIntent = new Intent(ToDOList.this, NotificationPublisher.class);
//        pendingIntent = PendingIntent.getBroadcast(ToDOList.this, requestCode, alarmIntent, 0);
        System.out.println("difference --" + mSelectedMiliSeconds + "---" + System.currentTimeMillis());
        alarmManager.set(AlarmManager.RTC_WAKEUP, mSelectedMiliSeconds, pendingIntent);
        //countDown();
    }

    private Notification getNotification(String bigContentTitle, String day, String bugsNotDone, String storiesNotDone) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
//        inboxStyle.setBigContentTitle(bigContentTitle);
        //inboxStyle.setSummaryText("(line.length) getBigText()");
//        inboxStyle.addLine("day : " + day);
//        inboxStyle.addLine("Bugs not done: " + bugsNotDone);
//        inboxStyle.addLine("stories not done: " + storiesNotDone);
        builder.setContentTitle(projectName)
                .setContentText(bigContentTitle);
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.mipmap.kiwi_logo);
        builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            builder.setStyle(inboxStyle);
        }
//        builder.setContentIntent(pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            builder.setPriority(Notification.PRIORITY_HIGH);
        }
//        builder.setOngoing(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            builder.build();
        }

//        Notification.Builder builder = new Notification.Builder(this);
//        builder.setContentTitle("Scheduled Notification");
//        builder.setContentText(content);
//        builder.setSmallIcon(R.drawable.ic_launcher);
        return builder.build();
    }

    private void countDown() {
        isFalse = true;
        mFinalValue = (mSelectedMiliSeconds - System.currentTimeMillis()) / 1000;
        System.out.println("difference --" + mFinalValue);
        Thread countDown = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isFalse) {
                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mFinalValue != 0) {
                                    mFinalValue = mFinalValue - 1;
                                    //textView.setText("" + mFinalValue);
                                } else {
                                    isFalse = false;
                                }
                            }
                        });
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        countDown.start();
    }

    /**
     * Adds Events and Reminders in Calendar.
     */
    private void addReminderInCalendar() {
        Calendar cal = Calendar.getInstance();
        Uri EVENTS_URI = Uri.parse(getCalendarUriBase(true) + "events");
        ContentResolver cr = getContentResolver();
        TimeZone timeZone = TimeZone.getDefault();

        /** Inserting an event in calendar. */
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.CALENDAR_ID, 1);
        values.put(CalendarContract.Events.TITLE, "Nishant Reminder 01");
        values.put(CalendarContract.Events.DESCRIPTION, "A test Reminder.");
        values.put(CalendarContract.Events.ALL_DAY, 1);
        // event starts at 11 minutes from now
        values.put(CalendarContract.Events.DTSTART, cal.getTimeInMillis() + 20 * 1000);
        // ends 60 minutes from now
        values.put(CalendarContract.Events.DTEND, cal.getTimeInMillis() + 60 * 1000);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
        values.put(CalendarContract.Events.HAS_ALARM, 1);
        Uri event = cr.insert(EVENTS_URI, values);

        // Display event id.
        Toast.makeText(getApplicationContext(), "Event added :: ID :: " + event.getLastPathSegment(), Toast.LENGTH_SHORT).show();

        /** Adding reminder for event added. */
        Uri REMINDERS_URI = Uri.parse(getCalendarUriBase(true) + "reminders");
        values = new ContentValues();
        values.put(CalendarContract.Reminders.EVENT_ID, Long.parseLong(event.getLastPathSegment()));
        values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        values.put(CalendarContract.Reminders.MINUTES, 10);
        cr.insert(REMINDERS_URI, values);
    }

    /**
     * Returns Calendar Base URI, supports both new and old OS.
     */
    private String getCalendarUriBase(boolean eventUri) {
        Uri calendarURI = null;
        try {
            if (android.os.Build.VERSION.SDK_INT <= 7) {
                calendarURI = (eventUri) ? Uri.parse("content://calendar/") : Uri.parse("content://calendar/calendars");
            } else {
                calendarURI = (eventUri) ? Uri.parse("content://com.android.calendar/") : Uri
                        .parse("content://com.android.calendar/calendars");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return calendarURI.toString();
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
        Map<Integer, List<InnerActionItems>> groupedHashMap = new TreeMap<Integer, List<InnerActionItems>>(Collections.<Integer>reverseOrder());
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

    static <K, V extends Comparable<? super V>>
    List<Map.Entry<K, V>> entriesSortedByValues(Map<K, V> map) {

        List<Map.Entry<K, V>> sortedEntries = new ArrayList<Map.Entry<K, V>>(map.entrySet());

        Collections.sort(sortedEntries,
                new Comparator<Map.Entry<K, V>>() {
                    @Override
                    public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
                        return e2.getValue().compareTo(e1.getValue());
                    }
                }
        );

        return sortedEntries;
    }
}
