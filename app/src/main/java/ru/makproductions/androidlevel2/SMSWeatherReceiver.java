package ru.makproductions.androidlevel2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSWeatherReceiver extends BroadcastReceiver {
    private String weather;
    private WeatherListener weatherListener;
    public SMSWeatherReceiver(WeatherListener weatherListener){
        this.weatherListener = weatherListener;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("OnReceiveWeather", "onReceive: received");
        if (intent != null && intent.getAction() != null) {
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            SmsMessage[] messages = new SmsMessage[pdus.length];

            for (int i = 0; i < pdus.length; i++) {
                messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
            }
            StringBuilder body = new StringBuilder();
            for (int i = 0; i < messages.length; i++) {
                body.append(messages[i].getMessageBody());
            }
            weather = body.toString();
            if(!weather.equals("1234"))onWeatherReceived();
        }
    }

    public String getWeather() {
        return weather;
    }

    private void onWeatherReceived(){
        weatherListener.updateWeather(weather);
    }

    public interface WeatherListener{
        void updateWeather(String weather);
    }
}
