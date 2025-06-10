package me.theoria.wifimuscles.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.Locale;

import me.theoria.wifimuscles.R;
import me.theoria.wifimuscles.model.ChartMarkerView;
import me.theoria.wifimuscles.model.RSSILevel;

public class ChartConfigurator {

    public static class ChartSetupResult {
        public LineChart chart;
        public LineDataSet primaryDataSet, excellentSet, goodSet, fairSet, weakSet, terribleSet;
        public LineData lineData;
    }

    /**
     * Method: flipRSSI
     * Changes negative values to positive values for visual aspects of the charts
     */
    public static Entry flipRSSIEntry(float x, float rssi){
        Entry entry = new Entry(x, Math.abs(rssi));
        entry.setData(rssi);
        return entry;
    }

    /**
     * Function: createThresholdDataSet
     * Visually highlight quality wifi bands on a chart.
     * @param context
     * @param level
     * @return
     */
    private static LineDataSet createThresholdDataSet(Context context, RSSILevel level) {
        LineDataSet set = new LineDataSet(new ArrayList<>(), context.getString(level.getRssiLabel()));
        set.setDrawFilled(true);
        //set.setFillDrawable();
        set.setColor(Color.BLACK);
        set.setLineWidth(3f);
        set.setDrawCircles(false);
        set.setDrawValues(false);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        set.setFillDrawable(ContextCompat.getDrawable(context, R.drawable.chart_fill_gradient));
        set.setDrawFilled(true);
        return set;
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

        // Configure X Axis
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(true);

        // Configure Y Axis
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setAxisMinimum(-127f);
        leftAxis.setAxisMaximum(0f);
        leftAxis.setDrawGridLines(false);
        /*leftAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format(Locale.getDefault(), "-%.0f", value);
            }
        });*/
        chart.getAxisRight().setEnabled(false);

        // Primary LineDataSet RSSI
        LineDataSet rssiValueDataSet = new LineDataSet(new ArrayList<>(), "WiFi RSSI (dBm)");
        rssiValueDataSet.setDrawCircles(false);
        rssiValueDataSet.setDrawValues(true);
        rssiValueDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        rssiValueDataSet.setCubicIntensity(0.2f);
        rssiValueDataSet.setColor(Color.CYAN);
        rssiValueDataSet.setLineWidth(2f);

        // Enable and set gradient fill
        rssiValueDataSet.setDrawFilled(true);
        Drawable gradientFill = ContextCompat.getDrawable(context, R.drawable.chart_fill_gradient);
        rssiValueDataSet.setFillDrawable(gradientFill);


        // Add marker
        ChartMarkerView marker = new ChartMarkerView(context, R.layout.marker_rssi);
        marker.setChartView(chart);
        chart.setMarker(marker);

        // RSSI Thresholds from RSSILevel ENUM
        LineDataSet excellentSet = createLineThresholdDataSet(context, RSSILevel.EXCELLENT);
        LineDataSet goodSet = createLineThresholdDataSet(context, RSSILevel.GOOD);
        LineDataSet fairSet = createLineThresholdDataSet(context, RSSILevel.FAIR);
        LineDataSet weakSet = createLineThresholdDataSet(context, RSSILevel.WEAK);
        LineDataSet terribleSet = createLineThresholdDataSet(context, RSSILevel.TERRIBLE);

        LineData lineData = new LineData(rssiValueDataSet);
        lineData.addDataSet(rssiValueDataSet);
        lineData.addDataSet(excellentSet);
        lineData.addDataSet(goodSet);
        lineData.addDataSet(fairSet);
        lineData.addDataSet(weakSet);
        lineData.addDataSet(terribleSet);

        // Set data to chart and redraw chart
        chart.setData(lineData);
        chart.invalidate();

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

    private static LineDataSet createLineThresholdDataSet(Context context, RSSILevel level) {
        LineDataSet set = new LineDataSet(new ArrayList<>(), context.getString(level.getRssiLabel()));
        set.setColor(Color.BLACK);
        set.setLineWidth(3.5f);
        set.setDrawValues(true);
        set.setDrawCircles(false);
        set.setDrawValues(false);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawFilled(true);
        Drawable gradientFill = ContextCompat.getDrawable(context, R.drawable.chart_fill_gradient);
        set.setFillDrawable(gradientFill);
        set.setFillColor(Color.BLUE);
        return set;
    }

    public static void addRssiEntry(LineDataSet dataSet, float x, int rssi) {
        float flipped = Math.abs(rssi); // e.g. -85 becomes 85
        dataSet.addEntry(new Entry(x, flipped));
    }
}


