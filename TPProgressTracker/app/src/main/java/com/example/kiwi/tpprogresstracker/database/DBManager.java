package com.example.kiwi.tpprogresstracker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by kiwi on 10/18/2016.
 */

public final class DBManager {
    private DBHelper mDBHelper;
    private Context context;
    private static DBManager instance;

    public static DBManager getInstance(Context context) {
        if (instance == null) {
            instance = new DBManager(context);
        }
        return instance;
    }

    private DBManager(Context context) {
        this.context = context;
        mDBHelper = DBHelper.getInstance(context);
    }

    public long insert(ContentValues values) {
        SQLiteDatabase sqlDB = mDBHelper.getWritableDatabase();
        long inserted = 0;
        inserted = sqlDB.insert(DBTableStructure.ActionItemsTable.TABLE_NAME, null, values);
        return inserted;
    }

    public Cursor fetchData(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        try {
            Cursor cursor = null;
            SQLiteDatabase sqlDB = mDBHelper.getWritableDatabase();
            cursor = sqlDB.query(DBTableStructure.ActionItemsTable.TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy);
            while (cursor.moveToNext()) {
                String strName = cursor.getString(1);
            }
            return cursor;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
