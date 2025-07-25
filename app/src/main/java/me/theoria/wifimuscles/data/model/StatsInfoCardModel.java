package me.theoria.wifimuscles.data.model;

import android.view.View;

import java.util.Objects;

public class StatsInfoCardModel {

    private final String label;
    private final String value;
    private final View.OnClickListener onClickListener;

    public StatsInfoCardModel(String label, String value, View.OnClickListener onClickListener) {
        this.label = label;
        this.value = value;
        this.onClickListener = onClickListener;
    }


    // -- Getters --//
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
        return Objects.equals(label, that.label) &&
                Objects.equals(value, that.value); // null safety
    }

    @Override
    public int hashCode() {
        int result = label.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }
}
