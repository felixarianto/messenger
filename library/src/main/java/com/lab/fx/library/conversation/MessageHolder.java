package com.lab.fx.library.conversation;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.lab.fx.library.data.Message;
import com.lab.fx.library.data.MessageKey;
import com.lab.fx.library.model.Holder;

/**
 * Created by febri on 29/07/17.
 */

public class MessageHolder extends Holder {

    public String message_id;
    public long   created_time;
    public String status;

    public String f_pin;
    public String l_pin;
    public String group;

    public String text;
    public String image;
    public String video;
    public String link;
    public String location;
    public String contact;

    public MessageHolder() {
    }
    public MessageHolder(Cursor p_cursor) {
        clone(p_cursor);
    }

    @Override
    public boolean clone(Cursor p_cursor) {
        boolean cloned = false;
        try {
            _id =          getLong(p_cursor, MessageDB.FIELD_ID);
            message_id   = getString(p_cursor, MessageDB.FIELD_MESSAGE_ID);
            created_time = getLong(p_cursor, MessageDB.FIELD_CREATED_TIME);
            status       = getString(p_cursor, MessageDB.FIELD_STATUS);

            f_pin = getString(p_cursor, MessageDB.FIELD_F_PIN);
            l_pin = getString(p_cursor, MessageDB.FIELD_L_PIN);
            group = getString(p_cursor, MessageDB.FIELD_GROUP_ID);

            text  = getString(p_cursor, MessageDB.FIELD_TEXT);
            image = getString(p_cursor, MessageDB.FIELD_IMAGE);
            video = getString(p_cursor, MessageDB.FIELD_VIDEO);
            link  = getString(p_cursor, MessageDB.FIELD_LINK);
            location = getString(p_cursor, MessageDB.FIELD_LOCATION);
            contact  = getString(p_cursor, MessageDB.FIELD_CONTACT);
            cloned = true;
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "", e);
        }
        return cloned;
    }

    private long getLong(Cursor cursor, String p_column) {
        int i = cursor.getColumnIndex(p_column);
        if (i != -1) {
            return cursor.getLong(i);
        }
        return 0;
    }
    private String getString(Cursor cursor, String p_column) {
        int i = cursor.getColumnIndex(p_column);
        if (i != -1) {
            return cursor.getString(i);
        }
        return null;
    }

    @Override
    public boolean clone(Message p_message) {
        boolean cloned = false;
        try {
            _id          = p_message.getLong  (MessageKey.ID);
            message_id   = p_message.getString(MessageKey.MESSAGE_ID);
            created_time = p_message.getLong  (MessageKey.CREATED_TIME);
            status       = p_message.getString(MessageKey.STATUS);

            f_pin = p_message.getString(MessageKey.F_PIN);
            l_pin = p_message.getString(MessageKey.L_PIN);
            group = p_message.getString(MessageKey.GROUP_ID);

            text  = p_message.getString(MessageKey.TEXT);
            image = p_message.getString(MessageKey.IMAGE);
            video = p_message.getString(MessageKey.VIDEO);
            link  = p_message.getString(MessageKey.LINK);
            location = p_message.getString(MessageKey.LOCATION);
            contact  = p_message.getString(MessageKey.CONTACT);
            cloned = true;
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "", e);
        }
        return cloned;
    }

    @Override
    public ContentValues toValues() {
        ContentValues values = new ContentValues();
        values.put(MessageDB.FIELD_ID, _id == 0 ? null : _id);
        values.put(MessageDB.FIELD_MESSAGE_ID,   message_id);
        values.put(MessageDB.FIELD_CREATED_TIME, created_time);
        values.put(MessageDB.FIELD_STATUS, status);
        values.put(MessageDB.FIELD_F_PIN, f_pin);
        values.put(MessageDB.FIELD_L_PIN, l_pin);
        values.put(MessageDB.FIELD_GROUP_ID, group);
        values.put(MessageDB.FIELD_TEXT,  text);
        values.put(MessageDB.FIELD_IMAGE, image);
        values.put(MessageDB.FIELD_VIDEO, video);
        values.put(MessageDB.FIELD_LINK,  link);
        values.put(MessageDB.FIELD_LOCATION, location);
        values.put(MessageDB.FIELD_CONTACT,  contact);
        return values;
    }

    @Override
    public String toString() {
        return toValues().toString();
    }
}
