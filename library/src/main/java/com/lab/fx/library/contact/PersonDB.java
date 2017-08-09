package com.lab.fx.library.contact;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.lab.fx.library.db.DB;
import com.lab.fx.library.db.DBHolder;

import java.util.ArrayList;

/**
 * Created by febri on 13/04/17.
 */

public class PersonDB extends DB {

    public static final String FIELD_ID  = "id";
    public static final String FIELD_PIN = "pin";
    public static final String FIELD_FIRST_NAME = "first_name";
    public static final String FIELD_LAST_NAME  = "last_name";
    public static final String FIELD_IMAGE_ID = "image_id";
    public static final String FIELD_EMAIL = "email";
    public static final String FIELD_PHONE = "phone";

    public static final String TABLE_NAME  = "PERSON";

    public static final String CREATE = "CREATE TABLE IF NOT EXISTS 'PERSON' (" +
            "'id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "'pin' text NOT NULL UNIQUE," +
            "'first_name' text," +
            "'last_name' text," +
            "'image_id' TEXT," +
            "'quote' TEXT," +
            "'connected' TEXT DEFAULT (0)," +
            "'last_update' TEXT," +
            "'phone' text," +
            "'email' text," +
            "'created_date' text)";

    public static synchronized ArrayList<DBHolder> getRecords(String p_where) {
        return getRecords(p_where, FIELD_FIRST_NAME, null);
    }
    public static synchronized ArrayList<DBHolder> getRecords(String p_where, String p_orderBy, String p_limit) {
        String[] fields = new String[]{
                FIELD_ID,
                FIELD_PIN,
                FIELD_FIRST_NAME,
                FIELD_LAST_NAME,
                FIELD_PHONE,
                FIELD_EMAIL,
                FIELD_IMAGE_ID
        };
        return getRecords(fields, p_where, null, p_orderBy, p_limit);
    }
    public static synchronized ArrayList<DBHolder> getRecords(String[] p_fields, String p_where, String p_groupBy, String p_orderBy, String p_limit) {
        ArrayList<DBHolder> records = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = fetch(TABLE_NAME, p_fields, p_where, p_groupBy, p_orderBy, p_limit);
            if (cursor != null) {
                ContentValues cv = new ContentValues();
                while (cursor.moveToNext()) {
                    records.add(new DBHolder(cursor));
                }
            }
        } catch (Exception e) {
            Log.e(TABLE_NAME, Log.getStackTraceString(e));
        }
        if (cursor != null) cursor.close();
        return records;
    }

}
