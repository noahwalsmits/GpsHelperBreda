package com.b.gpshelperbreda;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class Notifications {
    private static String CHANNEL_ID = "GPS Helper Breda";
    private NotificationManager notificationManager;
    private Context context;
    private int currentId;

    public Notifications(Context context) {
        this.notificationManager = context.getSystemService(NotificationManager.class);
        this.context = context;
        this.currentId = 0;

        // Create the NotificationChannel only on API 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "channel", importance);
            channel.setDescription("the only channel ");
            notificationManager.createNotificationChannel(channel);
        }
    }

    public int sendNotification(String title, String description) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this.context, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground) //TODO get custom icon (probably app launcher icon)
                .setContentTitle(title)
                .setContentText(description)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(description))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        int id = this.nextId();
        notificationManager.notify(id, builder.build());
        return id;
    }

    private int nextId() {
        this.currentId++;
        return this.currentId;
    }

}
