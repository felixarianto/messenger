package com.lab.fx.messenger.sms;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;

import com.lab.fx.library.app.AppProcessor;
import com.lab.fx.library.data.Message;
import com.lab.fx.library.data.MessageCode;
import com.lab.fx.library.data.MessageKey;

import org.json.JSONObject;

/**
 * Created by febri on 15/07/17.
 */

public class SmsProcessor implements AppProcessor {

    @Override
    public String[] getCode() {
        return new String[]{MessageCode.SMS_SEND};
    }

    @Override
    public void process(String p_code, Message p_message) {
        switch (p_code) {
            case MessageCode.SMS_SEND:    smsSend(p_message); break;
            case MessageCode.SMS_DELIVER: smsDeliver(p_message); break;
        }
    }

    public void smsSend(Message p_message) {

    }

    public void smsDeliver(Message p_message) {

    }
}
