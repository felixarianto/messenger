package com.lab.fx.library.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.lab.fx.library.data.Message;

/**
 * Created by febri on 29/07/17.
 */

public abstract class Holder {
    public long _id;
    public abstract boolean clone(Cursor  p_cursor);
    public abstract boolean clone(Message p_cursor);
    public abstract ContentValues toValues();
}
