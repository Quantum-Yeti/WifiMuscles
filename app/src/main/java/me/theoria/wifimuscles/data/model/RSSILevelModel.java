package me.theoria.wifimuscles.data.model;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import me.theoria.wifimuscles.R;

/**
 * Enum to represent Wifi RSSI data mapped to strength levels.
 */
public enum RSSILevelModel {

    // Bind RSSI String levels to drawables.
    EXCELLENT(R.string.label_excellent, R.drawable.fill_blue), // 0 to -50
    GOOD(R.string.label_good, R.drawable.fill_green), // -50 to -70
    FAIR(R.string.label_fair, R.drawable.fill_yellow), // -70 to -80
    WEAK(R.string.label_weak, R.drawable.fill_red), // -80 to -90
    UNUSABLE(R.string.label_unusable, R.drawable.fill_red); // < -90

    // Resource ID for the string labels.
    @StringRes
    private final int wifiStrengthLabel;

    // Resource ID for the drawables.
    @DrawableRes
    private final int fillDrawableRes;

    /**
     * Constructor for the enum values.
     *
     * @param wifiStrengthLabel
     * @param fillDrawableRes
     */
    RSSILevelModel(@StringRes int wifiStrengthLabel, @DrawableRes int fillDrawableRes) {
        this.wifiStrengthLabel = wifiStrengthLabel;
        this.fillDrawableRes = fillDrawableRes;
    }

    /**
     * String resource ID corresponding to the RSSI level.
     *
     * @return
     */
    public int getRssiLabel() {
        return wifiStrengthLabel;
    }

    /**
     * Drawable resource ID for chart fill.
     *
     * @return
     */
    public int getFillDrawableRes() {
        return fillDrawableRes;
    }

    /**
     * Method to map RSSI float to a simple threshold level as an enum.
     *
     * @param rssi
     * @return
     */
    public static RSSILevelModel mapRssi (float rssi) {
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

