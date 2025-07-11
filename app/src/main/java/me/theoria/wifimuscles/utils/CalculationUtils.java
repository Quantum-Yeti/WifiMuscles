package me.theoria.wifimuscles.utils;

import java.util.Locale;

public class CalculationUtils {

    /**
     * Utility method to convert an integer IP address to a String.
     *
     * @param ip Integer IP Address
     * @return String representation of integer IP Address
     */
    public static String intIPToString(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                ((ip >> 24) & 0xFF);
    }

    /**
     * Utility method to convert and display throughput.
     * @param kbps
     * @return
     */
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

    /**
     * Utility method to convert frequency to common references.
     * @param mhz
     * @return
     */
    public static String fqToGhz (int mhz) {
        if (mhz >= 2400 && mhz <= 2500) {
            return "2.4 GHz";
        } else if (mhz >= 4900 && mhz <= 5900) {
            return "5 GHz";
        } else if (mhz >= 5925) {
            return "6 GHz";
        } else return "Unknown Band";
    }

    /**
     * Utility method to return a simple strength level from the RSSI.
     * @param rssi
     * @return
     */
    public static int convertRssiToLevel(int rssi) {
        if (rssi >= -50) {
            return 5;
        } else if (rssi >= -60) {
            return 4;
        } else if (rssi >= -70) {
            return 3;
        } else if (rssi >= -80) {
            return 2;
        } else {
            return 1;
        }
    }


}
