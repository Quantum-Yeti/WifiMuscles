package me.theoria.wifimuscles.model;

public class WifiSignal {
    private final long timestamp;
    private final int rssi;

    public WifiSignal(long timestamp, int rssi) {
        this.timestamp = timestamp;
        this.rssi = rssi;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getRssi() {
        return rssi;
    }
}
