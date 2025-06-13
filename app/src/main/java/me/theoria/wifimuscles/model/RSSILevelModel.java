package me.theoria.wifimuscles.model;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import me.theoria.wifimuscles.R;

public enum RSSILevelModel {

    EXCELLENT(R.string.label_excellent, R.drawable.fill_blue), // 0 to -50
    GOOD(R.string.label_good, R.drawable.fill_green), // -50 to -70
    FAIR(R.string.label_fair, R.drawable.fill_yellow), // -70 to -80
    WEAK(R.string.label_weak, R.drawable.fill_red), // -80 to -90
    TERRIBLE(R.string.label_terrible, R.drawable.fill_red); // < -90

    @StringRes
    private final int wifiStrengthLabel;
    @DrawableRes
    private final int fillDrawableRes;

    RSSILevelModel(@StringRes int wifiStrengthLabel, @DrawableRes int fillDrawableRes) {
        this.wifiStrengthLabel = wifiStrengthLabel;
        this.fillDrawableRes = fillDrawableRes;
    }

    public int getRssiLabel() {
        return wifiStrengthLabel;
    }

    public int getFillDrawableRes() {
        return fillDrawableRes;
    }

    public static RSSILevelModel mapRssi (float rssi) {
        if (rssi >= -30) {
            return EXCELLENT;
        } else if (rssi >= -50) {
            return GOOD;
        } else if (rssi >= -70) {
            return FAIR;
        } else if (rssi >= -90) {
            return WEAK;
        } else {
            return TERRIBLE;
        }
    }


}
