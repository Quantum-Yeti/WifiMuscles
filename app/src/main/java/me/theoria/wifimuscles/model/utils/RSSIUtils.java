package me.theoria.wifimuscles.model.utils;

import me.theoria.wifimuscles.R;

public class RSSIUtils {

    /**
     * Method: mapRssiToLevels
     * This method takes the rssi reading and simplifies it to a one through five scale.
     * @param rssi
     * @return
     */
    public static Integer mapRssiToLevels(int rssi) {
        return rssi;
    }

    /**
     * Method: getRssiEmoji
     * This method takes the rssi reading and displays an emoji based on a one through five scale.
     * @param rssi
     * @return
     */
    public static int getRssiEmoji(int rssi) {
        if (rssi >= -30) {
            return R.drawable.emoji_mood_very_satisfied_24;
        } else if (rssi >= -50) {
            return R.drawable.emoji_mood_satisfied_24;
        } else if (rssi >= -70){
            return R.drawable.emoji_mood_neutral_24;
        } else if (rssi >= -90) {
            return R.drawable.emoji_mood_dissatisfied_24;
        } else return R.drawable.emoji_mood_bad_24;
    }
}
