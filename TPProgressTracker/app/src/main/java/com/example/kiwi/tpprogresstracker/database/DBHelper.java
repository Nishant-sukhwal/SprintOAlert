package com.example.kiwi.tpprogresstracker.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by kiwi on 10/18/2016.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "dbSprintOAlert";
    private static final int DB_VERSION = 1;
    private Context mContext;
    private static DBHelper instance;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mContext = context;
    }

    public static DBHelper getInstance(Context context) {
        if (instance == null || !context.equals(instance.mContext)) {
            instance = new DBHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DBQueryStructure.CREATE_ACTION_ITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.d(TAG, "onUpgrade: ");
    }
}
