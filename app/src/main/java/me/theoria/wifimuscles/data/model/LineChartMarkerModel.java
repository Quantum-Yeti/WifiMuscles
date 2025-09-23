package me.theoria.wifimuscles.data.model;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
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
            String markerText;

            if (dataSetIndex == 0) {
                // RSSI dataset
                float rssi = e.getY();
                RSSIQualityModel level = RSSIQualityModel.mapRssiToStringLevel(rssi);
                markerText = (int) rssi + " dBm | " + level;
            } else if (dataSetIndex == 1) {
                // Interference dataset
                float interference = e.getY();
                String interferenceLevel = getInterferenceLevel(interference);
                markerText = (int) interference + "% | " + interferenceLevel;
            } else {
                markerText = "";
            }
            markerContent.setText(markerText);
        }
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 3f), -getHeight());
    }

    private String getInterferenceLevel(float interference) {
        if (interference <= 10) {
            return "Low Interference";
        } else if (interference <= 30) {
            return "Minor Interference";
        } else if (interference <= 60){
            return "Moderate Interference";
        } else if (interference <= 80) {
            return "High Interference";
        } else if (interference <= 100) {
            return "Severe Interference";
        } else return "Error";
    }

}
