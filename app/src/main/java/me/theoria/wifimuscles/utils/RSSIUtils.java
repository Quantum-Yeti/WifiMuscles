package me.theoria.wifimuscles.utils;

import me.theoria.wifimuscles.R;

/**
 * RSSIUtilis is a reusable utility class for handling RSSI values and
 * provide methods to map the RSSI values to simplified threshold levels and
 * display an emoji representing the quality of the value.
 */
public class RSSIUtils {

    /**
     * Maps an RSSI integer value to a threshold value.
     * TODO: Extend rssi to a 1-5 scale.
     *
     * @param rssi
     * @return
     */
    public static Integer mapRssiToLevels(int rssi) {
        // TODO: Create logic to map RSSI to 1-5 scale.
        return rssi;
    }

    /**
     * Method to return an emoji based on the RSSI signal quality.
     *
     * - Very strong signal (>= -30 dBm) => Very satisfied emoji
     * - Strong signal (>= -50 dBm) => Satisfied emoji
     * - Moderate signal (>= -70 dBm) => Neutral emoji
     * - Weak signal (>= -90 dBm) => Dissatisfied emoji
     * - Very weak signal (< -90 dBm) => Bad emoji
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
