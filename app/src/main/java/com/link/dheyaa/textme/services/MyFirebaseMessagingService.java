package com.link.dheyaa.textme.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.link.dheyaa.textme.R;
import com.link.dheyaa.textme.activities.MainActivity;

import java.util.Date;
import java.util.Random;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage message) {
        super.onMessageReceived(message);

        Log.d("message  ->> ", "BODY: " + message.getNotification().getBody());
        Log.d("message  ->> ", "TITLE: " + message.getNotification().getTitle());

        sendMyNotification(message.getNotification());
    }



    private void sendMyNotification(RemoteMessage.Notification message) {


        if (message != null) {


            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

            mBuilder.setSmallIcon(R.mipmap.ic_launcher);
            mBuilder.setContentTitle(message.getTitle());
            mBuilder.setContentText(message.getBody());

            Intent resultIntent = new Intent(this, MainActivity.class);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);
            mBuilder.setAutoCancel(true);
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            mBuilder.setSound(soundUri);
            mBuilder.setContentIntent(pendingIntent);

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            Log.d("message notification ->", message.getBody());

            int id = ((int) Math.random());
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                int notifyID = id;
                String CHANNEL_ID = "3957";// The id of the channel.
                CharSequence name = "messagesChanal";// The user-visible name of the channel.
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                mBuilder.setChannelId(CHANNEL_ID);
                mNotificationManager.createNotificationChannel(mChannel);
            }

            mNotificationManager.notify(id, mBuilder.build());

        }
    }
}