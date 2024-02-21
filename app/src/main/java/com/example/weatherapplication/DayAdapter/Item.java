package com.example.weatherapplication.DayAdapter;

import java.text.DecimalFormat;

public class Item {
    private String hour;
    private String icon;
    private double temp;
    public static String tempUnit = "°C";

    public Item(String hour, String icon, double temp) {
        this.icon = icon;
        this.temp = temp;
        this.hour = hour;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTemp() {
        DecimalFormat df = new DecimalFormat("#");
        String formattedValue = df.format(temp);
        return formattedValue+tempUnit;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }
}
