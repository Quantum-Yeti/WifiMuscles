package me.theoria.wifimuscles.model;

public class WifiSignal {
    private final long timestamp;
    private final int rssi;
    private final int signalLevel;

    public WifiSignal(long timestamp, int rssi, int signalLevel) {
        this.timestamp = timestamp;
        this.rssi = rssi;
        this.signalLevel = signalLevel;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getRssi() {
        return rssi;
    }
    public int getSignalLevel() {return signalLevel;}
}
