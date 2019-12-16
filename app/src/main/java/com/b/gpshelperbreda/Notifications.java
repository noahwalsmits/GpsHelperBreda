package com.b.gpshelperbreda;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

/**
 * Class used to create push notifications
 */
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

    /**
     * Creates and sends a simple notification
     *
     * @param title       The title of the notification
     * @param description The description of the notification
     * @param id          Use the same id of a previous notification to update it
     * @return he id of the notification, we currently do not utilize it
     */
    public int sendNotification(String title, String description, int id) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this.context, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.icon)
                .setContentTitle(title)
                .setContentText(description)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(description))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificationManager.notify(id, builder.build());
        return id;
    }

    /**
     * Creates and sends a simple notification with a unique id
     *
     * @param title       The title of the notification
     * @param description The description of the notification
     * @return The id of the notification, we currently do not utilize it
     */
    public int sendNotification(String title, String description) {
        return this.sendNotification(title, description, this.nextId());
    }

    /**
     * Get a new unique id for a notification
     *
     * @return An integer that is more than 0
     */
    private int nextId() {
        this.currentId++;
        return this.currentId;
    }

}
