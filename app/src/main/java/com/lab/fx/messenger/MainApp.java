package com.lab.fx.messenger;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.lab.fx.library.app.App;
import com.lab.fx.library.service.MyServices;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;
import com.onesignal.OneSignal.NotificationOpenedHandler;

/**
 * Created by febri on 08/08/17.
 */

public class MainApp extends Application {

    private static String TAG = "MainApp";

    @Override
    public void onCreate() {
        super.onCreate();
        OneSignal.startInit(this)
        .autoPromptLocation(false) // default call promptLocation later
        .setNotificationReceivedHandler(new ExampleNotificationReceivedHandler())
        .setNotificationOpenedHandler  (new ExampleNotificationOpenedHandler())
        .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
        .unsubscribeWhenNotificationsAreDisabled(true)
        .init();
        MyServices.create(this);
    }

    private class ExampleNotificationReceivedHandler implements OneSignal.NotificationReceivedHandler {
        @Override
        public void notificationReceived(OSNotification notification) {
        }
    }


    private class ExampleNotificationOpenedHandler implements NotificationOpenedHandler {
        // This fires when a notification is opened by tapping on it.
        @Override
        public void notificationOpened(OSNotificationOpenResult result) {
            try {
//                JSONArray data = new JSONArray(result.notification.payload.body);
//                String url = data.getString(3);
//                if (!url.startsWith("http://") && !url.startsWith("https://")){
//                    url = "http://" + url;
//                }
//                Log.d(TAG, "Load URL " + url);
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            } catch (Exception e) {
                Log.e(TAG, "", e);
            }
        }
    }
}
