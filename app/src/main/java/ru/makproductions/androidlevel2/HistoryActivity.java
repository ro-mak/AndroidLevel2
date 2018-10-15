package ru.makproductions.androidlevel2;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class HistoryActivity extends AppCompatActivity {

    public static final String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";
    public static final String TAG = "HistoryActivity";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private DataBaseReader dataBaseReader;
    private DataBaseSaver dataBaseSaver;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.history_options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.clear_history) {
            dataBaseSaver.clearTable();
            dataBaseReader.refreshCursor();
            adapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_activity);
        dataBaseSaver = new DataBaseSaver(this);
        dataBaseSaver.openDatabase();
        dataBaseReader = dataBaseSaver.getDataBaseReader();
        recyclerView = findViewById(R.id.history_recycler_view);
        adapter = new HistoryRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    private class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryRecyclerViewAdapter.HistoryViewHolder> {

        @NonNull
        @Override
        public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new HistoryRecyclerViewAdapter.HistoryViewHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
            if (dataBaseReader.getNumberOfItems() > 0) {
                dataBaseReader.readDataInPosition(position);
                CityWeatherItem cityWeatherItem = dataBaseReader.cursorToCityWeatherItem();
                if (cityWeatherItem != null && cityWeatherItem.isNotEmpty()) {
                    holder.timeTextView.setText(getDate(cityWeatherItem.getTime()));
                    holder.cityTextView.setText(cityWeatherItem.getName());
                    holder.weatherTextView.setText(cityWeatherItem.getWeatherDescription());
                }
            }
        }

        @Override
        public int getItemCount() {
            return dataBaseReader.getNumberOfItems();
        }

        class HistoryViewHolder extends RecyclerView.ViewHolder {
            private TextView timeTextView;
            private TextView cityTextView;
            private TextView weatherTextView;

            HistoryViewHolder(LayoutInflater inflater, ViewGroup parent) {
                super(inflater.inflate(R.layout.history_item, parent, false));
                timeTextView = itemView.findViewById(R.id.time_text_view);
                cityTextView = itemView.findViewById(R.id.city_name_text_view);
                weatherTextView = itemView.findViewById(R.id.weather_description_text_view);
            }
        }
    }

    private String getDate(long millis) {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        Log.e(TAG, "getDate: millis=" + millis + " date:" + formatter.format(calendar.getTime()));
        return formatter.format(calendar.getTime());
    }
}
