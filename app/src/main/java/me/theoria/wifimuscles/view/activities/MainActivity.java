package me.theoria.wifimuscles.view.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import com.google.android.gms.ads.AdView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import me.theoria.wifimuscles.R;
import me.theoria.wifimuscles.data.managers.AdManager;
import me.theoria.wifimuscles.data.managers.NavigationManager;
import me.theoria.wifimuscles.view.fragments.ChartFragment;

public class MainActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Location + Wifi Permission Check
        checkLocationPermission();
        checkWifiAndLocationEnabled();


        // Fragment declaration
        Fragment mainFragment = new ChartFragment();


        // Set current fragment from setCurrentFragment helper
        setCurrentFragment(mainFragment);

        // Initialize navigation from NavigationManager
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        NavigationManager navigationManager = new NavigationManager(this, getSupportFragmentManager());
        navigationManager.setupNavigation(bottomNavigationView, R.id.fragment_container);

        // Initialize Google AdView
        AdView mAdView = findViewById(R.id.adView);
        AdManager.initAds(this, mAdView);

    }

    /**
     * Method: setCurrentFragment
     * Description: Replaces the fragment within the fragment container block from
     * the main activity.
     * @param fragment
     */
    private void setCurrentFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private void checkWifiAndLocationEnabled() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null && !wifiManager.isWifiEnabled()) {
            Toast.makeText(this, "Wi-Fi is required. Please enable Wi-Fi.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
        }
    }


    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }
}


