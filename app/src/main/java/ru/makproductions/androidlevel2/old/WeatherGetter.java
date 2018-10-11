package ru.makproductions.androidlevel2.old;

import android.os.AsyncTask;

public class WeatherGetter extends AsyncTask<String, Integer, String> {

    public interface WeatherGetterListener {
        void checkProgress(Integer updateProgress);

        void onComplete(String result);
    }

    private WeatherGetterListener weatherGetterListener;

    WeatherGetter(WeatherGetterListener weatherGetterListener) {
        this.weatherGetterListener = weatherGetterListener;
    }

    @Override
    protected String doInBackground(String... strings) {
        String result = strings[0];
        result += " in Moscow";
        sleep(1000, 30);
        result += " today";
        sleep(1000, 60);
        result += ". It's cold maan!";
        sleep(1000, 100);
        return result;
    }

    private void sleep(long time, int progress) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        publishProgress(progress);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        weatherGetterListener.checkProgress(values[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        weatherGetterListener.onComplete(s);
    }
}