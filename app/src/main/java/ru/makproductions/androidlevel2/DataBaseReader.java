package ru.makproductions.androidlevel2;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Closeable;
import java.io.IOException;

public class DataBaseReader implements Closeable {

    private static final int COLUMN_INDEX = 0;
    private static final int COLUMN_CITY = 2;
    private static final int COLUMN_WEATHER = 3;
    private static final int COLUMN_TIME = 1;
    private Cursor cursor;
    private SQLiteDatabase database;
    private String[] weatherTableColumns = {DataBaseCreator.COLUMN_ID, DataBaseCreator.COLUMN_TIME, DataBaseCreator.COLUMN_CITY_NAME, DataBaseCreator.TABLE_WEATHER};

    DataBaseReader(SQLiteDatabase database) {
        this.database = database;
    }

    void open() {
        sendQuery();
        cursor.moveToFirst();
    }

    private void sendQuery() {
        cursor = database.query(DataBaseCreator.TABLE_WEATHER, weatherTableColumns, null, null, null, null, null);
    }

    CityWeatherItem readDataInPosition(int position) {
        cursor.moveToPosition(position);
        return cursorToCityWeatherItem();
    }

    CityWeatherItem cursorToCityWeatherItem() {
        return new CityWeatherItem(cursor.getLong(COLUMN_INDEX), cursor.getString(COLUMN_CITY), cursor.getString(COLUMN_WEATHER), cursor.getLong(COLUMN_TIME));
    }


    void refreshCursor() {
        int position = cursor.getPosition();
        sendQuery();
        cursor.moveToPosition(position);
    }

    int getNumberOfItems() {
        return cursor.getCount();
    }


    @Override
    public void close() throws IOException {
        cursor.close();
    }
}
