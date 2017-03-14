package com.gabrielemaffoni.toastapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;


import static com.gabrielemaffoni.toastapp.utils.Static.BEER;
import static com.gabrielemaffoni.toastapp.utils.Static.COCKTAIL;
import static com.gabrielemaffoni.toastapp.utils.Static.COFFEE;
import static com.gabrielemaffoni.toastapp.utils.Static.LUNCH;

/**
 * Created by gabrielemaffoni on 14/03/2017.
 */

//TODO how do I transform this in a background service?(!IMPORTANT)

public class Notification extends Service {


    public static void showNotification(Context context, int typeOfEvent, String nameSender, String surnameSender, String nameReceiver) {
        String type = "";

        switch (typeOfEvent) {
            case BEER:
                type = "beer";
                break;
            case COCKTAIL:
                type = "cocktail";
                break;
            case LUNCH:
                type = "lunch";
                break;
            case COFFEE:
                type = "coffee";
                break;
        }
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.cast_ic_notification_small_icon)
                .setContentTitle(nameSender + " " + surnameSender)
                .setContentText("Hey " + nameReceiver + "! Wanna have a " + type + "?");


        Intent notificationIntent = new Intent(context, HomeActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(contentIntent);

        //Add as notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, notification.build());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
