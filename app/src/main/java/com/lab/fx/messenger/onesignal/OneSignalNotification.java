package com.lab.fx.messenger.onesignal;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.lab.fx.library.app.App;
import com.lab.fx.library.conversation.MessageDB;
import com.lab.fx.library.conversation.MessageHolder;
import com.lab.fx.library.data.Message;
import com.lab.fx.library.data.MessageCode;
import com.lab.fx.library.data.MessageKey;
import com.lab.fx.library.util.MediaUtil;
import com.lab.fx.messenger.R;
import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationDisplayedResult;
import com.onesignal.OSNotificationReceivedResult;

import org.json.JSONArray;

import java.math.BigInteger;

/**
 * Created by febri on 08/08/17.
 */

public class OneSignalNotification extends NotificationExtenderService {
    @Override
    protected boolean onNotificationProcessing(final OSNotificationReceivedResult notification) {
        OverrideSettings overrideSettings = new OverrideSettings();
        overrideSettings.extender = new NotificationCompat.Extender() {
            @Override
            public NotificationCompat.Builder extend(NotificationCompat.Builder builder) {
                builder.setSmallIcon(R.drawable.ic_rss_feed);
                Bitmap ic_large = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                builder.setLargeIcon(ic_large);
                try {
                    JSONArray data = new JSONArray(notification.payload.body);
                    NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle()
                            .setBigContentTitle(data.getString(1))
                            .bigText(data.getString(2))
                            .setSummaryText(data.getString(0))
                            ;
                    builder.setContentTitle(data.getString(1));
                    builder.setContentText (data.getString(2));
                    builder.setStyle(style);
                    builder.setContentIntent(null);

                    String url = data.getString(3);
                    if (!url.startsWith("http://") && !url.startsWith("https://")){
                        url = "http://" + url;
                    }

                    Message message = new Message();
                    message.put(MessageKey.MESSAGE_CODE, MessageCode.MSG_SEND);
                    message.put(MessageKey.MESSAGE_ID,   Message.generateId());
                    message.put(MessageKey.CREATED_TIME, System.currentTimeMillis());
                    message.put(MessageKey.F_PIN,  data.getString(0));
                    message.put(MessageKey.TEXT,   data.getString(1) + "\n" + data.getString(2));
                    message.put(MessageKey.LINK,   url);
                    App.process(message);
                } catch (Exception e) {
                    Log.e("", "", e);
                }
                return builder;
            }
        };

        displayNotification(overrideSettings);
        return true;
    }
}
