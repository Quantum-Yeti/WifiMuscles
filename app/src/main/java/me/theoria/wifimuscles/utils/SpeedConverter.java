package me.theoria.wifimuscles.utils;

import java.util.Locale;

public class SpeedConverter {

    public static String speedConvert(int kbps) {
        if (kbps >= 1000000) {
            double gbps = kbps / 1000000.0;
            return String.format(Locale.getDefault(), "%.2f Gbps", gbps);
        } else if (kbps >= 1000) {
            double mbps = kbps / 1000.0;
            return String.format(Locale.getDefault(), "%.2f Mbps", mbps);
        } else {
            return String.format(Locale.getDefault(), "%d Kbps", kbps);
        }
    }
}
