package com.njcrain.android.healthtracker;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

//The use of this class also comes from https://gist.github.com/BrandonSmith/6679223
public class NotificationPublisher extends BroadcastReceiver {
//    private int notificationId = 1;

    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = intent.getParcelableExtra("notification");
        int notificationId = intent.getIntExtra("notificationId", 0);

        notificationManager.notify(notificationId, notification);

        intent.putExtra("notificationId", ++notificationId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 5000, pendingIntent);
    }
}