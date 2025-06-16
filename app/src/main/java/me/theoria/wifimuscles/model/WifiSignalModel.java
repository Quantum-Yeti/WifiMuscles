package me.theoria.wifimuscles.model;

import androidx.annotation.NonNull;

public class WifiSignalModel {
    private final long timestamp;
    private final int rssi;
    private final int signalLevel;
    private final String currentSSID;

    public WifiSignalModel(long timestamp, int rssi, int signalLevel, @NonNull String currentSSID) {
        this.timestamp = timestamp;
        this.rssi = rssi;
        this.signalLevel = signalLevel;
        this.currentSSID = currentSSID;
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

    public String getCurrentSSID() {
        return currentSSID;
    }
}
