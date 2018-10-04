package ru.makproductions.androidlevel2;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ServiceActivity extends AppCompatActivity implements View.OnClickListener {
    private WeatherService weatherService;
    private boolean bound = false;
    final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            WeatherService.LocalBinder binder = (WeatherService.LocalBinder) service;
            weatherService = binder.getService();
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bound = false;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_activity);
        Button startSlaveryButton = findViewById(R.id.start_slave_business);
        startSlaveryButton.setOnClickListener(this);
        Button bindSlave = findViewById(R.id.bind_slave);
        bindSlave.setOnClickListener(this);
        Button unBindSlave = findViewById(R.id.unbind_slave);
        unBindSlave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.start_slave_business:
                startService(new Intent(ServiceActivity.this, WeatherService.class));
                break;
            case R.id.bind_slave:
                bindService(new Intent(ServiceActivity.this,WeatherService.class),serviceConnection, Context.BIND_AUTO_CREATE);
                break;
            case R.id.unbind_slave:
                if (bound) {
                    unbindService(serviceConnection);
                    bound = false;
                }
                break;

        }
    }
}
