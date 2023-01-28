package util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.cate.studentprogresstracker.ui.MainActivity;

public class Broadcaster {
    public void createBroadcast(Context context, Class<MyReceiver> receiver, String name, String message, long trigger) {
        Intent notifyStartIntent = new Intent(context, MyReceiver.class);
        notifyStartIntent.putExtra(name, message);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                ++MainActivity.alertNumber,
                notifyStartIntent,
                PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, pendingIntent);
    }
}
