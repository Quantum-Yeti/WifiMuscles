package me.theoria.wifimuscles.model.utils;

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

    public static ChartSetupResult setupChartConfig(LineChart chart, Context context) {

        configureChart(chart);
        configureXAxis(chart.getXAxis());
        configureYAxis(chart.getAxisLeft());
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

        // Currently empty datasets
        /*lineData.addDataSet(excellentSet);
        lineData.addDataSet(goodSet);
        lineData.addDataSet(fairSet);
        lineData.addDataSet(weakSet);
        lineData.addDataSet(unusableSet);*/

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

    private static void configureChart(LineChart chart) {
        chart.setDrawGridBackground(false);
        chart.setBackgroundColor(Color.TRANSPARENT);
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.getLegend().setEnabled(true);
        chart.getLegend().setTextSize(14f);
        chart.setExtraBottomOffset(10f);
        chart.setExtraTopOffset(10f);
        chart.setExtraLeftOffset(20f);
        chart.getLegend().setEnabled(false);

        Description description = new Description();
        description.setText("Connected to: ");
        chart.setDescription(description);
    }

    private static void configureXAxis(XAxis xAxis) {
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        xAxis.setDrawLabels(true);
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(5f);
        xAxis.setGranularityEnabled(true);
        xAxis.setLabelCount(8, false);
    }

    private static void configureYAxis(YAxis yAxis) {
        yAxis.setDrawGridLines(false);
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yAxis.setYOffset(0f);
        yAxis.setXOffset(0f);
        yAxis.setAxisMinimum(-100f); // dBm
        yAxis.setAxisMaximum(0f);
        yAxis.setGranularity(10f);
        yAxis.setLabelCount(5, true);
        yAxis.setTextSize(12f);
        yAxis.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        yAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return ((int) value) + " dBm";
            }
        });
    }

    private static LineDataSet createPrimaryDataSet(Context context) {
        LineDataSet dataSet = new LineDataSet(new ArrayList<>(), context.getString(R.string.wifi_strength));
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setCubicIntensity(0.3f);
        dataSet.setColor(Color.BLUE);
        dataSet.setLineWidth(2f);
        dataSet.setDrawFilled(false);
        dataSet.setDrawCircleHole(true);
        //dataSet.setFillColor(Color.parseColor("#05DA93")); // Solid fill
        return dataSet;
    }

    private static void setMarkerView(LineChart chart, Context context) {
        ChartMarkerModel marker = new ChartMarkerModel(context, R.layout.marker_rssi);
        marker.setChartView(chart);
        chart.setMarker(marker);
    }

    private static LineDataSet createLineThresholdDataSet(Context context, RSSILevelModel level) {
        LineDataSet set = new LineDataSet(new ArrayList<>(), context.getString(level.getRssiLabel()));
        set.setColor(Color.BLUE);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setLineWidth(3.5f);
        set.setDrawCircles(true);
        set.setDrawValues(false);
        set.setDrawFilled(false);
        return set;
    }
}


