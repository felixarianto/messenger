package com.lab.fx.library.db;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.SimpleCursorAdapter;

import java.util.HashMap;

/**
 * Created by febri on 13/04/17.
 */

public class DBHolder extends HashMap<String, Object> {
    public DBHolder() {
    }
    public DBHolder(Cursor p_cursor) {
        int count = p_cursor.getColumnCount();
        for (int i = 0; i < count; i++) {
            if (p_cursor.getType(i) == Cursor.FIELD_TYPE_INTEGER) {
                put(p_cursor.getColumnName(i), p_cursor.getInt(i));
            }
            else if (p_cursor.getType(i) == Cursor.FIELD_TYPE_FLOAT){
                put(p_cursor.getColumnName(i), p_cursor.getDouble(i));
            }
            else {
                put(p_cursor.getColumnName(i), p_cursor.getString(i));
            }
        }
    }

    public final String getString(String... p_key_default) {
        try {
            return  (String) get(p_key_default[0]);
        } catch (Exception e) {
            Log.d(this.getClass().getName(), "getString",  e);
        }
        return p_key_default.length > 1 ? p_key_default[0] : null;
    }

    public final double getDouble(String p_key, double p_default) {
        try {
            return (double) get(p_key);
        } catch (Exception e) {
            Log.d(this.getClass().getName(), "getDouble",  e);
        }
        return p_default;
    }

    public final int getInt(String p_key, int p_default) {
        try {
            return (int) get(p_key);
        } catch (Exception e) {
            Log.d(this.getClass().getName(), "getInt",  e);
        }
        return p_default;
    }
}
