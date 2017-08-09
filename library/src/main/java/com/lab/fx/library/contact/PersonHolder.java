package com.lab.fx.library.contact;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.lab.fx.library.data.Message;
import com.lab.fx.library.data.MessageKey;
import com.lab.fx.library.model.Holder;

/**
 * Created by febri on 30/07/17.
 */

public class PersonHolder extends Holder {

    public String pin;
    public String first_name;
    public String last_name;
    public String image_id;
    public String email;
    public String phone;

    @Override
    public boolean clone(Cursor p_cursor) {
        boolean cloned = false;
        try {
            _id     = p_cursor.getLong  (p_cursor.getColumnIndex(PersonDB.FIELD_ID));
            pin     = p_cursor.getString(p_cursor.getColumnIndex(PersonDB.FIELD_PIN));
            first_name = p_cursor.getString  (p_cursor.getColumnIndex(PersonDB.FIELD_FIRST_NAME));
            last_name  = p_cursor.getString(p_cursor.getColumnIndex(PersonDB.FIELD_LAST_NAME));
            image_id   = p_cursor.getString(p_cursor.getColumnIndex(PersonDB.FIELD_IMAGE_ID));
            email = p_cursor.getString(p_cursor.getColumnIndex(PersonDB.FIELD_EMAIL));
            phone = p_cursor.getString(p_cursor.getColumnIndex(PersonDB.FIELD_PHONE));

            cloned = true;
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "", e);
        }
        return cloned;
    }

    @Override
    public boolean clone(Message p_message) {
        boolean cloned = false;
        try {
            _id = p_message.getLong  (MessageKey.ID);
            pin = p_message.getString(MessageKey.F_PIN);
            first_name = p_message.getString(MessageKey.NAME);
            image_id   = p_message.getString(MessageKey.IMAGE);
            email = p_message.getString(MessageKey.EMAIL);
            phone = p_message.getString(MessageKey.PHONE);
            cloned = true;
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "", e);
        }
        return cloned;
    }

    @Override
    public ContentValues toValues() {
        ContentValues values = new ContentValues();
        values.put(PersonDB.FIELD_ID, _id == 0 ? null : _id);
        values.put(PersonDB.FIELD_PIN,   pin);
        values.put(PersonDB.FIELD_FIRST_NAME, first_name);
        values.put(PersonDB.FIELD_LAST_NAME, last_name);
        values.put(PersonDB.FIELD_IMAGE_ID, image_id);
        values.put(PersonDB.FIELD_EMAIL, email);
        values.put(PersonDB.FIELD_PHONE, phone);
        return values;
    }
}
