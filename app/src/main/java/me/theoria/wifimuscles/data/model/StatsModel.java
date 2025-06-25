package me.theoria.wifimuscles.data.model;

public class StatsModel {

    public static final int TYPE_INFO_CARD = 0;
    public static final int TYPE_NETWORK_CARD = 1;

    private int viewType;

    // DHCP fields
    private String ipAddress;
    private String gateway;
    private String netmask;
    private String dns1;
    private String dns2;
    private String serverAddress;
    private String leaseDuration;

    // Network fields
    private String level;
    private String capability;
    private String channel;
    private String centerFreq0;
    private String centerFreq1;
    private String passpoint;
    private String responder;

    public StatsModel(int viewType) {
        this.viewType = viewType;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    // DHCP getters/setters
    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getNetmask() {
        return netmask;
    }

    public void setNetmask(String netmask) {
        this.netmask = netmask;
    }

    public String getDns1() {
        return dns1;
    }

    public void setDns1(String dns1) {
        this.dns1 = dns1;
    }

    public String getDns2() {
        return dns2;
    }

    public void setDns2(String dns2) {
        this.dns2 = dns2;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public String getLeaseDuration() {
        return leaseDuration;
    }

    public void setLeaseDuration(String leaseDuration) {
        this.leaseDuration = leaseDuration;
    }

    // Network getters/setters
    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCapability() {
        return capability;
    }

    public void setCapability(String capability) {
        this.capability = capability;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getCenterFreq0() {
        return centerFreq0;
    }

    public void setCenterFreq0(String centerFreq0) {
        this.centerFreq0 = centerFreq0;
    }

    public String getCenterFreq1() {
        return centerFreq1;
    }

    public void setCenterFreq1(String centerFreq1) {
        this.centerFreq1 = centerFreq1;
    }

    public String getPasspoint() {
        return passpoint;
    }

    public void setPasspoint(String passpoint) {
        this.passpoint = passpoint;
    }

    public String getResponder() {
        return responder;
    }

    public void setResponder(String responder) {
        this.responder = responder;
    }
}
