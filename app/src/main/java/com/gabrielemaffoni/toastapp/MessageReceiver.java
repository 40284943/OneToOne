package com.gabrielemaffoni.toastapp;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by gabrielemaffoni on 14/03/2017.
 */

public class MessageReceiver extends FirebaseMessagingService {

    private static final String TAG = "Notification service";

    @Override
    public void onMessageReceived(RemoteMessage message) {
        // Notification.showNotification(getApplicationContext(),type, name_sender, surname_sender, name_receiver);
        Log.d(TAG, "From" + message.getFrom());
        Log.d(TAG, "Notification message body" + message.getNotification().getBody());
    }
}
