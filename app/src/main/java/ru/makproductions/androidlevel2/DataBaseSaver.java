package ru.makproductions.androidlevel2;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.Closeable;
import java.io.IOException;

public class DataBaseSaver implements Closeable {
    private static final String TAG = "DataBaseSaver";
    private DataBaseCreator dataBaseCreator;
    private SQLiteDatabase database;
    private DataBaseReader dataBaseReader;

    DataBaseSaver(Context context) {
        dataBaseCreator = new DataBaseCreator(context);
    }

    void openDatabase() {
        try {
            database = dataBaseCreator.getWritableDatabase();
            dataBaseReader = new DataBaseReader(database);
            dataBaseReader.open();
        } catch (SQLException e) {
            Log.e(TAG, "openDatabase: " + e.getMessage());
        }
    }

    CityWeatherItem saveCityWeatherItem(String city, String weather) {
        long currentTime = System.currentTimeMillis();
        ContentValues weatherCityValues = new ContentValues();
        weatherCityValues.put(DataBaseCreator.COLUMN_CITY_NAME, city);
        weatherCityValues.put(DataBaseCreator.COLUMN_TIME, currentTime);
        weatherCityValues.put(DataBaseCreator.COLUMN_WEATHER, weather);
        long insertId = database.insert(DataBaseCreator.TABLE_WEATHER, null, weatherCityValues);
        return new CityWeatherItem(insertId, city, weather, currentTime);
    }

    void changeCityWeatherItem(CityWeatherItem cityWeatherItem, String city, String weather) {
        long id = cityWeatherItem.getId();
        ContentValues weatherCityValues = new ContentValues();
        weatherCityValues.put(DataBaseCreator.COLUMN_ID, id);
        if (city != null) weatherCityValues.put(DataBaseCreator.COLUMN_CITY_NAME, city);
        if (weather != null) weatherCityValues.put(DataBaseCreator.COLUMN_WEATHER, weather);
        weatherCityValues.put(DataBaseCreator.COLUMN_TIME, System.currentTimeMillis());
        database.update(DataBaseCreator.TABLE_WEATHER, weatherCityValues, DataBaseCreator.COLUMN_ID + "=" + id, null);
    }

    public void deleteCityWeatherItem(CityWeatherItem cityWeatherItem) {
        database.delete(DataBaseCreator.TABLE_WEATHER, DataBaseCreator.COLUMN_ID + "=" + cityWeatherItem.getId(), null);
    }

    void clearTable() {
        database.delete(DataBaseCreator.TABLE_WEATHER, null, null);
    }

    @Override
    public void close() throws IOException {
        dataBaseCreator.close();
        dataBaseReader.close();
    }

    DataBaseReader getDataBaseReader() {
        return dataBaseReader;
    }
}
