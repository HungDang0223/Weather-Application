package com.example.weatherapplication.ViewComponent;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.weatherapplication.R;
import com.example.weatherapplication.URL.LinkAPI;

import org.json.JSONException;

public class AddCityActivity extends AppCompatActivity {

    EditText searchCityBar;
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

        setContentView(R.layout.activity_add_city);
        handleSearchBar();
    }

    public void searchCity(String city) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        LinkAPI searchWithCityNameAPI = new LinkAPI();
        searchWithCityNameAPI.setCity_URL(city);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, searchWithCityNameAPI.getCity_URL(), null,
            response -> {
                TextView tv;
                tv = findViewById(R.id.displayEditText);
                Double temp;
                try {
                    temp = response.getJSONObject("main").getDouble("temp");
                    tv.setText(temp.toString());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            },
            error -> {
                Toast.makeText(this, "Hãy nhập đúng tên thành phố!", Toast.LENGTH_SHORT).show();
            });
        requestQueue.add(jsonObjectRequest);
    }

    public void handleSearchBar() {
        searchCityBar = (EditText) findViewById(R.id.searchCityBar);
        TextView tv;
        tv = findViewById(R.id.displayEditText);
        searchCityBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    searchCity(searchCityBar.getText().toString());
                }
                return false;
            }
        });
    }

}
