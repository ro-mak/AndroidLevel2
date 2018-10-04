package ru.makproductions.androidlevel2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements WeatherGetter.WeatherGetterListener {


    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WeatherGetter weatherGetter = new WeatherGetter(this);
        String weather = "Autumn 5C° Rain";
        weatherGetter.execute(weather);
        progressBar = findViewById(R.id.progress_bar);
        Button nextAssignment = findViewById(R.id.next_assignment_button);
        nextAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ServiceActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public void checkProgress(Integer updateProgress) {
        progressBar.setProgress(updateProgress);
    }

    @Override
    public void onComplete(String result) {
        TextView weatherTextView = findViewById(R.id.weather);
        weatherTextView.setText(result);
        progressBar.setAlpha(0);
    }
}
