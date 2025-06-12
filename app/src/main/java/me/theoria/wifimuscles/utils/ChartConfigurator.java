package me.theoria.wifimuscles.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;

import me.theoria.wifimuscles.R;
import me.theoria.wifimuscles.model.ChartMarkerView;
import me.theoria.wifimuscles.model.RSSILevel;

public class ChartConfigurator {

    public static class ChartSetupResult {
        public LineChart chart;
        public LineDataSet primaryDataSet, excellentSet, goodSet, fairSet, weakSet, terribleSet;
        public LineData lineData;
    }

    public static int mapRssiToLevel(int rssi) {
        if (rssi >= -50) return 5;
        else if (rssi >= -60) return 4;
        else if (rssi >= -70) return 3;
        else if (rssi >= -80) return 2;
        else return 1;
    }

    public static void addRssiEntry(LineDataSet dataSet, float x, int rssi) {
        int signalLevel = mapRssiToLevel(rssi);
        Entry entry = new Entry(x, signalLevel);
        entry.setData("RSSI: " + rssi + " dBm | Level: " + signalLevel);
        dataSet.addEntry(entry);
    }

    /**
     * Method: setupLineChart
     * Configures and initializes a chart with gradient, marker tooltips,
     * and wraps the result in the object ChartSetupResult
     * @param chart
     * @param context
     * @return
     */
    public static ChartSetupResult setupLineChart(LineChart chart, Context context) {
        // Chart configuration
        chart.setDrawGridBackground(false);
        chart.setBackgroundColor(Color.TRANSPARENT);
        chart.getDescription().setEnabled(true);
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.getLegend().setEnabled(true);
        chart.getLegend().setTextSize(14f);
        chart.getDescription().setEnabled(true);

        // Configure X Axis
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(true);
        Description description = new Description();
        description.setText("(Seconds Elapsed)");
        description.setTextSize(14f);
        chart.setDescription(description);


        // Configure Y Axis for signal levels
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setAxisMinimum(1f);
        leftAxis.setAxisMaximum(5f);
        leftAxis.setGranularity(1f);
        leftAxis.setLabelCount(5, true);
        leftAxis.setDrawGridLines(true);
        leftAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                switch ((int) value) {
                    case 5: return "5";
                    case 4: return "4";
                    case 3: return "3";
                    case 2: return "2";
                    case 1: return "1";
                    default: return "";
                }
            }
        });
        chart.getAxisLeft().setTextSize(14f);
        leftAxis.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        chart.getAxisRight().setEnabled(false);

        // Main signal level line
        LineDataSet rssiValueDataSet = new LineDataSet(new ArrayList<>(), "WiFi Signal Level (1–5)");
        rssiValueDataSet.setDrawCircles(false);
        rssiValueDataSet.setDrawValues(true);
        rssiValueDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        rssiValueDataSet.setCubicIntensity(0.2f);
        rssiValueDataSet.setColor(Color.BLACK);
        rssiValueDataSet.setLineWidth(2f);
        rssiValueDataSet.setDrawFilled(true);

        Drawable gradientFill = ContextCompat.getDrawable(context, R.drawable.chart_fill_gradient);
        rssiValueDataSet.setFillDrawable(gradientFill);

        // Marker view setup
        ChartMarkerView marker = new ChartMarkerView(context, R.layout.marker_rssi);
        marker.setChartView(chart);
        chart.setMarker(marker);

        // Threshold lines by signal level
        LineDataSet excellentSet = createLineThresholdDataSet(context, RSSILevel.EXCELLENT);
        LineDataSet goodSet = createLineThresholdDataSet(context, RSSILevel.GOOD);
        LineDataSet fairSet = createLineThresholdDataSet(context, RSSILevel.FAIR);
        LineDataSet weakSet = createLineThresholdDataSet(context, RSSILevel.WEAK);
        LineDataSet terribleSet = createLineThresholdDataSet(context, RSSILevel.TERRIBLE);

        // Combine all datasets
        LineData lineData = new LineData();
        lineData.addDataSet(rssiValueDataSet);
        lineData.addDataSet(excellentSet);
        lineData.addDataSet(goodSet);
        lineData.addDataSet(fairSet);
        lineData.addDataSet(weakSet);
        lineData.addDataSet(terribleSet);

        chart.setData(lineData);
        chart.invalidate();

        // Return all components for access
        ChartSetupResult result = new ChartSetupResult();
        result.chart = chart;
        result.primaryDataSet = rssiValueDataSet;
        result.excellentSet = excellentSet;
        result.goodSet = goodSet;
        result.fairSet = fairSet;
        result.weakSet = weakSet;
        result.terribleSet = terribleSet;
        result.lineData = lineData;
        return result;
    }

    /**
     * Creates a threshold dataset with default styling for the given RSSI level.
     */
    private static LineDataSet createLineThresholdDataSet(Context context, RSSILevel level) {
        LineDataSet set = new LineDataSet(new ArrayList<>(), context.getString(level.getRssiLabel()));
        set.setColor(Color.BLACK);
        set.setLineWidth(3.5f);
        set.setDrawCircles(false);
        set.setDrawValues(false);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawFilled(true);
        return set;
    }


}


