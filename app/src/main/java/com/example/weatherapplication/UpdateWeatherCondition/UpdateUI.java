package com.example.weatherapplication.UpdateWeatherCondition;

import java.util.Calendar;

public class UpdateUI {
    public static String updateWeatherCondition(int condition) {
        if (condition>=200 && condition<232) {
            return "Trời giông";
        }
        if (condition>=300 && condition<321) {
            return "Mưa phùn";
        }
        if (condition>=500 && condition<=531) {
            return "Trời mưa";
        }
        if (condition>=600 && condition<=622) {
            return "Tuyết rơi";
        }
        if (condition>=700 && condition<=781) {
            return "Sương mù";
        }
        if (condition==800) {
            return  "Trời quang";
        }
        if (condition==801) {
            if (checkDayorNight()) { return "Ngày ít mây"; } else { return "Đêm ít mây"; }
        }
        if (condition==802) {
            if (checkDayorNight()) { return "Ngày mây rải rác"; } else { return "Đêm mây rải rác"; }
        }
        if (condition==803) {
            if (checkDayorNight()) { return "Ngày nhiều mây"; } else { return "Đêm nhiều mây"; }
        }
        if (condition==804) {
            if (checkDayorNight()) { return "Ngày âm u"; } else { return "Đêm âm u"; }
        }
        return null;
    }

    public static boolean checkDayorNight() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if ((hour >= 18 && hour < 24) || (hour >= 0 && hour < 6)) {
            return false;
        }
        return true;
    }
}
