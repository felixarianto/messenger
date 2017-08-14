package com.lab.fx.library.service;

import android.Manifest;
import android.app.Activity;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.util.Log;

import com.lab.fx.library.app.App;
import com.lab.fx.library.contact.Mine;
import com.lab.fx.library.contact.PersonDB;
import com.lab.fx.library.data.Message;
import com.lab.fx.library.data.MessageCode;
import com.lab.fx.library.data.MessageKey;
import com.lab.fx.library.db.DB;
import com.lab.fx.library.conversation.MessageDB;
import com.lab.fx.library.conversation.MessageProcessor;

/**
 * Created by febri on 14/04/17.
 */

public class MyServices extends IntentService {

    private static boolean READY   = false;
    private static boolean CREATED = false;
    private static Context CONTEXT = null;

    private static final String TAG = "MyServices";

    public MyServices() {
        super(TAG);
    }
    public static synchronized void create(Context p_context) {
        if (!CREATED) {
            CREATED     = true;
            SMS_CALBACK = new SmsCallback() {

                @Override
                public void sendingStatus(String message_id, int p_result) {
                    try {
                        Message message = new Message();
                        message.put(MessageKey.MESSAGE_CODE, MessageCode.MSG_UPDATE);
                        message.put(MessageKey.MESSAGE_ID,   message_id);
                        message.put(MessageKey.STATUS,       p_result == Activity.RESULT_OK ? MessageDB.STATUS_SENT : MessageDB.STATUS_FAILED);
                        App.process(message);
                    } catch (Exception e) {
                        Log.e(TAG, "", e);
                    }
                }

                @Override
                public void deliveringStatus(String message_id, int p_result) {
                    try {
                        Message message = new Message();
                        message.put(MessageKey.MESSAGE_CODE, MessageCode.MSG_UPDATE);
                        message.put(MessageKey.MESSAGE_ID,   message_id);
                        message.put(MessageKey.RESULT,       p_result == Activity.RESULT_OK ? MessageDB.STATUS_DELIVERED : MessageDB.STATUS_FAILED);
                        App.process(message);
                    } catch (Exception e) {
                        Log.e(TAG, "", e);
                    }
                }
            };
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
            Log.d(this.getClass().getName(), "onCreate");
            CONTEXT = MyServices.this;
            create(CONTEXT);
            db();
            processor();
            observer();
            connection();
            READY = true;
        } catch (Exception e) {
            Log.e(this.getClass().getName(), "onCreate", e);
        }
    }
    private void db() {
        DB.setDatabaseListener(new DB.DatabaseListener() {
            @Override
            public void onCreate(SQLiteDatabase p_sqlite) {
                try {
                    p_sqlite.execSQL(PersonDB.CREATE);
                    p_sqlite.execSQL(MessageDB.CREATE);
                    p_sqlite.execSQL(PrefsDB.CREATE);
                } catch (Exception e) {
                    Log.e(this.getClass().getName(), "onCreate DB", e);
                }
            }

            @Override
            public void onUpgrade(SQLiteDatabase p_sqlite, int p_old, int p_new) {

            }
        });
        DB.setVersion(1);
        DB.create(this);
    }
    /*
     * Socket Service
     */
    private static MyConnector mConnector;
    private void connection() {
        String address = PrefsDB.get(PrefsDB.KEY_APP_ADDRESS, "0.0.0.0");
        int    port    = PrefsDB.getInt(PrefsDB.KEY_APP_PORT, 0);
        mConnector = new MyConnector(address, port);
        mConnector.start();
    }
    public static boolean send(String p_data) {
        if (mConnector != null) {
            return mConnector.write(p_data);
        }
        return false;
    }
    /*
     * MSG Service
     */
    public static void sms(String p_number, final String message_id, final String message_text) {
        String SENT      = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI      = PendingIntent.getBroadcast(CONTEXT, 0, new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(CONTEXT, 0, new Intent(DELIVERED), 0);

        //---when the MSG has been sent---
        CONTEXT.registerReceiver(new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent arg1) {
                try {
                    if (SMS_CALBACK != null) {
                        SMS_CALBACK.sendingStatus(message_id, getResultCode());
                    }
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                }
            }
        }, new IntentFilter(SENT));

        //---when the MSG has been delivered---
        CONTEXT.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                if (SMS_CALBACK != null) {
                    SMS_CALBACK.deliveringStatus(message_id, getResultCode());
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        try {
            if      (p_number.startsWith("+62")) {}
            else if (p_number.startsWith("62"))  {p_number = "+"   + p_number;}
            else if (p_number.startsWith("08"))  {p_number = "+62" + p_number.substring(1);}
            Log.d(TAG, "Sending sms [" + p_number + "] " + message_text);
            sms.sendTextMessage(p_number, null, message_text, sentPI, deliveredPI);
        }
        catch (Exception e) {
            if (SMS_CALBACK != null) {
                SMS_CALBACK.deliveringStatus(message_id, SmsManager.RESULT_ERROR_GENERIC_FAILURE);
            }
        }
    }
    private static SmsCallback SMS_CALBACK = null;
    public static interface SmsCallback {
        public void sendingStatus(String message_id, int p_result);
        public void deliveringStatus(String message_id, int p_result);
    }
    /*
     * App Processor
     */
    private void processor() {
        App.addProcessor(new MessageProcessor());
        App.startProcessor();
    }

    private void observer() {
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
    }

    public static boolean isLogin() {
        return false;
    }

    public final static String[] PERMISSIONS = {
              Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS
            , Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS
            };
    public static boolean isPermissionsGranted(Activity activity) {
        boolean granted = false;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity != null && PERMISSIONS != null) {
            for (String permission : PERMISSIONS) {
                granted = ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
                if (!granted) {
                    break;
                }
            }
        }
        else {
            granted = true;
        }
        return granted;
    }
    public static void requestPermissions(Activity activity, int p_requestID) {
        ActivityCompat.requestPermissions(activity, PERMISSIONS, p_requestID);
    }
}
