package com.example.weatherapplication.UpdateWeatherCondition;

public class UpdateUI {
    public static String updateWeatherCondition(int condition, long sunrise, long sunset) {
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
            if (sunrise<sunset) { return "Ngày ít mây"; } else { return "Đêm ít mây"; }
        }
        if (condition==802) {
            if (sunrise<sunset) { return "Ngày mây rải rác"; } else { return "Đêm mây rải rác"; }
        }
        if (condition==803) {
            if (sunrise<sunset) { return "Ngày nhiều mây"; } else { return "Đêm nhiều mây"; }
        }
        if (condition==804) {
            if (sunrise<sunset) { return "Ngày âm u"; } else { return "Đêm âm u"; }
        }
        return null;
    }
}
