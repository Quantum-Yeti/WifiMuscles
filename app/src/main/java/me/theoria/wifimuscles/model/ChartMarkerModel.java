package me.theoria.wifimuscles.model;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import me.theoria.wifimuscles.R;
import me.theoria.wifimuscles.utils.RssiUtils;

public class ChartMarkerModel extends MarkerView {

    private final TextView markerContent;

    public ChartMarkerModel(Context context, int layoutResource) {
        super(context, layoutResource);
        markerContent = findViewById(R.id.markerContent);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        float rssi = e.getY();
        String strengthMarkerDescription = String.valueOf(RSSILevelModel.mapRssi(rssi));
        markerContent.setText("Strength: " + strengthMarkerDescription);
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 3f), -getHeight());
    }
}
