package me.theoria.wifimuscles.utils;

import android.content.Context;

import me.theoria.wifimuscles.R;

public class ChartInfoUtils {

    public static String getDescriptionForKey(Context context, String title) {
        switch (title) {
            case "SSID":
                return context.getString(R.string.ssid_txt);
            case "RSSI":
                return context.getString(R.string.rssi_txt);
            case "Freq":
                return context.getString(R.string.freq_txt);
            case "Freq Band":
                return context.getString(R.string.freq_short_txt);
            case "AP IP":
                return context.getString(R.string.ipv4_txt);
            case "BSSID":
                return context.getString(R.string.bssid_txt);
            case "Link Speed":
                return context.getString(R.string.link_speed_txt);
            case "Max Speed":
                return context.getString(R.string.max_link_speed_txt);
            case "Ping":
                return context.getString(R.string.ping_txt);
            default:
                return "No description available.";
        }
    }
}
