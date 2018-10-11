package ru.makproductions.androidlevel2;

import com.google.gson.annotations.SerializedName;

public class WeatherMap {

    @SerializedName("weather")
    private Weather[] weather;
    @SerializedName("main")
    private Main main;

    @SerializedName("name")
    private String name;

    @SerializedName("sys")
    private Sys sys;

    public String getName() {
        return name;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSys(Sys sys) {
        this.sys = sys;
    }

    Main getMain() {
        return main;
    }

    public Weather[] getWeather() {
        return weather;
    }

    public void setWeather(Weather[] weather) {
        this.weather = weather;
    }

    Sys getSys() {
        return sys;
    }

    public class Main {
        @SerializedName("temp")
        private double temp;

        double getTemp() {
            return temp;
        }

        public void setTemp(double temp) {
            this.temp = temp;
        }
    }


    public class Sys {
        @SerializedName("country")
        private String country;

        String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }
    }

    public class Weather {
        @SerializedName("description")
        private String description;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
