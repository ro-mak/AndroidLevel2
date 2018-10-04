package ru.makproductions.androidlevel2.old;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import ru.makproductions.androidlevel2.R;

import static android.hardware.Sensor.TYPE_ALL;

public class SensorsActivityOld extends AppCompatActivity {
    private RecyclerView sensorsRecyclerView;
    private RecyclerView.Adapter adapterRecView;
    private RecyclerView.LayoutManager layoutManagerRecView;
    private float[] values;
    private List<Sensor> sensorList;
    private SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorsRecyclerView = findViewById(R.id.sensors_recycler_view);
        layoutManagerRecView = new LinearLayoutManager(this);
        sensorsRecyclerView.setLayoutManager(layoutManagerRecView);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorList = sensorManager.getSensorList(TYPE_ALL);
        values = new float[sensorList.size()];
        for (Sensor sensor : sensorList) {
            sensorManager.registerListener(new SensorListener(), sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        adapterRecView = new AdapterSensorsRecView(sensorList);
        sensorsRecyclerView.setAdapter(adapterRecView);
        Button button = findViewById(R.id.next_slide_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SensorsActivityOld.this, CustomViewActivityOld.class);
                startActivity(intent);
            }
        });
    }


    private class AdapterSensorsRecView extends RecyclerView.Adapter<AdapterSensorsRecView.ViewHolderSensorsRecView> {
        private List<Sensor> dataSet;

        public AdapterSensorsRecView(List<Sensor> dataSet) {
            this.dataSet = dataSet;
        }

        @Override
        public AdapterSensorsRecView.ViewHolderSensorsRecView onCreateViewHolder(ViewGroup parent, int viewType) {
            RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.zitem_layout_old, parent, false);
            ViewHolderSensorsRecView vh = new ViewHolderSensorsRecView(relativeLayout);
            return vh;
        }

        @Override
        public void onBindViewHolder(AdapterSensorsRecView.ViewHolderSensorsRecView holder, int position) {
            Sensor element = dataSet.get(position);
            String name = element.getName() + " :";
            String data = "" + values[position];
            holder.sensorName.setText(name);
            holder.sensorData.setText(data);
        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }

        public class ViewHolderSensorsRecView extends RecyclerView.ViewHolder {
            public TextView sensorName;
            public TextView sensorData;
            public ImageView sensorImage;

            public ViewHolderSensorsRecView(View itemView) {
                super(itemView);
                sensorName = itemView.findViewById(R.id.sensor_name);
                sensorData = itemView.findViewById(R.id.sensor_data);
                sensorImage = itemView.findViewById(R.id.sensor_image);

            }
        }
    }

    private class SensorListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            values[sensorList.indexOf(sensorEvent.sensor)] = sensorEvent.values[0];
            adapterRecView.notifyDataSetChanged();
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }
}
