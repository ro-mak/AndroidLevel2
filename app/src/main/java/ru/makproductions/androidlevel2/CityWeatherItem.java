package ru.makproductions.androidlevel2;

class CityWeatherItem {
    private long id;
    private String name;
    private String weatherDescription;
    private long time;


    CityWeatherItem(long id, String name, String weatherDescription, long time) {
        this.id = id;
        this.name = name;
        this.weatherDescription = weatherDescription;
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public long getTime() {
        return time;
    }

    boolean isNotEmpty() {
        return name != null && weatherDescription != null;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String getWeatherDescription() {
        return weatherDescription;
    }

    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }
}
