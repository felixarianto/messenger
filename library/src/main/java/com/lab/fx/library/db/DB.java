package com.lab.fx.library.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.lab.fx.library.model.Holder;

import java.util.ArrayList;

public abstract class DB {
	
	private static final String TAG = "DB";
    
	private static SQLiteOpenHelper mHelper;
	private static SQLiteDatabase mSQLite;
	private static DatabaseListener mDatabaseListener;
	private static int mVersion;
	private static boolean mCreated;

	private static final Object SYNC = new Object();
	public static final void create(Context p_context) {
		synchronized (SYNC) {
			if (!mCreated) {
    			mHelper = new SQLiteOpenHelper(p_context, TAG, null, mVersion) {
    				@Override
    				public void onCreate(SQLiteDatabase p_sqlite) {
    					if (mDatabaseListener != null) {
    						mDatabaseListener.onCreate(p_sqlite);
    					}
    				}
    				@Override
    				public void onUpgrade(SQLiteDatabase p_sqlite, int p_old, int p_new) {
    					if (mDatabaseListener != null) {
    						mDatabaseListener.onUpgrade(p_sqlite, p_old, p_new);
    					}
    				}
    			};
    			mSQLite = mHelper.getWritableDatabase();
    			mCreated = true;
    		}
		}
	}
	
	public static final void destroy() {
		synchronized (SYNC) {
			mCreated = false;
			if (mHelper != null) try { mHelper.close(); mHelper = null; } catch (Exception e) { }
			if (mSQLite != null) try { mSQLite.close(); mSQLite = null; } catch (Exception e) { }
		}
	}
	
	public static final void recreate(Context p_context) {
		DB.destroy();
		DB.create(p_context);
	}
	
	public static final boolean isCreated() {
		return mCreated;
	}

	public static final void setVersion(int p_version) {
		mVersion = p_version;
	}
	
	public static final int getVersion() {
		return mVersion;
	}
	
	public static final void setDatabaseListener(DatabaseListener p_listener) {
		mDatabaseListener = p_listener;
	}
	
	public static final DatabaseListener getDatabaseListener() {
		return mDatabaseListener;
	}


	public final static Cursor fetch(String p_table, String[] p_fields, String p_where, String p_groupBy, String p_orderBy, String p_limit) {
    	synchronized (SYNC) {
	    	Cursor cursor = null;
	    	try {
	    		cursor = mSQLite.query(p_table, p_fields, p_where, null, p_groupBy, null, p_orderBy, p_limit);
	    	} catch (Exception e) {
		    	Log.e(TAG, Log.getStackTraceString(e));
		    }
	        return cursor;	
    	}
	}

    public final static int truncate(String p_table){
        synchronized (SYNC) {
            int truncate = -1;
            try {
                truncate = mSQLite.delete(p_table, null, null);
            } catch (Exception e) {
            }
            return truncate;
        }
    }

	public final static int delete(String p_table, String p_where) {
    	synchronized (SYNC) {
			int delete = -1;
	    	try {
		    	delete = mSQLite.delete(p_table, p_where, null);
		    } catch (Exception e) {
		    	Log.e(TAG, Log.getStackTraceString(e));
		    }
	    	return delete;
    	}
	}

	public final static int update(String p_table, ContentValues p_values, String p_where) {
    	synchronized (SYNC) {
    		int update = -1;
	    	if (p_values != null) {
		    	try {
		    		update = mSQLite.update(p_table, p_values, p_where, null);
		    	} catch (SQLiteConstraintException sqlce) {
		    		Log.w(TAG, sqlce.getMessage());
		    	} catch (Exception e) {
		    		Log.e(TAG, Log.getStackTraceString(e));
		    	}
	    	}
	    	return update;
    	}
	}

    public static long[] insertTransaction(String p_table, ContentValues[] p_values, boolean p_replace) {
    	synchronized (SYNC) {
	    	int length    = p_values == null ? 0 : p_values.length;
			long[] result = new long[length];
			if (p_values != null) {
				int index = 0;
	    		try {
	    			mSQLite.beginTransaction();
		    		while (index < p_values.length) {
		    			long insert = -1;	    		
		    			try {
		    				insert = mSQLite.insertWithOnConflict(p_table, null, p_values[index], p_replace ? SQLiteDatabase.CONFLICT_REPLACE : SQLiteDatabase.CONFLICT_ABORT);
		    			} catch (SQLiteConstraintException sqlce) {
				    		Log.w(TAG, sqlce.getMessage());
				    	} catch (Exception e) {
		    	    		Log.e(TAG, Log.getStackTraceString(e));
		    	    	}
		    			result[index++] = insert;
					}
		    		mSQLite.setTransactionSuccessful();
		    		mSQLite.endTransaction();
		    	} catch (Exception e) {
		    		Log.e(TAG, Log.getStackTraceString(e));
		    	}
	    		for (int x = index; x < p_values.length; x++) {
	    			result[x] = -1;
	    		}
	    	}
	    	return result;
    	}
    }

    public final static long insert(String p_table, ContentValues p_values, boolean p_replace) {
    	synchronized (SYNC) {
    		long insert = -1;
	    	if (p_values != null) {
		    	try {
		    		insert = mSQLite.insertWithOnConflict(p_table, null, p_values, p_replace ? SQLiteDatabase.CONFLICT_REPLACE : SQLiteDatabase.CONFLICT_ABORT);
		    	} catch (SQLiteConstraintException sqlce) {
		    		Log.w(TAG, sqlce.getMessage());
		    	} catch (Exception e) {
		    		Log.e(TAG, Log.getStackTraceString(e));
		    	}
	    	}
	    	return insert;
    	}
    }

	public final static long insert(String p_table, ContentValues p_values) {
    	synchronized (SYNC) {
	    	long insert = -1;
	    	if (p_values != null) {
		    	try {
		    		insert = mSQLite.insertOrThrow(p_table, null, p_values);
		    	} catch (SQLiteConstraintException sqlce) {
		    		Log.w(TAG, sqlce.getMessage());
		    		insert = -2;
		    	} catch (Exception e) {
		    		Log.e(TAG, Log.getStackTraceString(e));
		    	}
	    	}
	    	return insert;
    	}
    }    

    public static final Cursor rawQuery(String p_query) {
    	synchronized (SYNC) {
		  	Cursor cursor = null;
		  	try {
		  		cursor = mSQLite.rawQuery(p_query, null);
		  	} catch (Exception e) {
		  		Log.e(TAG, Log.getStackTraceString(e));
		  	}
		  	return cursor;
    	}
	}

	public static final boolean execSQL(String p_query) {
		synchronized (SYNC) {
			boolean result = false;
			try {
				mSQLite.execSQL(p_query);
				result = true;
			} catch (Exception e) {
				Log.e(TAG, Log.getStackTraceString(e));
			}
			return result;
		}
	}

	public static interface DatabaseListener {
		
		void onCreate(SQLiteDatabase p_sqlite);
		void onUpgrade(SQLiteDatabase p_sqlite, int p_old, int p_new);
		
	}

}