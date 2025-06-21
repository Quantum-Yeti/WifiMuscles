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

    // Speed of the connection in Mbps.
    private final int linkSpeed;

    // MAC address of the access point.
    private final String mac;

    /**
     * Constructor that creates all the WifiSignalModel data.
     *
     * @param timestamp
     * @param rssi
     * @param signalLevel
     * @param frequency
     * @param ip
     * @param networkID
     * @param ssid
     * @param linkSpeed
     * @param mac
     */
    public WifiSignalModel(long timestamp, int rssi, int signalLevel, int frequency, int ip, int networkID, String ssid, int linkSpeed, String mac) {
        this.timestamp = timestamp;
        this.rssi = rssi;
        this.signalLevel = signalLevel;
        this.frequency = frequency;
        this.ip = ip;
        this.networkID = networkID;
        this.ssid = ssid;
        this.linkSpeed = linkSpeed;
        this.mac = mac;
    }

    // Section for all getters.
    public long getTimestamp() {
        return timestamp;
    }

    public int getRssi() {
        return rssi;
    }

    public int getSignalLevel() {
        return signalLevel;
    }

    public int getIP() { return ip; }

    public int getFrequency() { return frequency; }

    public int getNetworkID() { return networkID; }

    public String getSSIDText() { return ssid; }

    public int getLinkSpeed() { return linkSpeed; }

    public String getMac() { return mac; }
}
