package com.example.weatherapplication.URL;

import com.example.weatherapplication.Location.LocationInfo;

public class LinkAPI {
    private String link_Current_Weather_API;
    private String link_3h_Forecast_API; // 5 days
    private String city_URL;

    public LinkAPI() {
        link_Current_Weather_API = "https://api.openweathermap.org/data/2.5/weather?lat="
                + LocationInfo.lat+"&lon="+LocationInfo.lon+"&appid="+LocationInfo.API_KEY+"&units=metric";
        link_3h_Forecast_API = "https://api.openweathermap.org/data/2.5/forecast?lat="
                + LocationInfo.lat+"&lon="+LocationInfo.lon+"&appid="+LocationInfo.API_KEY+"&units=metric";
    }

    public String getLinkCurrentWeatherAPI() {
        return link_Current_Weather_API;
    }
    public String getLink3hForecastAPI() { return link_3h_Forecast_API; }

    public void setCity_URL(String cityName) {
        city_URL = "https://api.openweathermap.org/data/2.5/weather?q="+cityName+"&appid="+LocationInfo.API_KEY;
    }

    public String getCity_URL() {
        return city_URL;
    }
}
