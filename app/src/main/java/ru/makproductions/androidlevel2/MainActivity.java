package ru.makproductions.androidlevel2;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    public static final String TAG = "MainActivity";
    public static final String COMMA_SPACE = ", ";
    public static final String TEMPERATURE_IS = " temperature is: ";
    public static final String CELCIUS = " C°";
    private SearchView citySearchView;
    private TextView weatherTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        citySearchView = findViewById(R.id.city_search_view);
        weatherTextView = findViewById(R.id.weather_retrofit_text_view);
        initRetrofit();
        citySearchView.setOnQueryTextListener(this);
    }

    private void loadBackground(String weatherType) {
        ImageView imageView = findViewById(R.id.image_view);
        String path = "";
        if (weatherType.contains(getString(R.string.clear_sky_weather_type))) {
            path = getString(R.string.clear_sky_image_path);

        } else if (weatherType.contains(getString(R.string.few_clouds_weather_type))) {
            path = getString(R.string.few_clouds_image_path);

        } else if (weatherType.contains(getString(R.string.scattered_clouds_weather_type))) {
            path = getString(R.string.scattered_clouds_image_path);

        } else if (weatherType.contains(getString(R.string.broken_clouds_weather_type))) {
            path = getString(R.string.broken_clouds_image_path);

        } else if (weatherType.contains(getString(R.string.shower_rain_weather_type))) {
            path = getString(R.string.shower_rain_image_path);

        } else if (weatherType.contains(getString(R.string.rain_weather_type))) {
            path = getString(R.string.rain_image_path);

        } else if (weatherType.contains(getString(R.string.thunderstorm_weather_type))) {
            path = getString(R.string.thunderstorm_image_path);

        } else if (weatherType.contains(getString(R.string.snow_weather_type))) {
            path = getString(R.string.snow_image_path);

        } else if (weatherType.contains(getString(R.string.mist_weather_type))) {
            path = getString(R.string.mist_image_path);
        }
        if (!path.isEmpty()) {
            Picasso.get().load(path).into(imageView);
        } else {
            Log.d(TAG, "loadBackground: " + weatherType);
        }
    }

    private OpenWeatherRetrofit openWeatherRetrofit;

    private void initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.api_base_uri))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        openWeatherRetrofit = retrofit.create(OpenWeatherRetrofit.class);
    }

    private void getWeather(String city, String appId) {
        openWeatherRetrofit.loadWeather(city, appId, getString(R.string.weather_units_type))
                .enqueue(new Callback<WeatherMap>() {
                    @Override
                    public void onResponse(@NonNull Call<WeatherMap> call, @NonNull Response<WeatherMap> response) {
                        if (response.body() != null) {
                            StringBuilder weather = new StringBuilder();
                            WeatherMap body = response.body();
                            weather.append(body.getName());
                            weather.append(COMMA_SPACE);
                            weather.append(body.getSys().getCountry());
                            weather.append(TEMPERATURE_IS);
                            weather.append(body.getMain().getTemp());
                            weather.append(CELCIUS);
                            weatherTextView.setText(weather.toString());
                            loadBackground(body.getWeather()[0].getDescription());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<WeatherMap> call, @NonNull Throwable t) {
                        weatherTextView.setText(R.string.try_again);
                        Log.e(TAG, "onFailure: " + t.getMessage());
                    }
                });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        getWeather(query, getString(R.string.app_id));
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
