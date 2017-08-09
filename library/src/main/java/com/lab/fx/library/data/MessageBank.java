package com.lab.fx.library.data;

import android.provider.Settings;
import android.util.Log;

import com.lab.fx.library.contact.Mine;
import com.lab.fx.library.conversation.MessageHolder;

import org.json.JSONException;

/**
 * Created by febri on 30/07/17.
 */

public class MessageBank {

    private static String TAG = "MessageBank";

    public static Message msgRequest(String l_pin, String p_text, String p_media) {
        Message m = new Message();
        try {
            m.put(MessageKey.MESSAGE_CODE, MessageCode.MSG_REQUEST);
            m.put(MessageKey.MESSAGE_ID,   Message.generateId());
            m.put(MessageKey.CREATED_TIME, System.currentTimeMillis());
            m.put(MessageKey.F_PIN, Mine.get().pin);
            m.put(MessageKey.L_PIN, l_pin);
            m.put(MessageKey.TEXT,  p_text);
            m.put(MessageKey.MEDIA, p_media);
            return m;
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
        return m;
    }

}
