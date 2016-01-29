package com.ferran.yep.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.ferran.yep.R;
import com.ferran.yep.views.MainActivity;
import com.parse.ParseBroadcastReceiver;
import com.parse.ParsePush;
import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ferra on 27/01/2016.
 */
public class CustomPushReciver extends ParsePushBroadcastReceiver {


    private static final String TAG = "CustomReceiver";
    private static final int NOTIFICATION_ID = 1;
    public static int numMessages = 0;

    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        try {
            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
            Log.d(TAG, "onReceive: ");
            String title = json.getString("From");
            String message = json.getString("Message");
            generateNotification(context, title, message);
        } catch (JSONException e) {

        }
    }

    private void generateNotification(Context context, String title, String message) {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, 0);

        numMessages = 0;
        NotificationManager mNotifM = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_stat_bird)
                        .setContentTitle(title + ":")
                        .setContentText(message)
                        .setNumber(++numMessages);

        mBuilder.setAutoCancel(true);
        //Vibration
        mBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});

        //LED
        mBuilder.setLights(Color.MAGENTA, 3000, 3000);

        //Ton
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(uri);

        mBuilder.setContentIntent(contentIntent);

        mNotifM.notify(NOTIFICATION_ID, mBuilder.build());

    }
}
