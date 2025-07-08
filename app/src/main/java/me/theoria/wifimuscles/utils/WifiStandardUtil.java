package me.theoria.wifimuscles.utils;

import android.net.wifi.WifiInfo;
import android.os.Build;

public class WifiStandardUtil {

    public static final int WIFI_STANDARD_UNKNOWN = 0;
    public static final int WIFI_STANDARD_LEGACY = 1;
    public static final int WIFI_STANDARD_11N = 4;
    public static final int WIFI_STANDARD_11AC = 5;
    public static final int WIFI_STANDARD_11AX = 6;

    public static String getWifiStandardName(WifiInfo info) {
        if (info == null) return "Unknown";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            switch (info.getWifiStandard()) {
                case WIFI_STANDARD_11AX:
                    return "Wi-Fi 6 (802.11ax)";
                case WIFI_STANDARD_11AC:
                    return "Wi-Fi 5 (802.11ac)";
                case WIFI_STANDARD_11N:
                    return "Wi-Fi 4 (802.11n)";
                case WIFI_STANDARD_LEGACY:
                    return "Legacy (pre-802.11n)";
                default:
                    return "Unknown";
            }
        }
        return "Unknown";
    }
}

