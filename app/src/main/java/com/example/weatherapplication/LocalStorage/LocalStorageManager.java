package com.example.weatherapplication.LocalStorage;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

import org.json.JSONException;
import org.json.JSONObject;

public class LocalStorageManager {
    private static final String PREF_NAME = "MyAppPrefs";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_LATITUDE = "latitude";
//    private static String API_KEY; // type JSON
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;
    public LocalStorageManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveLocationData(Location location) {
        editor.putFloat(KEY_LONGITUDE, (float) location.getLongitude());
        editor.putFloat(KEY_LATITUDE, (float) location.getLatitude());
        editor.apply();
    }

    public Location getLocationData() {
        double latitude = sharedPreferences.getFloat(KEY_LATITUDE, 0);
        double longitude = sharedPreferences.getFloat(KEY_LONGITUDE, 0);
        Location location = new Location("");
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return location;
    }

    /**
     *
     * @param API_KEY: Có 2 KEY_API là current_weather và forecast_weather
     * @param jsonData 2 API đều trả về JSON type
     */

    public void saveJsonData(String API_KEY, String jsonData) {
        editor.putString(API_KEY, jsonData);
        editor.apply();
    }

    public JSONObject getJsonData(String API_KEY) throws JSONException {
        return new JSONObject(sharedPreferences.getString(API_KEY, ""));
    }

}
