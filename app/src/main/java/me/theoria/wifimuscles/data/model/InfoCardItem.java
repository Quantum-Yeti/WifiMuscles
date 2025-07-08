package me.theoria.wifimuscles.data.model;

public class InfoCardItem {
    public final String title;
    public final String value;
    public final int iconRes;
    public int emojiRes;

    public InfoCardItem(String title, String value, int iconRes) {
        this(title, value, iconRes, 0);
    }


    public InfoCardItem(String title, String value, int iconRes, int emojiRes) {
        this.title = title;
        this.value = value;
        this.iconRes = iconRes;
        this.emojiRes = emojiRes;

    }

    // Getters
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

}
