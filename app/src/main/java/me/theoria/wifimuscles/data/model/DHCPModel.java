package me.theoria.wifimuscles.data.model;

/**
 * Reference objects in model.
 */
public class DHCPModel {
    private final int gateway;
    private final int netmask;
    private final int dns1;
    private final int dns2;
    private final int serverAddress;
    private final int leaseDuration;

    public DHCPModel(
            int gateway,
            int netmask,
            int dns1,
            int dns2,
            int serverAddress,
            int leaseDuration
    ) {
        this.gateway = gateway;
        this.netmask = netmask;
        this.dns1 = dns1;
        this.dns2 = dns2;
        this.serverAddress = serverAddress;
        this.leaseDuration = leaseDuration;

    }

    // Object getters
    public int getGateway() {
        return gateway;
    }

    public int getNetmask() {
        return netmask;
    }

    public int getDns1() {
        return dns1;
    }

    public int getDns2() {
        return dns2;
    }

    public int getServerAddress() {
        return serverAddress;
    }

    public int getLeaseDuration() {
        return leaseDuration;
    }

}
