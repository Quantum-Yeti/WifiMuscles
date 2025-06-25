package me.theoria.wifimuscles.data.model;

import java.util.List;

public class ConnectivityModel {

    public enum TransportType {
        WIFI, CELLULAR, ETHERNET, VPN, UNKNOWN
    }

    private final boolean hasInternet;
    private final boolean isValidated;
    private final boolean isMetered;
    private final TransportType transportType;
    private final int downstreamKbps;
    private final int upstreamKbps;

    private final String interfaceName;
    private final String domains;

    public ConnectivityModel (boolean hasInternet,
                              boolean isValidated,
                              boolean isMetered,
                              TransportType transportType,
                              int downstreamKbps,
                              int upstreamKbps,
                              String interfaceName,
                              String domains) {

        this.hasInternet = hasInternet;
        this.isValidated = isValidated;
        this.isMetered = isMetered;
        this.transportType = transportType;
        this.downstreamKbps = downstreamKbps;
        this.upstreamKbps = upstreamKbps;
        this.interfaceName = interfaceName;
        this.domains = domains;
    }

    public boolean hasInternet() {
        return hasInternet;
    }

    public boolean isValidated() {
        return isValidated;
    }

    public boolean isMetered() {
        return isMetered;
    }

    public TransportType getTransportType() {
        return transportType;
    }

    public int getDownstreamKbps() {
        return downstreamKbps;
    }

    public int getUpstreamKbps() {
        return upstreamKbps;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public String getDomains() {
        return domains;
    }

}
