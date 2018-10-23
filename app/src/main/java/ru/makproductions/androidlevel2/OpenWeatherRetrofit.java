package ru.makproductions.androidlevel2;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherRetrofit {
    @GET("data/2.5/weather")
    Call<WeatherMap> loadWeather(@Query("q") String cityName, @Query("appid") String appId, @Query("units") String units);
    @GET("data/2.5/weather")
    Call<WeatherMap> loadWeatherByCoordinates(@Query("lat") Double latitude,@Query("lon") Double longtitude, @Query("appid") String appId, @Query("units") String units);
}
