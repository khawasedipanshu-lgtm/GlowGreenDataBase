package com.example.glowgreendatabase;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    private TextView txtName;
    private EditText etSearch;

    private CardView cardCrop;
    private CardView cardWeather;
    private CardView cardMarket;
    private CardView cardProfile;

    private BottomNavigationView bottomNavigationView;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initializeViews();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Display logged-in user name
        String name = preferences.getString("name", "Farmer");
        txtName.setText(name);

        // Crop
        cardCrop.setOnClickListener(v -> {

            Toast.makeText(this,
                    "Crop Recommendation",
                    Toast.LENGTH_SHORT).show();

             startActivity(new Intent(HomeActivity.this,
             CropActivity.class));

        });

        // Weather
        cardWeather.setOnClickListener(v -> {

            Toast.makeText(this,
                    "Weather",
                    Toast.LENGTH_SHORT).show();

             startActivity(new Intent(HomeActivity.this,
             WeatherActivity.class));

        });

        // Market
        cardMarket.setOnClickListener(v -> {

            Toast.makeText(this,
                    "Market Prices",
                    Toast.LENGTH_SHORT).show();

             startActivity(new Intent(HomeActivity.this,
             MarketActivity.class));

        });

        // Profile
        cardProfile.setOnClickListener(v -> {

            Intent intent = new Intent(
                    HomeActivity.this,
                    ProfileActivity.class);

            startActivity(intent);

        });

        // Bottom Navigation
        bottomNavigationView.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_home) {

                return true;

            } else if (id == R.id.nav_crop) {

                Toast.makeText(this,
                        "Crop",
                        Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(
                        HomeActivity.this,
                        CropActivity.class);
                startActivity(intent);
                return true;

            } else if (id == R.id.nav_weather) {

                Toast.makeText(this,
                        "Weather",
                        Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(
                        HomeActivity.this,
                        WeatherActivity.class);

                startActivity(intent);

                return true;

            } else if (id == R.id.nav_profile) {

                Intent intent = new Intent(
                        HomeActivity.this,
                        ProfileActivity.class);

                startActivity(intent);

                return true;
            }

            return false;
        });

        // Search
        etSearch.setOnEditorActionListener((v, actionId, event) -> {

            String search = etSearch.getText().toString().trim();

            if (!search.isEmpty()) {

                Toast.makeText(this,
                        "Searching : " + search,
                        Toast.LENGTH_SHORT).show();

            }

            return true;
        });

    }

    private void initializeViews() {

        txtName = findViewById(R.id.txtName);

        etSearch = findViewById(R.id.etSearch);

        cardCrop = findViewById(R.id.cardCrop);

        cardWeather = findViewById(R.id.cardWeather);

        cardMarket = findViewById(R.id.cardMarket);

        cardProfile = findViewById(R.id.cardProfile);

        bottomNavigationView = findViewById(R.id.bottomNavigation);

    }

}