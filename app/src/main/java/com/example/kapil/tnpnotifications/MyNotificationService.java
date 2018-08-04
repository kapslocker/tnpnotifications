package com.example.kapil.tnpnotifications;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyNotificationService extends FirebaseMessagingService {

    private static final String LogTag = "TnPNotificationService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){
        Log.d(LogTag, "From: " + remoteMessage.getFrom());
        if (remoteMessage.getNotification() != null) {
            Log.d(LogTag, "Notification:" + remoteMessage.getNotification().getBody());
        }
    }
    @Override
    public void onNewToken(String token) {
        Log.d(LogTag, "Firebase token:" + token);
        //TODO: Send this token to server
    }
}
