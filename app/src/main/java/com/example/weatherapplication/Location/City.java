package com.example.weatherapplication.Location;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class City {

    public static void setCoordinates(Location location) {
        try {
            LocationInfo.lat = location.getLatitude();
            LocationInfo.lon = location.getLongitude();
            Log.d("Location_lat:", String.valueOf(LocationInfo.lat));
            Log.d("Location_lon:", String.valueOf(LocationInfo.lon));
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            Log.d("Location_error","Khong lay duoc vi tri");
        }
    }

    public static Address getAddress(Context context) {
        Address address = new Address(Locale.getDefault());

        try {
            Geocoder gcd = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = gcd.getFromLocation(LocationInfo.lat, LocationInfo.lon, 1);
            if (addresses != null && addresses.size() > 0) {
                address = addresses.get(0);
            }
        }
        catch (IOException e) {
            Log.d("Location_address", "K lay duoc address");
            e.printStackTrace();
        }

        return address;
    }

}
