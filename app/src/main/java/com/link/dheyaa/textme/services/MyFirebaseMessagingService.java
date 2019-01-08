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

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.link.dheyaa.textme.R;
import com.link.dheyaa.textme.activities.MainActivity;
import com.link.dheyaa.textme.activities.MessagingPage;
import com.link.dheyaa.textme.utils.dataBaeseHelpers;
import java.util.Date;
import java.util.Random;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public void onNewToken(String token) {
        dataBaeseHelpers.setToken(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage message) {
        super.onMessageReceived(message);

        Log.d("message  ->> ", "BODY: " + message.getNotification().getBody());
        Log.d("message  ->> ", "TITLE: " + message.getNotification().getTitle());

        sendMyNotification(message);
    }



    private void sendMyNotification(RemoteMessage message) {


        if (message != null) {


            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            Intent resultIntent = new Intent(this, MessagingPage.class);
            resultIntent.putExtra("friend_name", message.getData().get("friendName").toString());
            resultIntent.putExtra("friend_id", message.getData().get("friendId").toString());


            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_ONE_SHOT);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

            mBuilder.setSmallIcon(R.drawable.app_icon);
            mBuilder.setContentTitle(message.getNotification().getTitle());
            mBuilder.setContentText(message.getNotification().getBody());

            mBuilder.setAutoCancel(true);
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            mBuilder.setSound(soundUri);
            mBuilder.setContentIntent(pendingIntent);

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            Log.d("message notification ->", message.getNotification().getBody());

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