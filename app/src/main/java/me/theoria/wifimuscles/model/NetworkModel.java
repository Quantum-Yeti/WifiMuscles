package me.theoria.wifimuscles.model;

public class NetworkModel {

    // Reference objects
    private final String ssid;
    private final String bssid;
    private final int networkRssi;
    private final int signalLevel;
    private final int frequency;
    private final long timestamp;
    private final String capabilities;
    private final int channelWidth;
    private final int centerFreq0;
    private final int centerFreq1;
    private final boolean passPoint;
    private final boolean is80211mcResponder;
    private final int channelNumber;

    /**
     * Method for initializing the Model.
     *
     * @param timestamp
     * @param ssid
     * @param bssid
     * @param networkRssi
     * @param signalLevel
     * @param frequency
     * @param capabilities
     * @param channelWidth
     * @param centerFreq0
     * @param centerFreq1
     * @param passPoint
     * @param is80211mcResponder
     */
    public NetworkModel(long timestamp,
                        String ssid,
                        String bssid,
                        int networkRssi,
                        int signalLevel,
                        int frequency,
                        String capabilities,
                        int channelWidth,
                        int centerFreq0,
                        int centerFreq1,
                        boolean passPoint,
                        boolean is80211mcResponder,
                        int channelNumber
    ) {
        this.timestamp = timestamp;
        this.ssid = ssid;
        this.bssid = bssid;
        this.networkRssi = networkRssi;
        this.signalLevel = signalLevel;
        this.frequency = frequency;
        this.capabilities = capabilities;
        this.channelWidth = channelWidth;
        this.centerFreq0 = centerFreq0;
        this.centerFreq1 = centerFreq1;
        this.passPoint = passPoint;
        this.is80211mcResponder = is80211mcResponder;
        this.channelNumber = channelNumber;
    }

    // Object getters
    public String getSsid() {
        return ssid;
    }

    public String getBssid() {
        return bssid;
    }

    public int getNetworkRssi() {
        return networkRssi;
    }

    public int getSignalLevel() {
        return signalLevel;
    }

    public int getFrequency() {
        return frequency;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getCapabilities() {
        return capabilities;
    }

    public int getChannelWidth() {
        return  channelWidth;
    }

    public int getCenterFreq0() {
        return centerFreq0;
    }

    public int getCenterFreq1() {
        return centerFreq1;
    }

    public boolean getPassPoint() {
        return passPoint;
    }
    public boolean getIs80211mcResponder() {
        return  is80211mcResponder;
    }

    public int getChannelNumber() {
        return channelNumber;
    }
}
