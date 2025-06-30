package me.theoria.wifimuscles.utils;

/**
 * IPToString is a reusable utility class to convert an integer IP address to a String.
 */
public class NumToStringUtils {

    /**
     * Method to convert an integer IP address to a String.
     *
     * @param ip Integer IP Address
     * @return String representation of integer IP Address
     */
    public static String intIPToString(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                ((ip >> 24) & 0xFF);
    }

}
