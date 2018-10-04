package ru.makproductions.androidlevel2;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

public class WeatherService extends Service {

    private static final String SOME_UNIQUE_ID = "SOME_UNIQUE_ID";
    private static int mID = 4123;

    @Override
    public void onCreate() {
        notificationAlert("WeatherSlave notification","At your service, Master!");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread() {
            @Override
            public void run() {
                try
                {
                    Thread.sleep(10000);
                    stopSelf();
                } catch(
                        InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }.start();
        return START_REDELIVER_INTENT;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        notificationAlert("WeatherSlave notification","Life for my Master!");
        super.onDestroy();
    }

    private void notificationAlert(String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, SOME_UNIQUE_ID);
        builder.setSmallIcon(R.mipmap.ic_launcher_round);
        builder.setContentTitle(title);
        builder.setContentText(message);
        Intent resultIntent = new Intent(this,ServiceActivity.class);
        //developer.android
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(ServiceActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

//        //http://developer.alexanderklimov.ru
        //PendingIntent pendingIntent = PendingIntent.getActivity(this,0,resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pendingIntent);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(mID++,  builder.build());
    }
}
