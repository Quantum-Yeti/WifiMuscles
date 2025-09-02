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
            case "Private IP":
                return context.getString(R.string.private_ip_txt);
            case "BSSID":
                return context.getString(R.string.bssid_txt);
            case "Link Speed":
                return context.getString(R.string.link_speed_txt);
            case "Interference":
                return context.getString(R.string.interference_descript);
            case "Max Speed":
                return context.getString(R.string.max_link_speed_txt);
            case "Ping":
                return context.getString(R.string.ping_txt);
            case "Channel":
                return context.getString(R.string.channel_txt);
            default:
                return "No description available.";
        }
    }
}
