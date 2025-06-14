package me.theoria.wifimuscles.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;

import me.theoria.wifimuscles.R;
import me.theoria.wifimuscles.model.ChartMarkerModel;
import me.theoria.wifimuscles.model.RSSILevelModel;

public class ChartConfig {

    public static class ChartSetupResult {
        public LineChart chart;
        public LineDataSet primaryDataSet, excellentSet, goodSet, fairSet, weakSet, unusableSet;
        public LineData lineData;
    }

    public static ChartSetupResult setupChartConfig(LineChart chart, Context context, String currentSSID) {
        configureChart(chart, currentSSID);
        configureXAxis(chart.getXAxis());
        configureYAxis(chart.getAxisLeft(), context);
        chart.getAxisRight().setEnabled(false);

        LineDataSet rssiValueDataSet = createPrimaryDataSet(context);
        setMarkerView(chart, context);

        LineDataSet excellentSet = createLineThresholdDataSet(context, RSSILevelModel.EXCELLENT);
        LineDataSet goodSet = createLineThresholdDataSet(context, RSSILevelModel.GOOD);
        LineDataSet fairSet = createLineThresholdDataSet(context, RSSILevelModel.FAIR);
        LineDataSet weakSet = createLineThresholdDataSet(context, RSSILevelModel.WEAK);
        LineDataSet unusableSet = createLineThresholdDataSet(context, RSSILevelModel.UNUSABLE);

        LineData lineData = new LineData();
        lineData.addDataSet(rssiValueDataSet);
        lineData.addDataSet(excellentSet);
        lineData.addDataSet(goodSet);
        lineData.addDataSet(fairSet);
        lineData.addDataSet(weakSet);
        lineData.addDataSet(unusableSet);

        chart.setData(lineData);
        chart.invalidate();

        ChartSetupResult result = new ChartSetupResult();
        result.chart = chart;
        result.primaryDataSet = rssiValueDataSet;
        result.excellentSet = excellentSet;
        result.goodSet = goodSet;
        result.fairSet = fairSet;
        result.weakSet = weakSet;
        result.unusableSet = unusableSet;
        result.lineData = lineData;

        return result;
    }

    // Helper: Configure chart
    private static void configureChart(LineChart chart, String currentSSID) {
        chart.setDrawGridBackground(false);
        chart.setBackgroundColor(Color.TRANSPARENT);
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.getLegend().setEnabled(true);
        chart.getLegend().setTextSize(14f);
        chart.setExtraBottomOffset(10f);
        chart.setExtraTopOffset(10f);

        Description description = new Description();
        description.setText("Connected to: " + currentSSID);
        chart.setDescription(description);
    }

    // Helper: Configure X Axis
    private static void configureXAxis(XAxis xAxis) {
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        xAxis.setDrawLabels(false);
        xAxis.setDrawAxisLine(false);
    }

    // Helper: Configure Y Axis
    private static void configureYAxis(YAxis yAxis, Context context) {
        yAxis.setDrawGridLines(true);
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yAxis.setYOffset(-6f);
        yAxis.setAxisMinimum(1f);
        yAxis.setAxisMaximum(5f);
        yAxis.setGranularity(1f);
        yAxis.setLabelCount(5, true);
        yAxis.setTextSize(14f);
        yAxis.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        yAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                switch ((int) value) {
                    case 5: return "Excellent";
                    case 4: return "Good";
                    case 3: return "Fair";
                    case 2: return "Weak";
                    case 1: return "Unusable";
                    default: return "";
                }
            }
        });
    }

    // Helper: Create main dataset
    private static LineDataSet createPrimaryDataSet(Context context) {
        LineDataSet dataSet = new LineDataSet(new ArrayList<>(), context.getString(R.string.wifi_strength));
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        dataSet.setMode(LineDataSet.Mode.LINEAR);
        dataSet.setCubicIntensity(0.1f);
        dataSet.setColor(Color.BLACK);
        dataSet.setLineWidth(2.5f);
        dataSet.setDrawFilled(true);
        return dataSet;
    }

    // Helper: Set marker view
    private static void setMarkerView(LineChart chart, Context context) {
        ChartMarkerModel marker = new ChartMarkerModel(context, R.layout.marker_rssi);
        marker.setChartView(chart);
        chart.setMarker(marker);
    }

    // Helper: Create threshold lines
    private static LineDataSet createLineThresholdDataSet(Context context, RSSILevelModel level) {
        LineDataSet set = new LineDataSet(new ArrayList<>(), context.getString(level.getRssiLabel()));
        set.setColor(Color.BLACK);
        set.setLineWidth(3.5f);
        set.setDrawCircles(true);
        set.setDrawValues(false);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawFilled(true);
        return set;
    }
}


