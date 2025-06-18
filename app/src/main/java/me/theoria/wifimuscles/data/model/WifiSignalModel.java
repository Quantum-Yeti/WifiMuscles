package me.theoria.wifimuscles.data.model;

public class WifiSignalModel {
    private final long timestamp;
    private final int rssi, signalLevel, frequency, ip, networkID;
    private final String ssid;

    public WifiSignalModel(long timestamp, int rssi, int signalLevel, int frequency, int ip, int networkID, String ssid) {
        this.timestamp = timestamp;
        this.rssi = rssi;
        this.signalLevel = signalLevel;
        this.frequency = frequency;
        this.ip = ip;
        this.networkID = networkID;
        this.ssid = ssid;
    }

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

    public String getSSIDText() {return ssid; }
}
