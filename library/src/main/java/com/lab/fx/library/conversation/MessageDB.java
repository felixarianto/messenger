package com.lab.fx.library.conversation;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.lab.fx.library.db.DB;

import java.util.ArrayList;

/**
 * Created by febri on 15/04/17.
 */

public class MessageDB extends DB {

    public static final String FIELD_ID  = "id";
    public static final String FIELD_MESSAGE_ID  = "message_id";
    public static final String FIELD_CREATED_TIME  = "created_time";
    public static final String FIELD_STATUS     = "status";

    public static final String FIELD_F_PIN = "f_pin";
    public static final String FIELD_L_PIN = "l_pin";
    public static final String FIELD_GROUP_ID = "group_id";

    public static final String FIELD_TEXT  = "text";
    public static final String FIELD_IMAGE = "image";
    public static final String FIELD_VIDEO = "video";
    public static final String FIELD_LINK  = "link";
    public static final String FIELD_LOCATION = "location";
    public static final String FIELD_CONTACT  = "contact";

    public static final String TABLE_NAME  = "MESSAGE";

    public static final String CREATE = "CREATE TABLE IF NOT EXISTS 'MESSAGE' (" +
            "'id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "'message_id' TEXT," +
            "'created_time' TEXT," +
            "'status' TEXT," +

            "'f_pin' text," +
            "'l_pin' text," +
            "'group_id' text," +

            "'text' TEXT," +
            "'image' TEXT," +
            "'video' TEXT," +
            "'link' TEXT," +
            "'location' TEXT," +
            "'contact' TEXT)";

    public static final String STATUS_WAITING   = "w";
    public static final String STATUS_SENT      = "s";
    public static final String STATUS_DELIVERED = "d";
    public static final String STATUS_READ      = "r";
    public static final String STATUS_FAILED    = "f";

    public static synchronized MessageHolder getRecord(String p_where) {
        ArrayList<MessageHolder> m = getRecords(p_where, null, "1");
        if (!m.isEmpty()) {
            return m.get(0);
        }
        return null;
    }
    public static synchronized ArrayList<MessageHolder> getRecords(String p_where) {
        return getRecords(p_where, FIELD_CREATED_TIME + " desc ", null);
    }
    public static synchronized ArrayList<MessageHolder> getRecords(String p_where, String p_orderBy, String p_limit) {
        String[] fields = new String[]{
                FIELD_ID,
                FIELD_MESSAGE_ID,
                FIELD_CREATED_TIME,
                FIELD_STATUS,
                FIELD_F_PIN,
                FIELD_L_PIN,
                FIELD_GROUP_ID,
                FIELD_TEXT,
                FIELD_IMAGE,
                FIELD_VIDEO,
                FIELD_LINK,
                FIELD_LOCATION,
                FIELD_CONTACT
        };
        return getRecords(fields, p_where, null, p_orderBy, p_limit);
    }
    public static synchronized ArrayList<MessageHolder> getRecords(String[] p_fields, String p_where, String p_groupBy, String p_orderBy, String p_limit) {
        ArrayList<MessageHolder> records = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = fetch(TABLE_NAME, p_fields, p_where, p_groupBy, p_orderBy, p_limit);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    records.add(new MessageHolder(cursor));
                }
            }
        } catch (Exception e) {
            Log.e(TABLE_NAME, Log.getStackTraceString(e));
        }
        if (cursor != null) cursor.close();
        return records;
    }

    public static int update(ContentValues p_values, String p_where) {
        return update(TABLE_NAME, p_values, p_where);
    }

    public static long insert(ContentValues p_values) {
        return insert(TABLE_NAME, p_values, true);
    }

    public static int delete(String p_where) {
        return delete(TABLE_NAME, p_where);
    }

    public static int count(String p_where) {
        int count = 0;
        Cursor cursor;
        if (p_where != null) {
            cursor = rawQuery("select COUNT(*) from " + TABLE_NAME + " where " + p_where);
        }
        else {
            cursor = rawQuery("select COUNT(*) from " + TABLE_NAME);
        }
        if (cursor != null) {
            if (cursor.moveToNext()) {
                count = cursor.getInt(0);
            }
        }
        return count;
    }



}
