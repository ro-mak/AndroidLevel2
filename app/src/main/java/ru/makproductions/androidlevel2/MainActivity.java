package ru.makproductions.androidlevel2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, SMSWeatherReceiver.WeatherListener {

    public static final String TAG = "MainActivity";
    public static final String COMMA_SPACE = ", ";
    public static final String TEMPERATURE_IS = " temperature is: ";
    public static final String CELCIUS = " C°";
    public static final int PERMISSIONS_REQUEST_CODE = 1234;
    private SearchView citySearchView;
    private TextView weatherTextView;
    private DataBaseSaver dataBaseSaver;
    private DataBaseReader dataBaseReader;
    private boolean isLoadedThroughInternet = true;
    private LocationManager locationManager;
    private String geoProvider;
    private double latitude;
    private double longtitude;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        citySearchView = findViewById(R.id.city_search_view);
        weatherTextView = findViewById(R.id.weather_retrofit_text_view);
        initRetrofit();
        citySearchView.setOnQueryTextListener(this);
        dataBaseSaver = new DataBaseSaver(this);
        dataBaseSaver.openDatabase();
        dataBaseReader = dataBaseSaver.getDataBaseReader();
        SMSWeatherReceiver smsWeatherReceiver = new SMSWeatherReceiver(this);
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(smsWeatherReceiver, intentFilter);
        showLocalWeatherIfAllowed();
        // FirebaseMessaging.getInstance().subscribeToTopic("Hello");
    }

    private void showLocalWeatherIfAllowed() {
        if (checkPermissions()) {
            requestGeographicCoordinates();
        } else {
            requestGeoLocationPermissions();
        }
    }

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length == 2
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED ||
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                requestGeographicCoordinates();
            }
        }
    }

    private void requestGeoLocationPermissions() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(this, permissions, PERMISSIONS_REQUEST_CODE);
        }
    }

    @SuppressLint("MissingPermission")
    private void requestGeographicCoordinates() {
        if (!checkPermissions()) return;
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        geoProvider = locationManager.getBestProvider(criteria, true);
        if (geoProvider != null) {
            locationManager.requestLocationUpdates(geoProvider, 10000, 10, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    latitude = location.getLatitude();
                    longtitude = location.getLongitude();
                    Log.e(TAG, "onLocationChanged: lat " + latitude);
                    Log.e(TAG, "onLocationChanged: lon " + longtitude);
                    getWeatherByCoordinates(latitude, longtitude, getString(R.string.app_id));
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.history_option) {
            Intent intent = new Intent(this, HistoryActivity.class);
            startActivity(intent);
        } else if (id == R.id.get_weather_by_internet) {
            item.setChecked(true);
            isLoadedThroughInternet = true;
        } else if (id == R.id.get_weather_by_sms) {
            item.setChecked(true);
            isLoadedThroughInternet = false;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void saveToDataBase(String city, String weather) {
        if (!searchCity(city, weather)) {
            dataBaseSaver.saveCityWeatherItem(city, weather);
        }
        Log.e(TAG, "saveToDataBase: " + city + " saved");
    }

    private boolean searchCity(String city, String weather) {
        for (int i = 0; i < dataBaseReader.getNumberOfItems(); i++) {
            dataBaseReader.readDataInPosition(i);
            CityWeatherItem cityWeatherItem = dataBaseReader.cursorToCityWeatherItem();
            if (cityWeatherItem.getName().equals(city)) {
                dataBaseSaver.changeCityWeatherItem(cityWeatherItem, city, weather);
                dataBaseReader.refreshCursor();
                return true;
            }
        }

        return false;
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
                        onRetrofitResponse(response);
                    }

                    @Override
                    public void onFailure(@NonNull Call<WeatherMap> call, @NonNull Throwable t) {
                        weatherTextView.setText(R.string.try_again);
                        Log.e(TAG, "onFailure: " + t.getMessage());
                    }
                });
    }

    private void getWeatherByCoordinates(Double latitude, Double longtitude, String appId) {
        openWeatherRetrofit.loadWeatherByCoordinates(latitude, longtitude, appId, getString(R.string.weather_units_type))
                .enqueue(new Callback<WeatherMap>() {
                    @Override
                    public void onResponse(@NonNull Call<WeatherMap> call, @NonNull Response<WeatherMap> response) {
                        onRetrofitResponse(response);
                    }

                    @Override
                    public void onFailure(@NonNull Call<WeatherMap> call, @NonNull Throwable t) {
                        weatherTextView.setText(R.string.try_again);
                        Log.e(TAG, "onFailure: " + t.getMessage());
                    }
                });
    }

    private void onRetrofitResponse(Response<WeatherMap> response) {
        if (response.body() != null) {
            StringBuilder weatherGUI = new StringBuilder();
            WeatherMap body = response.body();
            String cityName = body.getName();
            String description = body.getDescription();
            StringBuilder weatherDatabase = new StringBuilder();
            weatherDatabase.append(body.getTemp());
            weatherDatabase.append(CELCIUS);
            weatherDatabase.append(COMMA_SPACE);
            weatherDatabase.append(description);

            weatherGUI.append(cityName);
            weatherGUI.append(COMMA_SPACE);
            weatherGUI.append(body.getCountry());
            weatherGUI.append(TEMPERATURE_IS);
            weatherGUI.append(weatherDatabase.toString());

            weatherTextView.setText(weatherGUI.toString());
            saveToDataBase(cityName, weatherDatabase.toString());
            loadBackground(description);
            Log.e(TAG, "onResponse: " + "Responded to " + cityName);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.e(TAG, "onQueryTextSubmit: " + query + " submitted");
        if (isLoadedThroughInternet) {
            getWeather(query, getString(R.string.app_id));
        } else {
            SMSWeatherSender smsWeatherSender = new SMSWeatherSender();
            smsWeatherSender.askForWeather(this);
        }
        citySearchView.clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
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

    @Override
    public void updateWeather(String weather) {
        weatherTextView.setText(weather);
    }
}
