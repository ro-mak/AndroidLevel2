package ru.makproductions.androidlevel2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SMSWeatherReceiver extends BroadcastReceiver {
    private String weather;
    private WeatherListener weatherListener;

    public SMSWeatherReceiver(WeatherListener weatherListener) {
        this.weatherListener = weatherListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("OnReceiveWeather", "onReceive: received");
        weather = SMSHelper.receiveSMS(intent);
        if (!weather.equals("1234")) onWeatherReceived();

    }

    public String getWeather() {
        return weather;
    }

    private void onWeatherReceived() {
        weatherListener.updateWeather(weather);
    }

    public interface WeatherListener {
        void updateWeather(String weather);
    }
}
