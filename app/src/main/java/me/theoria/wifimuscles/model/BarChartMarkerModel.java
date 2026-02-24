package me.theoria.wifimuscles.model;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import me.theoria.wifimuscles.R;

public class BarChartMarkerModel extends MarkerView {

    private final TextView markerContent;

    public BarChartMarkerModel(Context context, int layoutResource) {
        super(context, layoutResource);
        markerContent = findViewById(R.id.markerContent);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        if (e != null) {
            // display RSSI value
            float originalRssi = e.getY();

            // Map signal level
            RSSIQualityModel level = RSSIQualityModel.mapRssiToStringLevel(originalRssi);

            // Set display text
            String rssiMarker = (int) originalRssi + " dBm | " + level.name();
            markerContent.setText(rssiMarker);
        }
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2f), -getHeight());
    }
}
