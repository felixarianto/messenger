package com.lab.fx.library.service;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.lab.fx.library.db.DB;

/**
 * Created by febri on 15/07/17.
 */

public class PrefsDB extends DB{

    public static final String KEY_APP_ADDRESS = "app_address";
    public static final String KEY_APP_PORT = "app_port";

    public static final String TAG = "PREFS";

    public static final String FIELD__ID   = "_id";
    public static final String FIELD_KEY   = "key";
    public static final String FIELD_VALUE = "value";

    public static final String   TABLE_NAME   = "PREFS";
    public static final String[] TABLE_FIELDS  = new String[] { FIELD__ID,
            FIELD_KEY,
            FIELD_VALUE };

    public static final String CREATE = "CREATE TABLE IF NOT EXISTS 'PREFS' (" +
            "'_id' integer PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "'key' text UNIQUE," +
            "'value' text)";

    private PrefsDB() {
        super ();
    }

    public static synchronized boolean put(String p_key, String p_value) {
        boolean result = false;
        ContentValues values = new ContentValues();
        values.put(FIELD_KEY,   p_key);
        values.put(FIELD_VALUE, p_value);
        long insert = insert(TABLE_NAME, values, true);
        if ( insert > 0) {
            result = true;
        }
        return result;
    }

    public static synchronized String get(String p_key, String p_default) {
        String result = null;
        try {
            Cursor cursor = fetch(TABLE_NAME, TABLE_FIELDS, FIELD_KEY + "='" + p_key + "'", null, null, "1");
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    result = cursor.getString(2);
                }
                cursor.close();
            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
        return result == null ? p_default : result;
    }

    public static synchronized int getInt(String p_key, int p_default) {
        int r = p_default;
        try {
            String value = get(p_key, null);
            if (value != null) {
                r = Integer.valueOf(value);
            }
        } catch (Exception e) {}
        return r;
    }


}
