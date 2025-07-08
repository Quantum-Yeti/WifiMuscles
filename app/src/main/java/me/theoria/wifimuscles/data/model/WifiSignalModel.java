package me.theoria.wifimuscles.data.model;

/**
 * Model class that holds properties of Wifi information.
 */
public class WifiSignalModel {

    // Timestamp for when reading was taken.
    private final long timestamp;

    // Received Signal Strength Indicator (RSSI) in dBm.
    private final int rssi;

    // Simplified signal strength level (e.g., 1 to 5).
    private final int signalLevel;

    // Frequency of the WiFi channel (in MHz)
    private final int frequency;

    // IP address of the connected device (represented as an integer).
    private final int ip;

    // ID of the current connected network.
    private final int networkID;

    // Human-readable name of the WiFi network.
    private final String ssid;
    private final String bssid;

    // Speed of the connection in Mbps.
    private final int linkSpeed;
    private final int maxLinkSpeed;

    private final String wifiStandard;

    // Count of nearby overlapping networks (potential interference).
    private final int interferenceLevel;

    /**
     * Constructor that creates all the WifiSignalModel data.
     */
    public WifiSignalModel(long timestamp,
                           int rssi,
                           int signalLevel,
                           int frequency,
                           int ip,
                           int networkID,
                           String ssid,
                           String bssid,
                           int linkSpeed,
                           int maxLinkSpeed,
                           String wifiStandard,
                           int interferenceLevel) {
        this.timestamp = timestamp;
        this.rssi = rssi;
        this.signalLevel = signalLevel;
        this.frequency = frequency;
        this.ip = ip;
        this.networkID = networkID;
        this.ssid = ssid;
        this.bssid = bssid;
        this.linkSpeed = linkSpeed;
        this.maxLinkSpeed = maxLinkSpeed;
        this.wifiStandard = wifiStandard;
        this.interferenceLevel = interferenceLevel;
    }

    // Getters
    public long getTimestamp() {
        return timestamp;
    }

    public int getRssi() {
        return rssi;
    }

    public int getSignalLevel() {
        return signalLevel;
    }

    public int getIP() {
        return ip;
    }

    public int getFrequency() {
        return frequency;
    }

    public int getNetworkID() {
        return networkID;
    }

    public String getSSIDText() {
        return ssid;
    }

    public String getBssid() {
        return bssid;
    }

    public int getLinkSpeed() {
        return linkSpeed;
    }

    public int getMaxLinkSpeed() {
        return maxLinkSpeed;
    }

    public String getWifiStandard() {
        return wifiStandard;
    }

    public int getInterferenceLevel() {
        return interferenceLevel;
    }
}
