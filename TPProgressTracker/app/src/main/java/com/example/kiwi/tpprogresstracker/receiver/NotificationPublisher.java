package com.example.kiwi.tpprogresstracker.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by kiwi on 11/2/2016.
 */

public class NotificationPublisher extends BroadcastReceiver {

    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";

    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        if (notification != null) {
            int id = 0;
            if (intent.getStringExtra(NOTIFICATION_ID) != null) {
                int notificationID = Integer.valueOf(intent.getStringExtra(NOTIFICATION_ID));
                id = intent.getIntExtra(NOTIFICATION_ID, notificationID);
            }
            notificationManager.notify(id, notification);
        }
    }
}