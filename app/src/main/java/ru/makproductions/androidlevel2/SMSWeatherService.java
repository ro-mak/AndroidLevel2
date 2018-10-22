package ru.makproductions.androidlevel2;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.util.Log;

public class SMSWeatherService extends Service {

    public static final String TAG = "SMSWeatherService";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    BroadcastReceiver broadcastReceiver;

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand: Service started");
        new Thread(new Runnable() {
            @Override
            public void run() {
                broadcastReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        Log.e(TAG, "onReceive: SMS received");
                        if (isAskedForWeather(intent)) {
//                            Intent smsIntent = new Intent(Intent.ACTION_VIEW);
//                            smsIntent.putExtra("sms_body", "Moscow +3");
//                            smsIntent.setType("vnd.android-dir/mms-sms");
//                            smsIntent.putExtra("address","5554");
//                            smsIntent.putExtra("exit_on_sent", true);
//                            smsIntent.setFlags(FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(smsIntent);
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage("5554", null, "Moscow +3", null, null);
                        }
                    }
                };
            }
        }).start();

        Log.e(TAG, "onStartCommand: Receiver created");
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(broadcastReceiver, intentFilter);
        Log.e(TAG, "onStartCommand: Receiver registered");
        return super.onStartCommand(intent, flags, startId);
    }

    private boolean isAskedForWeather(Intent intent) {
        Log.e(TAG, "isAskedForWeather:");
        SMSHelper.receiveSMS(intent).equals("1234");
        return false;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        Log.e(TAG, "OnDestroy: Service destroyed");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
