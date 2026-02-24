package me.theoria.wifimuscles.core.permissions;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import me.theoria.wifimuscles.MainActivity;

public class Permission {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    public static void checkWifiAndLocationEnabled(MainActivity mainActivity) {

        WifiManager wifiManager = (WifiManager) mainActivity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            LocationManager locationManager = (LocationManager) mainActivity.getSystemService(Context.LOCATION_SERVICE);

            if (wifiManager != null && !wifiManager.isWifiEnabled()) {
                Toast.makeText(mainActivity, "Wi-Fi is required. Please enable Wi-Fi.", Toast.LENGTH_LONG).show();
                mainActivity.startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
            }

            boolean isLocationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isLocationEnabled) {
                Toast.makeText(mainActivity, "Location services are required. Please enable them,", Toast.LENGTH_LONG).show();
                mainActivity.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }

            if (ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        mainActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
}

