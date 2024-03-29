package com.example.weatherapplication;

import static com.example.weatherapplication.Location.City.getAddress;
import static com.example.weatherapplication.Location.City.setCoordinates;
import static com.example.weatherapplication.UpdateWeatherCondition.UpdateUI.updateWeatherCondition;

import android.Manifest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;

import com.android.volley.toolbox.Volley;
import com.example.weatherapplication.DayAdapter.Item;
import com.example.weatherapplication.DayAdapter.ItemsAdapter;
import com.example.weatherapplication.LocalStorage.LocalStorageManager;
import com.example.weatherapplication.URL.LinkAPI;
import com.example.weatherapplication.ViewComponent.AddCityActivity;
import com.example.weatherapplication.network.InternetConnectivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private ItemsAdapter itemsAdapter;
    private List<Item> items;
    private static final int PERMISSION_CODE = 1;
    Address address;
    int hmdt, press;
    String city = "", locality = "";
    double currentTemp = 0, feelTemp = 0, windSp;
    String icon, tempUnit, weatherDes, crDate;
    Date date;
    View taskBar;
    TextView cityName, currentTemperature, temperatureUnit, weatherDescription, currentDate,
            humidity, feelTemperature, windSpeed, pressure, weatherCondition;
    RelativeLayout rootLayout, changeTemperatureUnit, addCityBtn, addCityView;
    LocalStorageManager localStorageManager;
    LocationManager locationManager;
    Location location;
    int mainColor, textColor;
    boolean isPause = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        localStorageManager = new LocalStorageManager(this);
        locationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);

        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.itemsRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);

        swipeRefreshLayout = findViewById(R.id.swiperefreshlayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        setAppColor();

        getLocation();

        renderDataWhenDisConnected();

        handleTaskBar();
    }

    @Override
    protected void onStart() {
        super.onStart();
        isPause = false;
    }
    @Override
    protected void onPause() {
        super.onPause();
        isPause = true;
    }

    @Override
    public void onRefresh() {
        if (!InternetConnectivity.isInternetConnected(this)) {
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Bật Dữ liệu hoặc sử dụng Wi-fi để cập nhật.", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }, 3000);
        } else {
            getLocation();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // view ở dưới là vùng có thể nhấp vào, những vùng còn lại khi touch sẽ ẩn view
        taskBar = findViewById(R.id.taskBar);

        Rect viewRect = new Rect();
        taskBar.getGlobalVisibleRect(viewRect);
        if (!viewRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
            taskBar.setVisibility(View.INVISIBLE);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void setAppColor() {
        mainColor = ContextCompat.getColor(this, R.color.dayBGColor);
        textColor = ContextCompat.getColor(this, R.color.dayTextColor);

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if ((hour >= 18 && hour < 24) || (hour >= 0 && hour < 6)) {
            mainColor = ContextCompat.getColor(this, R.color.nightBGColor);
        }
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(mainColor);
            getWindow().setStatusBarColor(mainColor);
        }
    }

    public void getLocation() {
        // Check if the app has permission to access the device's location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // If permission is not granted, request it
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_CODE);
        } else {
            // Get last location (before GPS turn off)
            getLocationByUsingNetwork();
            // Get location when GPS turn off
            location = localStorageManager.getLocationData();

            setCoordinates(location);
            // Get response from API type JSON and render to UI
            getCurrentWeatherDataByUsingNetwork();
            getForecastWeatherDataByUsingNetWork();
        }
    }

    public void getLocationByUsingNetwork() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                5000,
                5,
                new LocationListener() {
                    @Override
                        public void onLocationChanged(@NonNull Location location) {
                        MainActivity.this.location = location;
                        localStorageManager.saveLocationData(location);
                    }

                    @Override
                    public void onProviderDisabled(@NonNull String provider) {
                        if (!isPause) {
                            Toast.makeText(MainActivity.this, "Bật dịch vụ Vị trí để thu thập thông tin thời tiết tại vị trí hiện tại của bạn.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onProviderEnabled(@NonNull String provider) {

                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }
                }
        );
    }

    public void getCurrentWeatherDataByUsingNetwork() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        LinkAPI url = new LinkAPI();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url.getLinkCurrentWeatherAPI(), null,
                response ->
                {
                    localStorageManager.saveJsonData("current_weather",String.valueOf(response));
                    renderCurrentWeather(response);
                },
                error -> Toast.makeText(getApplicationContext(), "Bật Dữ liệu hoặc sử dụng Wi-fi để truy cập.", Toast.LENGTH_SHORT).show());
        requestQueue.add(jsonObjectRequest);
    }

    public void getForecastWeatherDataByUsingNetWork() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        LinkAPI url = new LinkAPI();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url.getLink3hForecastAPI(), null,
                response ->
                {
                    localStorageManager.saveJsonData("forecast_weather",String.valueOf(response));
                    renderForecastWeather(response);
                },
                error -> {});
        requestQueue.add(jsonObjectRequest);
    }

    public void renderDataWhenDisConnected() {
        // Dùng khi kết nối bị ngắt
        if (!InternetConnectivity.isInternetConnected(this)) {
            try {
                renderCurrentWeather(localStorageManager.getJsonData("current_weather"));
                renderForecastWeather(localStorageManager.getJsonData("forecast_weather"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void renderCurrentWeather(JSONObject response) {
        int conditionID=800;

        address = getAddress(this);

        cityName = findViewById(R.id.cityName);
        currentTemperature = findViewById(R.id.currentTemperature);
        currentDate = findViewById(R.id.currentDate);
        feelTemperature = findViewById(R.id.feelTemperature);
        humidity = findViewById(R.id.humidity);
        windSpeed = findViewById(R.id.windSpeed);
        pressure = findViewById(R.id.pressure);
        weatherCondition = findViewById(R.id.weatherCondition);

        locality = address.getLocality();
        city = address.getAdminArea();

        date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d 'thg' M", new Locale("vi", "VN"));
        crDate = simpleDateFormat.format(date);

        try {
            currentTemp = response.getJSONObject("main").getDouble("temp");
            feelTemp = response.getJSONObject("main").getDouble("feels_like");
            hmdt = response.getJSONObject("main").getInt("humidity");
            windSp= response.getJSONObject("wind").getDouble("speed");
            press = response.getJSONObject("main").getInt("pressure");

            conditionID = response.getJSONArray("weather").getJSONObject(0).getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("API_response","K lay duoc");
        }

        cityName.setText(locality+", "+city);
        currentDate.setText(crDate);
        currentTemperature.setText(roundedDouble(currentTemp));
        feelTemperature.setText(roundedDouble(feelTemp) + "°C");
        humidity.setText(hmdt+"%");
        windSpeed.setText(roundedDouble(windSp * 3.6)+" km/h");
        pressure.setText(new DecimalFormat("#,###").format(press)+"hPa");
        weatherCondition.setText(updateWeatherCondition(conditionID));
    }

    public void renderForecastWeather(JSONObject response) {
        items = new ArrayList<>();
        Item item;
        String hour, icon;
        double temp;
        try {
            JSONArray weatherArray = response.getJSONArray("list");
            for (int i=0; i<15; i++) {
                JSONObject weatherObject = weatherArray.getJSONObject(i);

                // Lấy epoch time chuyển đổi qua ngày tháng
                long h = weatherObject.getLong("dt");
                Date date = new Date(h * 1000); // Đổi từ seconds -> milliseconds

                // format 16:00 -> 4:00CH (Viet Nam)
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm aa", new Locale("vi", "VN"));

                hour = simpleDateFormat.format(date);
                icon = "i"+weatherObject.getJSONArray("weather").getJSONObject(0).getString("icon");
                temp = weatherObject.getJSONObject("main").getDouble("temp");

                item = new Item(hour, icon, temp);
                items.add(item);
            }
            itemsAdapter = new ItemsAdapter(items);
            recyclerView.setAdapter(itemsAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("API_response", "Khong lay duoc Forecast weather");
        }
    }

    public void handleTaskBar() {
        final boolean[] isTaskBarVisible = {false};

        ImageView showTaskbar;
        
        taskBar = findViewById(R.id.taskBar);
        showTaskbar = findViewById(R.id.showTaskBar);
        taskBar.setVisibility(View.INVISIBLE);
        rootLayout = findViewById(R.id.rootLayout);
        changeTemperatureUnit = findViewById(R.id.changeTemperatureUnit);
        addCityBtn = findViewById(R.id.addCityBtn);

        // Bật/Tắt khi nhấn vào show task bar
        showTaskbar.setOnClickListener(v -> {
            if (isTaskBarVisible[0]) {
                taskBar.setVisibility(View.INVISIBLE);
            } else {
                taskBar.setVisibility(View.VISIBLE);
            }
            isTaskBarVisible[0] = !isTaskBarVisible[0];
        });

        // Xử lý ẩn taskBar khi nhấn bút Back (taskBar đang mở)
        rootLayout.setFocusableInTouchMode(true);
        rootLayout.requestFocus();
        rootLayout.setOnKeyListener((view, i, keyEvent) -> {
            if (i == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_DOWN
                && taskBar.getVisibility() == View.VISIBLE) {
                taskBar = findViewById(R.id.taskBar);
                taskBar.setVisibility(View.INVISIBLE);
                return true;
            }
            return false;
        });

        addCityBtn.setOnClickListener(view -> {
            Intent addCityIntent = new Intent(MainActivity.this, AddCityActivity.class);
            taskBar.setVisibility(View.INVISIBLE);
            startActivity(addCityIntent);
        });

    }
    public String roundedDouble(double value) {
        DecimalFormat df = new DecimalFormat("#");
        String formattedValue = df.format(value);
        return formattedValue;
    }

}