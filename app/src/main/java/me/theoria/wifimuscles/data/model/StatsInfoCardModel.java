package me.theoria.wifimuscles.data.model;

import android.view.View;

import java.util.Objects;

public class StatsInfoCardModel {

    private final String title;
    private final String label;
    private final String value;
    private final View.OnClickListener onClickListener;

    public StatsInfoCardModel( String title, String label, String value, View.OnClickListener onClickListener) {
        this.title = title;
        this.label = label;
        this.value = value;
        this.onClickListener = onClickListener;
    }


    // -- Getters --//
    public String getTitle() {
        return title;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    // -- DiffUtils Hashcode --//
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatsInfoCardModel that = (StatsInfoCardModel) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(label, that.label) &&
                Objects.equals(value, that.value); // null safety
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, label, value);
    }
}
