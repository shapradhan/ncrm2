package com.example.ncrm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * Created by shameer on 2018-03-20.
 */

public class NotificationActivity {
    private static final int NOTIFICATION_ID_OPEN_ACTIVITY = 9;

    public static void openNotification(Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        builder.setContentTitle("Notification");
        builder.setContentText("Text");
        notificationManager.notify(NOTIFICATION_ID_OPEN_ACTIVITY, builder.build());
    }
}
