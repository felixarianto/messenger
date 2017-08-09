package com.lab.fx.messenger.sender;

import android.util.Log;

import com.lab.fx.library.data.Message;
import com.lab.fx.library.data.MessageKey;
import com.lab.fx.library.service.MyServices;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * Created by febri on 15/07/17.
 */

public class Sender extends Thread {

    private static String TAG = "Sender";

    private static LinkedBlockingDeque<Message> OUTGOING_QUEUE = new LinkedBlockingDeque<>();

    @Override
    public void run() {
        try {
            Message message;
            while (!isInterrupted()) {
                try {
                    message = OUTGOING_QUEUE.poll(10, TimeUnit.MINUTES);
                    if (message == null) {
                        continue;
                    }
                    Log.d(getName(), "[S1] " + message.toString());
                    boolean write = MyServices.send(message.toString());
                    if (!write) {
                        OUTGOING_QUEUE.addFirst(message);
                    }
                    Log.d(getName(), "[S4] " + (write ? "" : "FAILED"));
                } catch (Exception e) {
                    Log.e(getName(), "", e);
                }
            }
        } catch (Exception e) {
            Log.e(getName(), "", e);
        }
    }

    public static boolean send(Message p_message) {
        if (OutgoingDB.put(p_message.getString(MessageKey.MESSAGE_ID, ""), p_message.toString())) {
            OUTGOING_QUEUE.add(p_message);
            return true;
        }
        return false;
    }

}
