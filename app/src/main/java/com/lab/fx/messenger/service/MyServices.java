package com.lab.fx.messenger.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.util.Log;

import com.lab.fx.library.db.DB;
import com.lab.fx.messenger.conversation.MessageDB;
import com.lab.fx.messenger.person.PersonDB;

/**
 * Created by febri on 14/04/17.
 */

public class MyServices extends IntentService {

    private static boolean READY   = false;
    private static boolean CREATED = false;

    public MyServices() {
        super("MyServices");
    }

    public static synchronized void create(Context p_context) {
        if (!CREATED) {
            CREATED = true;
            p_context.startService(new Intent(p_context, MyServices.class));
        }
    }

    public final static boolean isReady() {
        return READY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            Db();
            Connection();
            Observer();
            READY = true;
        } catch (Exception e) {
            Log.e(this.getClass().getName(), "onCreate", e);
        }
    }
    private void Db() {
        DB.setDatabaseListener(new DB.DatabaseListener() {
            @Override
            public void onCreate(SQLiteDatabase p_sqlite) {
                p_sqlite.execSQL(PersonDB.CREATE);
                p_sqlite.execSQL(MessageDB.CREATE);
            }

            @Override
            public void onUpgrade(SQLiteDatabase p_sqlite, int p_old, int p_new) {

            }
        });
        DB.setVersion(1);
        DB.create(this);
    }

    private void Connection() {
    }

    private void Observer() {
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
    }
}
