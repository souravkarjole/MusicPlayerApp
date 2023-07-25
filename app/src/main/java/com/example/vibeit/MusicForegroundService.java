package com.example.vibeit;

import static com.example.vibeit.App.CHANNEL_ID;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class MusicForegroundService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String song_name = intent.getStringExtra("SONG_NAME");

        // called when user interacts with the notification
        Intent notificationIntent = new Intent(this,android_large___1_activity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                notificationIntent, // enables user to open the 'android_large___1_activity' when clicked to notification as it wraps above notificationIntent
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // When you create a notification using NotificationCompat.Builder,
        // you need to specify the CHANNEL_ID to assign the notification to a specific channel. If the channel does not exist, it will be created.

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Playing...")
                .setContentText(song_name)
                .setSmallIcon(R.drawable.music_notif)
                .build();


        /*
            The first parameter of the startForeground() method is the unique ID of the notification.
            This ID is used to identify the notification, and it is required because a foreground service must have a
            notification associated with it. The notification acts as a persistent indicator to the user that the service is running and performing some important task.
         */
        startForeground(1,notification);

        /*
            In Android, a service can run in the background to perform some tasks,
            and it may get stopped by the system when it is no longer needed or when the system requires resources.
            The START_NOT_STICKY return value indicates that if the service gets killed by the system, it should not be restarted automatically.
         */
        return START_NOT_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        stopForeground(true);
    }
}
