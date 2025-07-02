package me.theoria.wifimuscles.data.model;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import me.theoria.wifimuscles.R;

/**
 * Enum to represent Wifi RSSI data mapped to strength levels.
 */
public enum RSSILevelModel {

    // Bind RSSI String levels to drawables for gradients.
    EXCELLENT(R.string.leve_excellent, R.drawable.fill_blue), // 0 to -50
    GOOD(R.string.level_good, R.drawable.fill_green), // -50 to -70
    FAIR(R.string.level_fair, R.drawable.fill_yellow), // -70 to -80
    WEAK(R.string.level_weak, R.drawable.fill_red), // -80 to -90
    UNUSABLE(R.string.level_unusable, R.drawable.fill_red); // < -90

    // Resource ID for the string labels.
    @StringRes
    private final int wifiStrengthLabel;

    // Resource ID for the drawables.
    @DrawableRes
    private final int fillDrawableRes;

    /**
     * Constructor for the enum values.
     *
     * @param wifiStrengthLabel String for the level (excellent, good, etc.)
     * @param fillDrawableRes Gradient drawables.
     */
    RSSILevelModel(@StringRes int wifiStrengthLabel, @DrawableRes int fillDrawableRes) {
        this.wifiStrengthLabel = wifiStrengthLabel;
        this.fillDrawableRes = fillDrawableRes;
    }

    /**
     * String resource ID corresponding to the RSSI level.
     *
     * @return the String for rssi level.
     */
    public int getRssiLabel() {
        return wifiStrengthLabel;
    }

    /**
     * Drawable resource ID for chart fill.
     *
     * @return the gradient based on the String RSSI level.
     */
    public int getFillDrawableRes() {
        return fillDrawableRes;
    }

    /**
     * Method to map RSSI float to a simple threshold level as an enum.
     *
     * @param rssi raw rssi reading
     * @return an enum based on the rssi reading
     */
    public static RSSILevelModel mapRssiToStringLevel(float rssi) {
        if (rssi >= -50) {
            return EXCELLENT;
        } else if (rssi >= -60) {
            return GOOD;
        } else if (rssi >= -70) {
            return FAIR;
        } else if (rssi >= -80) {
            return WEAK;
        } else {
            return UNUSABLE;
        }
    }
}

