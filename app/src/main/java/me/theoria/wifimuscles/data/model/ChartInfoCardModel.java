package me.theoria.wifimuscles.data.model;

import java.util.Objects;

public class ChartInfoCardModel {
    public final String title;
    public final String value;
    public final int iconRes;
    public int emojiRes;

    public ChartInfoCardModel(String title, String value, int iconRes) {
        this(title, value, iconRes, 0);
    }


    public ChartInfoCardModel(String title, String value, int iconRes, int emojiRes) {
        this.title = title;
        this.value = value;
        this.iconRes = iconRes;
        this.emojiRes = emojiRes;

    }

    // -- Getters --//
    public String getTitle() {
        return title;
    }

    public String getValue() {
        return value;
    }

    public int getIconResId() {
        return iconRes;
    }

    public int getEmojiRes() {
        return emojiRes;
    }

    // -- DiffUtils Hashcode --//
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChartInfoCardModel)) return false;

        ChartInfoCardModel that = (ChartInfoCardModel) o;
        return iconRes == that.iconRes &&
                emojiRes == that.emojiRes &&
                Objects.equals(title, that.title) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + value.hashCode();
        result = 31 * result + iconRes;
        result = 31 * result + emojiRes;
        return result;
    }



}
