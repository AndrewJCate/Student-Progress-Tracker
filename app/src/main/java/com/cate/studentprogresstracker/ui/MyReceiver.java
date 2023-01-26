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
        Toast.makeText(context, "Notification created successfully.",Toast.LENGTH_LONG).show();

        createNotificationChannel(context, CHANNEL_ID);
        createNotification(context, intent.getStringExtra("msg"));
    }

    private void createNotificationChannel(Context context, String channel_id) {
        CharSequence name;
        String description;
        int importance;
        NotificationChannel channel;
        NotificationManager notificationManager;

        name = context.getResources().getString(R.string.channel_name);
        description = context.getString(R.string.channel_description);
        importance = NotificationManager.IMPORTANCE_DEFAULT;

        channel = new NotificationChannel(channel_id, name, importance);
        channel.setDescription(description);

        // Register channel with system
        notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    private void createNotification(Context context, String text) {
        Notification notification;
        NotificationManager notificationManager;

        notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(context.getString(R.string.app_title))
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId++, notification);
    }
}