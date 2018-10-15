package ru.makproductions.androidlevel2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;

public class DataBaseCreator extends SQLiteOpenHelper {

    private static final String DATA_BASE_NAME = "weatherInCities.db";
    private static final int DATA_BASE_VERSION = 1;
    static final String TABLE_WEATHER = "weather";
    static final String COLUMN_ID = BaseColumns._ID;
    static final String COLUMN_TIME = "time";
    static final String COLUMN_CITY_NAME = "city";
    static final String COLUMN_WEATHER = "weather";


    DataBaseCreator(@Nullable Context context) {
        super(context, DATA_BASE_NAME, null, DATA_BASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_WEATHER + " (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_TIME + " INTEGER," + COLUMN_CITY_NAME + " TEXT," + COLUMN_WEATHER + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            //код обновления
        }
    }
}
