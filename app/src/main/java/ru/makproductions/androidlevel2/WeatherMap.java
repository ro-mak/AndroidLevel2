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

    String getName() {
        return name;
    }

    void setMain(Main main) {
        this.main = main;
    }

    void setName(String name) {
        this.name = name;
    }

    void setSys(Sys sys) {
        this.sys = sys;
    }

    Main getMain() {
        return main;
    }

    Weather[] getWeather() {
        return weather;
    }

    void setWeather(Weather[] weather) {
        this.weather = weather;
    }

    Sys getSys() {
        return sys;
    }

    double getTemp() {
        return main.getTemp();
    }

    void setTemp(double temp) {
        main.setTemp(temp);
    }

    private class Main {
        @SerializedName("temp")
        private double temp;

        double getTemp() {
            return temp;
        }

        void setTemp(double temp) {
            this.temp = temp;
        }
    }

    String getCountry() {
        return sys.getCountry();
    }

    void setCountry(String country) {
        sys.setCountry(country);
    }

    private class Sys {
        @SerializedName("country")
        private String country;

        String getCountry() {
            return country;
        }

        void setCountry(String country) {
            this.country = country;
        }
    }


    String getDescription() {
        return weather[0].getDescription();
    }

    void setDescription(String description) {
        weather[0].setDescription(description);
    }

    private class Weather {
        @SerializedName("description")
        private String description;

        String getDescription() {
            return description;
        }

        void setDescription(String description) {
            this.description = description;
        }
    }
}
