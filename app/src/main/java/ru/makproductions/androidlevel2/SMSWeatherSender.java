package ru.makproductions.androidlevel2;

import android.app.Activity;
import android.content.Intent;
import android.telephony.SmsManager;

public class SMSWeatherSender {

    public static final String TEL_NUMBER = "5554";

    public void askForWeather(Activity activity){
        activity.startService(new Intent(activity, SMSWeatherService.class));
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(TEL_NUMBER,null,"1234",null,null);
    }
}
