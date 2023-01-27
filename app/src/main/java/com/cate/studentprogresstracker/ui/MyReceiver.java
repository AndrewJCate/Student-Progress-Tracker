package com.cate.studentprogresstracker.ui;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.cate.studentprogresstracker.R;

public class MyReceiver extends BroadcastReceiver {

    private final String CHANNEL_ID = "progress_tracker";

    public static int notificationId = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        createNotificationChannel(context);
        createNotification(context, intent.getStringExtra("msg"));
    }

    private void createNotificationChannel(Context context) {
        CharSequence name = context.getResources().getString(R.string.channel_name);
        String description = context.getString(R.string.channel_description);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);

        // Register channel with system
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    private void createNotification(Context context, String text) {
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(context.getString(R.string.app_title))
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId++, notification);

        // FIXME: Toast only shows when notification is received. How display when notification is successfully created?
        Toast.makeText(context, "Notification created successfully.",Toast.LENGTH_LONG).show();
    }
}