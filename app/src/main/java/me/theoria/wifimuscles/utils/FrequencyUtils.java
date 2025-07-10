package me.theoria.wifimuscles.utils;

public class FrequencyUtils {

    public static String fqToGhz (int mhz) {
        if (mhz >= 2400 && mhz <= 2500) {
            return "2.4 GHz";
        } else if (mhz >= 4900 && mhz <= 5900) {
            return "5 GHz";
        } else if (mhz >= 5925) {
            return "6 GHz";
        } else return "Unknown Band";
    }
}
