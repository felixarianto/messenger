package com.lab.fx.messenger.sender;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.lab.fx.library.db.DB;

import java.util.ArrayList;

/**
 * Created by febri on 15/07/17.
 */

public class OutgoingDB extends DB {

    public static final String FIELD__ID   = "_id";
    public static final String FIELD_MESSAGE_ID   = "message_id";
    public static final String FIELD_MESSAGE_TEXT = "message_text";
    public static final String FIELD_STATUS = "status";

    public static final String   TABLE_NAME   = "OUTGOING";
    public static final String[] TABLE_FIELDS  = new String[] { FIELD__ID,
            FIELD_MESSAGE_ID,
            FIELD_MESSAGE_TEXT,
            FIELD_STATUS};

    public static final String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS 'OUTGOING' (" +
            "'_id' integer PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "'message_id' text UNIQUE," +
            "'message_text' text," +
            "'status' text" +
            ")";

    private OutgoingDB() {
        super ();
    }

    public static synchronized boolean put(String p_id, String p_text) {
        boolean result = false;
        ContentValues values = new ContentValues();
        values.put(FIELD_MESSAGE_ID,   p_id);
        values.put(FIELD_MESSAGE_TEXT, p_text);
        long insert = insert(TABLE_NAME, values, true);
        if ( insert > 0) {
            result = true;
        }
        return result;
    }

    public static synchronized int remove(String p_id) {
        return delete(TABLE_NAME, FIELD_MESSAGE_ID + "='" + p_id + "'");
    }
    public static synchronized ArrayList<String[]> gets() {
        ArrayList<String[]> result = new ArrayList<>();
        try {
            Cursor cursor = fetch(TABLE_NAME, TABLE_FIELDS, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    result.add(new String[] {cursor.getString(1), cursor.getString(2)});
                }
                cursor.close();
            }
        } catch (Exception e) {
            Log.e(TABLE_NAME, "", e);
        }
        return result;
    }

}
