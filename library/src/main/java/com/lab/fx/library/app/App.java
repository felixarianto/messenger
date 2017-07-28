package com.lab.fx.library.app;

import android.os.Handler;
import android.util.Log;

import com.lab.fx.library.data.MessageKey;
import com.lab.fx.library.data.Message;

import java.util.ArrayList;
import java.util.HashMap;
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
    private static Handler       UI_HANDLER;
    private static AppUICallback UI_CALLBACK;
    public  static void setUICallback(AppUICallback p_callback) {
        Log.d(TAG, "setUICallback: " + (p_callback == null ? "EMPTY" : p_callback.getClass().getSimpleName()));
        if (p_callback == null) {
            UI_HANDLER  = null;
            return;
        }
        UI_CALLBACK = p_callback;
        UI_HANDLER  = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(android.os.Message msg) {
                try {
                    UI_CALLBACK.incomingData(msg.what, msg.obj);
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                }
                return true;
            }
        });
    }
    public static void postUI(int p_push_id, Object p_data) {
        try {
            if (UI_HANDLER != null) {
                android.os.Message m = new android.os.Message();
                m.what = p_push_id;
                m.obj  = p_data;
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
            String[] ids = p_processors.getCode();
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
                    String mcode;
                    Message message;
                    while (!isInterrupted()) {
                        try {
                            message = PROCESS_QUEUE.poll(10, TimeUnit.MINUTES);
                            if (message == null) {
                                try {Thread.sleep(10000);} catch (Exception e){}
                                continue;
                            }
                            Log.d(TAG, "[T1] " + message.toString());
                            mcode = message.getString(MessageKey.MESSAGE_CODE, "");
                            ArrayList<AppProcessor> list = PROCESSOR.get(mcode);
                            if (list != null) {
                                for (AppProcessor p : list) {
                                    try {
                                        p.process(mcode, message);
                                    } catch (Exception e) {
                                        Log.e(TAG, "", e);
                                    }
                                }
                            }
                            Log.d(TAG, "[T4]");
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
