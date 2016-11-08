package com.parkatosu.parkatosu;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import java.util.Date;

/**
 * Created by ajmcs on 11/4/2016.
 */
public class NotificationReceiver extends BroadcastReceiver{
    public void onReceive(Context context, Intent intent) {

        //Toast.makeText(context, "Repeating Alarm worked.", Toast.LENGTH_LONG).show();


        // try here

        // prepare intent which is triggered if the
        // notification is selected

        Intent i = new Intent(context, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, i, 0);

        String permit = intent.getStringExtra("permit");

        // build notification
        // the addAction re-use the same intent to keep the example short

        NotificationCompat.Builder n  = new NotificationCompat.Builder(context)
                .setContentTitle("STREET SWEEPING TOWING")
                .setContentText("STARTS TOMORROW at 8AM for permit " + permit + " until 4PM.")
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentIntent(pIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int id = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        System.out.println("streetsweep id: " + id);

        notificationManager.notify(id, n.build());


    }
}