package me.theoria.wifimuscles.data.model;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import me.theoria.wifimuscles.R;

public class LineChartMarkerModel extends MarkerView {

    private final TextView markerContent;

    public LineChartMarkerModel(Context context, AttributeSet attrs) {
        super(context, R.layout.marker_view);
        markerContent = findViewById(R.id.markerContent);
    }

    public LineChartMarkerModel(Context context, int layoutResource) {
        super(context, layoutResource);
        markerContent = findViewById(R.id.markerContent);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        if (e != null) {

            // Identify the dataset being clicked
            int dataSetIndex = highlight.getDataSetIndex();

            // Initialize variables for RSSI and interference
            float rssi = e.getY();
            float interference = e.getY();
            RSSIQualityModel level = RSSIQualityModel.mapRssiToStringLevel(rssi);

            // String construction for both RSSI and interference
            String rssiMarker = (int) rssi + " dBm | " + level;
            String interferenceLevel = getInterferenceLevel(interference);

            // Concatenate both together
            String combinedGraphMarker = rssiMarker + "\n" + interferenceLevel;

            // Set combined concatenated values together
            markerContent.setText(combinedGraphMarker);
        }
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 3f), -getHeight());
    }

    private String getInterferenceLevel(float interference) {
        if (interference <= 30) {
            return "Low Interference";
        } else if (interference <= 60) {
            return "Medium Interference";
        } else {
            return "High Interference";
        }
    }

}
