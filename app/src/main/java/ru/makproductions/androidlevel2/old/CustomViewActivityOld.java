package ru.makproductions.androidlevel2.old;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import ru.makproductions.androidlevel2.R;

public class CustomViewActivityOld extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zcustom_view_activity_layout_old);
    }
}
