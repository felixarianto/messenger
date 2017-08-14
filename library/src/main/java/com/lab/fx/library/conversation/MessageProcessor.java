package com.lab.fx.library.conversation;

import android.content.ContentValues;
import android.telephony.SmsManager;

import com.lab.fx.library.app.App;
import com.lab.fx.library.app.AppProcessor;
import com.lab.fx.library.data.Message;
import com.lab.fx.library.data.MessageCode;
import com.lab.fx.library.data.MessageKey;
import com.lab.fx.library.service.MyServices;

/**
 * Created by febri on 15/07/17.
 */

public class MessageProcessor implements AppProcessor {

    public String[] getId() {
        return new String[]{MessageCode.MSG};
    }

    @Override
    public void process(String p_code, Message p_message) {
        switch (p_code) {
            case MessageCode.MSG_REQUEST:  msgRequest(p_code, p_message); break;
            case MessageCode.MSG_UPDATE:   msgUpdate (p_code, p_message); break;
            case MessageCode.MSG_SEND:     msgSend   (p_code, p_message); break;
            case MessageCode.MSG_DELETE:   msgDelete (p_code, p_message); break;
        }
    }

    public void msgUpdate(String p_code, Message p_message) {
        String key_message_id = p_message.getString(MessageKey.MESSAGE_ID, "");
        String key_status     = p_message.getString(MessageKey.STATUS, "");

        ContentValues
        cvalues = new ContentValues();
        cvalues.put(MessageDB.FIELD_STATUS, key_status);

        int update = MessageDB.update(cvalues, MessageDB.FIELD_MESSAGE_ID + "='" + key_message_id + "'");
        if (update > 0) {
            App.postUI(p_code, MessageDB.getRecord(MessageDB.FIELD_MESSAGE_ID + "='" + key_message_id + "'"));
        }
    }

    public void msgDelete(String p_code, Message p_message) {
        String key_message_id = p_message.getString(MessageKey.MESSAGE_ID, "");
        MessageHolder holder  = MessageDB.getRecord(MessageDB.FIELD_MESSAGE_ID + "='" + key_message_id + "'");
        if (holder == null) {
            return;
        }
        int update = MessageDB.delete(MessageDB.FIELD_MESSAGE_ID + "='" + key_message_id + "'");
        if (update > 0) {
            App.postUI(p_code, holder);
        }
    }

    public void msgRequest(String p_code, Message p_message) {
        String key_media = p_message.getString(MessageKey.MEDIA, "");

        MessageHolder m = new MessageHolder();
        if (!m.clone(p_message)) {
            return;
        }

        m.status    = MessageDB.STATUS_WAITING;
        if (key_media.equals("sms")) {
            MyServices.sms(m.l_pin, m.message_id, m.text);
        }
        else {
            MyServices.send(m.toString());
        }

        long insert = MessageDB.insert(m.toValues());
        if (insert > 0) {
            App.postUI(p_code, m);
        }
    }

    public void msgSend(String p_code, Message p_message) {
        MessageHolder m = new MessageHolder();
        if (!m.clone(p_message)) {
            return;
        }

        m.status    = MessageDB.STATUS_DELIVERED;
        long insert = MessageDB.insert(m.toValues());
        if (insert > 0) {
            App.postUI(p_code, m);
        }
    }
}
