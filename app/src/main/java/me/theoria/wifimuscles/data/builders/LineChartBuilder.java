package me.theoria.wifimuscles.data.builders;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

import me.theoria.wifimuscles.R;
import me.theoria.wifimuscles.data.model.ChartMarkerModel;
import me.theoria.wifimuscles.data.model.RSSILevelModel;

public class LineChartBuilder {

    public static class ChartSetupResult {
        public LineChart chart;
        public LineDataSet primaryDataSet, excellentSet, goodSet, fairSet, weakSet, unusableSet, linkSpeedDataSet;
        public LineData lineData;
    }

    public static ChartSetupResult setupChartConfig(LineChart chart, Context context) {

        configureChart(chart);
        configureXAxis(chart.getXAxis());
        configureYAxis(chart.getAxisLeft());

        LineDataSet rssiValueDataSet = createPrimaryDataSet(context);
        setMarkerView(chart, context);

        LineDataSet excellentSet = createLineThresholdDataSet(context, RSSILevelModel.EXCELLENT);
        LineDataSet goodSet = createLineThresholdDataSet(context, RSSILevelModel.GOOD);
        LineDataSet fairSet = createLineThresholdDataSet(context, RSSILevelModel.FAIR);
        LineDataSet weakSet = createLineThresholdDataSet(context, RSSILevelModel.WEAK);
        LineDataSet unusableSet = createLineThresholdDataSet(context, RSSILevelModel.UNUSABLE);

        // Create the linkSpeedDataSet
        LineDataSet linkSpeedDataSet = createLinkSpeedDataSet(context);

        LineData lineData = new LineData();
        lineData.addDataSet(rssiValueDataSet);
        lineData.addDataSet(linkSpeedDataSet);

        // Currently empty datasets
        /*lineData.addDataSet(excellentSet);
        lineData.addDataSet(goodSet);
        lineData.addDataSet(fairSet);
        lineData.addDataSet(weakSet);
        lineData.addDataSet(unusableSet);*/

        chart.setData(lineData);
        chart.animateXY(1000, 1000, Easing.EaseInQuart);
        chart.invalidate();

        ChartSetupResult result = new ChartSetupResult();
        result.chart = chart;
        result.primaryDataSet = rssiValueDataSet;
        result.excellentSet = excellentSet;
        result.goodSet = goodSet;
        result.fairSet = fairSet;
        result.weakSet = weakSet;
        result.unusableSet = unusableSet;
        result.linkSpeedDataSet = linkSpeedDataSet;
        result.lineData = lineData;

        return result;
    }

    private static void configureChart(LineChart chart) {
        chart.setDrawGridBackground(false);
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setExtraBottomOffset(10f);
        chart.setExtraTopOffset(10f);
        chart.setExtraLeftOffset(5f);
        chart.getLegend().setEnabled(false);

        // Remove the description label
        Description description = new Description();
        description.setEnabled(false); // Disable the description
        chart.setDescription(description);
    }

    private static void configureXAxis(XAxis xAxis) {
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(5f);
        xAxis.setGranularityEnabled(true);
        xAxis.setLabelCount(8, false);
        xAxis.setTextColor(R.color.bg_primary);
        xAxis.setGridColor(Color.TRANSPARENT);
    }

    private static void configureYAxis(YAxis yAxis) {
        yAxis.setDrawGridLines(false);
        yAxis.setDrawAxisLine(false);
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yAxis.setGridColor(Color.TRANSPARENT);
        yAxis.setYOffset(0f);
        yAxis.setXOffset(8f);
        yAxis.setAxisMinimum(-100f); // dBm
        yAxis.setAxisMaximum(0f);
        yAxis.setGranularity(10f);
        yAxis.setLabelCount(4, true);
        yAxis.setTextSize(12f);
        yAxis.setDrawLabels(true);
        yAxis.setTextColor(Color.WHITE);
        yAxis.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
    }


    private static LineDataSet createPrimaryDataSet(Context context) {
        LineDataSet dataSet = new LineDataSet(new ArrayList<>(), context.getString(R.string.label_blank));
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setCubicIntensity(0.3f);
        dataSet.setColor(context.getColor(R.color.accent_light_blue));
        dataSet.setLineWidth(4f);
        dataSet.setDrawFilled(false);
        dataSet.setDrawCircleHole(true);
        dataSet.getColor(R.color.accent_pink);
        return dataSet;
    }

    public static LineDataSet createLinkSpeedDataSet(Context context) {
        List<Entry> linkSpeedEntries = new ArrayList<>();
        LineDataSet linkSpeedDataSet = new LineDataSet(linkSpeedEntries, context.getString(R.string.link_speed));
        linkSpeedDataSet.setColor(Color.RED);
        linkSpeedDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        linkSpeedDataSet.setLineWidth(2f);
        linkSpeedDataSet.setDrawCircles(false);
        linkSpeedDataSet.setDrawValues(false);
        linkSpeedDataSet.setAxisDependency(YAxis.AxisDependency.RIGHT); // Important!
        return linkSpeedDataSet;
    }

    private static void setMarkerView(LineChart chart, Context context) {
        ChartMarkerModel marker = new ChartMarkerModel(context, R.layout.marker_view);
        marker.setChartView(chart);
        chart.setMarker(marker);
    }

    private static LineDataSet createLineThresholdDataSet(Context context, RSSILevelModel level) {
        LineDataSet set = new LineDataSet(new ArrayList<>(), context.getString(level.getRssiLabel()));
        set.setColor(R.color.accent_pink);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setLineWidth(3.5f);
        set.setDrawCircles(true);
        set.setDrawValues(false);
        set.setDrawFilled(false);
        return set;
    }
}
