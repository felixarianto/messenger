package com.lab.fx.library.app;

import android.os.Handler;
import android.util.Log;

import com.lab.fx.library.data.MessageKey;
import com.lab.fx.library.data.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * Created by febri on 22/05/17.
 */

public class App {

    private static String TAG = "App";

    public static final String GLOBAL_PROCESSOR_CODE = "-1";
    /*
     * UI Callback
     */
    private static Handler UI_HANDLER;
    private static ConcurrentHashMap<String, AppUICallback> UI_CALLBACKS = new ConcurrentHashMap();
    public  static void putUICallback(String p_id, AppUICallback p_callback) {
        Log.d(TAG, "putUICallback: " + (p_callback == null ? "EMPTY" : p_callback.getClass().getSimpleName()));
        if (UI_HANDLER == null) {
            setUICallback(p_callback);
        }
        else {
            UI_CALLBACKS.put(p_id, p_callback);
        }
    }
    public  static void setUICallback(AppUICallback p_callback) {
        Log.d(TAG, "setUICallback: " + (p_callback == null ? "EMPTY" : p_callback.getClass().getSimpleName()));
        UI_HANDLER  = null;
        UI_CALLBACKS.clear();
        if (p_callback == null) {
            return;
        }
        UI_CALLBACKS.put("APP", p_callback);
        UI_HANDLER  = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(android.os.Message msg) {
                try {
                    Object[] m = (Object[]) msg.obj;
                    for (AppUICallback callback : UI_CALLBACKS.values()) {
                        Log.d(callback.getClass().getSimpleName(), "incomingData: " + m[0] + "  "  + m[1]);
                        callback.incomingData((String) m[0], m[1]);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                }
                return true;
            }
        });
    }
    public static void postUI(String p_code, Object p_data) {
        try {
            if (UI_HANDLER != null) {
                android.os.Message m = new android.os.Message();
                m.obj  = new Object[]{p_code, p_data};
                UI_HANDLER.sendMessage(m);
            }
            else {
                Log.w(TAG, "NO UI_HANDLER FOUND for " + (p_data == null ? null : p_data.getClass().getSimpleName()));
            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
    }
    /*
     * Processor
     */
    private static HashMap<String, ArrayList<AppProcessor>> PROCESSOR = new HashMap();
    public static void addProcessor(AppProcessor p_processors) {
        try {
            String[] ids = p_processors.getId();
            for (String id : ids) {
                ArrayList<AppProcessor> list = PROCESSOR.get(id);
                if (list == null) {
                    PROCESSOR.put(id, list =  new ArrayList());
                }
                list.add(p_processors);
            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
    }
    private static LinkedBlockingDeque<Message> PROCESS_QUEUE;
    private static Thread PROCESS_THREAD;
    public static void startProcessor() {
        try {
            if (PROCESS_THREAD != null) {
                try {
                    PROCESS_THREAD.interrupt();
                } catch (Exception e) {}
            }
            PROCESS_QUEUE = new LinkedBlockingDeque<>();
            PROCESS_THREAD = new Thread() {
                @Override
                public void run() {
                    long t0;
                    String mcode;
                    Message message;
                    while (!isInterrupted()) {
                        try {
                            message = PROCESS_QUEUE.poll(10, TimeUnit.MINUTES);
                            if (message == null) {
                                try {Thread.sleep(10000);} catch (Exception e){}
                                continue;
                            }
                            t0 = System.currentTimeMillis();
                            Log.d(TAG, "[T1] " + message.toString());
                            mcode = message.getString(MessageKey.MESSAGE_CODE, "");
                            ArrayList<AppProcessor> list = PROCESSOR.get(mcode.split("_", 2)[0]);
                            if (list != null) {
                                for (AppProcessor p : list) {
                                    try {
                                        p.process(mcode, message);
                                    } catch (Exception e) {
                                        Log.e(TAG, "", e);
                                    }
                                }
                            }
                            Log.d(TAG, "[T4] " + (System.currentTimeMillis() - t0) + "ms");
                        } catch (Exception e) {
                            Log.e(TAG, "", e);
                        }
                    }
                }
            };
            PROCESS_THREAD.start();
        } catch (Exception e){
            Log.e(TAG, "", e);
        }
    }
    public static void process(Message p_message) {
        try {
            PROCESS_QUEUE.add(p_message);
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
    }
}
