package me.theoria.wifimuscles.utils;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.os.Build;

import java.util.List;
import java.util.Locale;

public class CalculationUtils {

    public static class InterferenceResult {
        public final int interferenceCount;
        public final float interferencePercent;

        public InterferenceResult (int interferenceCount, float interferencePercent) {
            this.interferenceCount = interferenceCount;
            this.interferencePercent = interferencePercent;
        }
    }

    /**
     * Utility method to convert an integer IP address to a String.
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

    /**
     * Utility method to format Lease Duration into minutes and seconds.
     */
    public static String formatLeaseDuration(int totalSeconds) {
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        StringBuilder builder = new StringBuilder();
        if (hours > 0) {
            builder.append(hours).append(" hr ");
        }
        if (minutes > 0 || hours > 0) { // always show minutes if hours exist
            builder.append(minutes).append(" min ");
        }
        builder.append(seconds).append(" sec");

        return builder.toString().trim();
    }

    /**
     * Utility method to calculate the Wi-Fi channel number from the MHz frequency.
     * Calculation is based on common 2.4GHz, 5GHz, and 6GHz bands.
     * @param frequency Frequency in MHz.
     * @return Channel number or -1 if unknown.
     */
    public static int calculateChannel(int frequency) {
        // 2.4 GHz band (channels 1–13)
        // 802.11b/g/n
        if (frequency >= 2412 && frequency <= 2472) {
            return (frequency - 2407) / 5;
        }

        // Channel 14 (only used in Japan)
        if (frequency == 2484) {
            return 14;
        }

        // 5 GHz band (common channels: 36–165)
        // 802.11a/n/ac
        // TODO: May not map cleanly depending on region
        if (frequency >= 5180 && frequency <= 5825) {
            return (frequency - 5000) / 5;
        }

        // 6 GHz band (Wi-Fi 6E: channels 1–233)
        // 802.11ax (Wi-Fi 6E) & 7
        if (frequency >= 5955 && frequency <= 7115) {
            return ((frequency - 5955) / 5) + 1;
        }

        // Unknown frequency
        return -1;
    }

    /**
     * Method to return the wifi standard from the API.
     */
    public static String getWifiStandardName(WifiInfo info) {
        if (info == null) return "Unknown";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                int standard = info.getWifiStandard();
                switch (standard) {
                    case ScanResult.WIFI_STANDARD_11AX:
                        return "Wi-Fi 6 (802.11ax)";
                    case ScanResult.WIFI_STANDARD_11AC:
                        return "Wi-Fi 5 (802.11ac)";
                    case ScanResult.WIFI_STANDARD_11N:
                        return "Wi-Fi 4 (802.11n)";
                    case ScanResult.WIFI_STANDARD_LEGACY:
                        return "Legacy (pre-802.11n)";
                    default:
                        return "Unknown";
                }
            } catch (Exception e) {
                return "Unknown";
            }
        }
        return "Unknown";
    }

    /**
     * Calculates Wi-Fi interference based on overlapping frequencies and signal strength.
     * @param results List of all scan results.
     * @param selfInfo WifiInfo object representing current connection.
     * @return InterferenceResult containing count and percentage.
     */
    public static InterferenceResult calculateInterference(List<ScanResult> results, WifiInfo selfInfo) {
        if (results == null || selfInfo == null) return new InterferenceResult(0, 0f);

        int currentFreq = selfInfo.getFrequency();
        String currentBssid = selfInfo.getBSSID();

        int interferenceCount = 0;
        int totalNetworksScanned = 0;

        for (ScanResult result : results) {
            if (result.BSSID.equals(currentBssid)) continue;
            totalNetworksScanned++;

            if (isOverlapping(result.frequency, currentFreq) && result.level > -85) {
                interferenceCount++;
            }
        }

        float interferencePercentage = (totalNetworksScanned > 0) ? (interferenceCount * 100f / totalNetworksScanned) : 0f;
        return new InterferenceResult(interferenceCount, interferencePercentage);
    }

    private static boolean isOverlapping(int f1, int f2) {
        int diff = Math.abs(f1 - f2);

        if (is2_4GHz(f1, f2)) return diff <= 20;
        if (is5GHz(f1, f2)) return diff <= 40;
        if (is6GHz(f1, f2)) return diff <= 80;
        if (is7GHz(f1, f2)) return diff <= 160;
        return false;
    }

    // Interference Helper Methods for specific bands TODO: refactor for flexibility
    private static boolean is2_4GHz(int f1, int f2) {
        return (f1 >= 2400 && f1 <= 2500) && (f2 >= 2400 && f2 <= 2500);
    }

    private static boolean is5GHz(int f1, int f2) {
        return (f1 >= 4900 && f1 <= 5900) && (f2 >= 4900 && f2 <= 5900);
    }

    private static boolean is6GHz(int f1, int f2) {
        return (f1 >= 5925 && f1 <= 7125) && (f2 >= 5925 && f2 <= 7125);
    }

    private static boolean is7GHz(int f1, int f2) {
        return (f1 >= 7126 && f1 <= 7950) && (f2 >= 7126 && f2 <= 7950);
    }
}


