package com.example.weatherapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);
        splashScreen();
    }

    public void splashScreen() {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
                startActivity(new Intent(SplashScreen.this, MainActivity.class));
                finish();
        }, 4000);

    }

}
