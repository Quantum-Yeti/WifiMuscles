package me.theoria.wifimuscles.model;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class WiFiRepository {

    private final WifiManager wifiManager;

    public WiFiRepository(Context context) {
        this.wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    public int getCurrentRssi() {
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo != null ? wifiInfo.getRssi() : -100;
    }

}
