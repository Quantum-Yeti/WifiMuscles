package me.theoria.wifimuscles.utils;

/**
 * IPToString is a reusable utility class to convert an integer IP address to a String.
 */
public class IpToString {

    /**
     * Method to convert an integer IP address to a String.
     *
     * @param ip
     * @return
     */
    public String IntIPToString(int ip) {
        return "IP: " + (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                ((ip >> 24) & 0xFF);
    }

}
