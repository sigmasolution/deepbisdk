package com.pl.deepbisdk.localdata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.pl.deepbisdk.localdata.dao.HitsObject;

import java.util.ArrayList;
import java.util.Calendar;

public class DatabaseAccess {
    private static String HIT_TABLE = "STORED_HITS";

    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;

    /**
     * Private constructor to aboid object creation from outside classes.
     *
     * @param context
     */
    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    /**
     * Open the databases connection.
     */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /**
     * Close the databases connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public ArrayList<HitsObject> getAllHits() {
        try {
            open();
            ArrayList<HitsObject> hitsObjects = new ArrayList<>();

            Cursor cursor = database.rawQuery("select * from STORED_HITS", null);
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    HitsObject ho = new HitsObject();
                    ho.time_create = cursor.getLong(cursor.getColumnIndex("time_create"));
                    ho.json_content = cursor.getString(cursor.getColumnIndex("json_content"));
                    ho.other_data = cursor.getString(cursor.getColumnIndex("other_data"));
                    hitsObjects.add(ho);
                    cursor.moveToNext();
                }
            }
            cursor.close();

            return hitsObjects;
        } finally {
            close();
        }
    }

    public int clearAllStoredHit() {
        try {
            open();
            return database.delete(HIT_TABLE, null, null);
        } finally {
            close();
        }
    }

    public void clearHits(Long ... timeCreated) {
        try {
            open();
            String args = TextUtils.join(", ", timeCreated);
            database.execSQL(String.format("DELETE FROM " + HIT_TABLE + " WHERE time_create IN (%s);", args));
        } finally {
            close();
        }
    }

    public int clearExpiredHits() {
        try {
            open();
            long timeMillis = Calendar.getInstance().getTimeInMillis();
            long timeExpire = timeMillis - (12 * 3600 * 1000); // 12 hours
            return database.delete(HIT_TABLE, "" + "time_create < ?",
                    new String[]{"" + timeExpire});
        } finally {
            close();
        }
    }

    public void insertHit(HitsObject hitsObject) {
        try {
            open();
            ContentValues initialValues = new ContentValues();
            initialValues.put("time_create", hitsObject.time_create);
            initialValues.put("json_content", hitsObject.json_content);
            initialValues.put("other_data", hitsObject.other_data);

            database.insert(HIT_TABLE, null, initialValues);
        } finally {
            close();
        }
    }
}
