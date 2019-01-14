
/* TextMe Team
 * Jan 2019
 * MyFirebaseMessagingService class:
 * Subclass of FirebaseMessagingService that provide functions to work for messaging
 * */

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
import com.link.dheyaa.textme.utils.dataBaseHelpers;
import java.util.Date;
import java.util.Random;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    /* method launches when there is a new token
    * @param token - token needs to be set
    * */
    @Override
    public void onNewToken(String token) {
        dataBaseHelpers.setToken (token);
        System.out.println ("new->>token");
    }

    /* method launches when a message is received
    * @param message - remote message received
    * */
    @Override
    public void onMessageReceived(RemoteMessage message) {
        super.onMessageReceived(message);
        sendMyNotification(message);
    }

    /* method to generate android notification
    * @param message - remote message received
    * */
    private void sendMyNotification(RemoteMessage message) {

        if (message != null) { // if message is not empty

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            // put attributes of massage in intent
            Intent resultIntent = new Intent(this, MessagingPage.class);
            resultIntent.putExtra("friend_name", message.getData().get("friendName").toString());
            resultIntent.putExtra("friend_id", message.getData().get("friendId").toString());
            resultIntent.putExtra("friend_image", message.getData().get("friend_image").toString());

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_ONE_SHOT);

            // build notification
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
            mBuilder.setSmallIcon(R.drawable.app_icon);
            mBuilder.setContentTitle("Message from: " + message.getNotification().getTitle()); // set title
            mBuilder.setContentText(message.getNotification().getBody()); // set message text
            mBuilder.setAutoCancel(true); // enable auto cancel
            // ring on notification
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            mBuilder.setSound(soundUri);
            mBuilder.setContentIntent(pendingIntent);

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            Log.d("message notification ->", message.getNotification().getBody());

            int id = ((int) Math.random());
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){ // if version number is bigger than 0
                int notifyID = id;
                String CHANNEL_ID = "TextMeChanal";// The id of the channel.
                CharSequence name = "messagesChanal";// The user-visible name of the channel.
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                mBuilder.setChannelId(CHANNEL_ID);
                mNotificationManager.createNotificationChannel(mChannel);
            }

            mNotificationManager.notify(id, mBuilder.build()); // notify

        }
    }
}