package ru.makproductions.androidlevel2;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FireBaseService extends FirebaseMessagingService {

    public static final String TAG = "FireBaseService";

    @Override
    public void onDestroy() {
        makeNotification("OnDestroy", "Destroyed service");
        super.onDestroy();

    }

    @Override
    public void onCreate() {
        makeNotification("Oncreate", "Created service");
        super.onCreate();
    }

    @Override
    public void onNewToken(String s) {
        Log.e(TAG, "onNewToken: " + s);
        makeNotification("Token", s);
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String token = instanceIdResult.getToken();
                SharedPreferences sharedPreferences = getSharedPreferences("firebase", MODE_PRIVATE);
                sharedPreferences.edit().putString("token", token).apply();
            }
        });
        super.onNewToken(s);
    }

    private static final String SOME_UNIQUE_ID = "SOME_UNIQUE_ID";
    private static int mID = 4123;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        if (notification != null) {
            makeNotification(notification.getTitle(), notification.getBody());
        }
        super.onMessageReceived(remoteMessage);
    }

    private void makeNotification(String title, String notificationText) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, SOME_UNIQUE_ID);
        builder.setSmallIcon(R.mipmap.ic_launcher_round);
        builder.setContentTitle(title);
        builder.setContentText(notificationText);
        Intent resultIntent = new Intent(this, MainActivity.class);
        //developer.android
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) manager.notify(mID++, builder.build());
    }
}
