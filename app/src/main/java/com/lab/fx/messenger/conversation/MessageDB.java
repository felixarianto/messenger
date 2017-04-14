package com.lab.fx.messenger.conversation;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.lab.fx.library.db.DB;
import com.lab.fx.library.db.DBHolder;

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
    public static final String FIELD_GROUP = "group";

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
            "'group' text," +

            "'text' TEXT," +
            "'image' TEXT," +
            "'video' TEXT," +
            "'link' TEXT," +
            "'location' TEXT," +
            "'contact' TEXT)";

    public static synchronized ArrayList<DBHolder> getRecords(String p_where) {
        return getRecords(p_where, FIELD_CREATED_TIME + " desc ", null);
    }
    public static synchronized ArrayList<DBHolder> getRecords(String p_where, String p_orderBy, String p_limit) {
        String[] fields = new String[]{
                FIELD_ID,
                FIELD_MESSAGE_ID,
                FIELD_CREATED_TIME,
                FIELD_STATUS,
                FIELD_F_PIN,
                FIELD_L_PIN,
                FIELD_GROUP,
                FIELD_TEXT,
                FIELD_IMAGE,
                FIELD_VIDEO,
                FIELD_LINK,
                FIELD_LOCATION,
                FIELD_CONTACT
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
