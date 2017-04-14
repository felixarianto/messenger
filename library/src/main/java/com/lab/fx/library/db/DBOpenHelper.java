package com.lab.fx.library.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

	public static final String TAG = "DBOpenHelper";

    protected DBOpenHelper(Context p_context, String p_path, String p_name, int p_version) {
    	super (p_context, p_name, null, p_version);
    }

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}