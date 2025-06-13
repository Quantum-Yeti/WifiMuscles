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
import me.theoria.wifimuscles.model.ChartMarkerModel;
import me.theoria.wifimuscles.model.RSSILevelModel;

public class ChartConfigurator {

    public static class ChartSetupResult {
        public LineChart chart;
        public LineDataSet primaryDataSet, excellentSet, goodSet, fairSet, weakSet, terribleSet;
        public LineData lineData;
    }



    public static void addRssiEntry(LineDataSet dataSet, float x, int rssi) {
        int signalLevel = RssiUtils.mapRssiToLevels(rssi);
        Entry entry = new Entry(x, signalLevel);
        entry.setData(RssiUtils.mapRssiToLevels(rssi));
        dataSet.addEntry(entry);
    }

    /**
     * Method: setupChartConfig
     * Configures and initializes a chart with gradient, marker tooltips,
     * and wraps the result in the object ChartSetupResult
     *
     * @param chart
     * @param context
     * @return
     */
    public static ChartSetupResult setupChartConfig(LineChart chart, Context context, String currentSSID) {
        // Chart configuration
        chart.setDrawGridBackground(false);
        chart.setBackgroundColor(Color.TRANSPARENT);
        //chart.getDescription().setEnabled(true);
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.getLegend().setEnabled(true);
        //chart.getLegend().getEntries();
        chart.getLegend().setTextSize(14f);
        chart.setExtraBottomOffset(10f);
        chart.setExtraTopOffset(10f);
        //chart.getDescription().setEnabled(true);

        // Configure X Axis
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setSpaceMax(5f);
        xAxis.setDrawGridLines(true);
        xAxis.setDrawLabels(false);
        xAxis.setDrawAxisLine(false);
        /*Description description = new Description();
        description.setText("(Seconds)");*/
        /*description.setTextSize(14f);
        chart.setDescription(description);*/

        // Test current SSID name description
        Description description = new Description();
        description.setText("Connected to: " +currentSSID);
        chart.setDescription(description);

        // Configure Y Axis for signal levels
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        //leftAxis.setSpaceTop(2f);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        leftAxis.setYOffset(-6f);
        leftAxis.setAxisMinimum(1f);
        leftAxis.setAxisMaximum(5f);
        leftAxis.setGranularity(1f);
        leftAxis.setLabelCount(5, true);
        leftAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                switch ((int) value) {
                    case 5: return "Excellent";
                    case 4: return "Good";
                    case 3: return "Fair";
                    case 2: return "Weak";
                    case 1: return "Terrible";
                    default: return "";
                }
            }
        });
        chart.getAxisLeft().setTextSize(14f);
        leftAxis.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        chart.getAxisRight().setEnabled(false);

        // Main signal level line
        LineDataSet rssiValueDataSet = new LineDataSet(new ArrayList<>(), context.getString(R.string.wifi_strength));
        rssiValueDataSet.setDrawCircles(false);
        rssiValueDataSet.setDrawValues(false);
        rssiValueDataSet.setMode(LineDataSet.Mode.LINEAR);
        rssiValueDataSet.setCubicIntensity(0.1f);
        rssiValueDataSet.setColor(Color.BLACK);
        rssiValueDataSet.setLineWidth(2.5f);
        rssiValueDataSet.setDrawFilled(true);

        //Test gradient fill
        /*Drawable gradientFill = ContextCompat.getDrawable(context, R.drawable.chart_fill_gradient);
        rssiValueDataSet.setFillDrawable(gradientFill);*/

        // Marker view setup
        ChartMarkerModel marker = new ChartMarkerModel(context, R.layout.marker_rssi);
        marker.setChartView(chart);
        chart.setMarker(marker);

        // Threshold lines by signal level
        LineDataSet excellentSet = createLineThresholdDataSet(context, RSSILevelModel.EXCELLENT);
        LineDataSet goodSet = createLineThresholdDataSet(context, RSSILevelModel.GOOD);
        LineDataSet fairSet = createLineThresholdDataSet(context, RSSILevelModel.FAIR);
        LineDataSet weakSet = createLineThresholdDataSet(context, RSSILevelModel.WEAK);
        LineDataSet terribleSet = createLineThresholdDataSet(context, RSSILevelModel.TERRIBLE);

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
    private static LineDataSet createLineThresholdDataSet(Context context, RSSILevelModel level) {
        LineDataSet set = new LineDataSet(new ArrayList<>(), context.getString(level.getRssiLabel()));
        set.setColor(Color.BLACK);
        set.setLineWidth(3.5f);
        set.setDrawCircles(false);
        set.setDrawValues(false);
        set.setMode(LineDataSet.Mode.LINEAR);
        set.setDrawFilled(true);
        return set;
    }


}


