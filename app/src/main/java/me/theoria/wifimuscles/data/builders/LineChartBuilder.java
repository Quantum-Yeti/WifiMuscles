package me.theoria.wifimuscles.data.builders;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;

import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;

import me.theoria.wifimuscles.R;
import me.theoria.wifimuscles.data.model.ChartMarkerModel;
import me.theoria.wifimuscles.data.model.RSSIQualityModel;

public class LineChartBuilder {

    public static class ChartSetupResult {
        public LineChart chart;
        public LineDataSet primaryDataSet, excellentSet, goodSet, fairSet, weakSet, unusableSet;
        public LineData lineData;
    }

    public static ChartSetupResult setupChartConfig(LineChart chart, Context context) {
        configureChart(chart);
        configureXAxis(chart.getXAxis());
        configureLeftYAxis(chart.getAxisLeft());

        LineDataSet rssiValueDataSet = createPrimaryDataSet(context);

        // Threshold lines tied to RSSI
        LineDataSet excellentSet = createLineThresholdDataSet(context, RSSIQualityModel.EXCELLENT);
        LineDataSet goodSet = createLineThresholdDataSet(context, RSSIQualityModel.GOOD);
        LineDataSet fairSet = createLineThresholdDataSet(context, RSSIQualityModel.FAIR);
        LineDataSet weakSet = createLineThresholdDataSet(context, RSSIQualityModel.WEAK);
        LineDataSet unusableSet = createLineThresholdDataSet(context, RSSIQualityModel.UNUSABLE);

        setMarkerView(chart, context);

        // Add all datasets to chart
        LineData lineData = new LineData();
        lineData.addDataSet(rssiValueDataSet);

        // Threshold quality set - might use this later
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
        result.lineData = lineData;

        return result;
    }

    private static void configureChart(LineChart chart) {
        chart.setDrawGridBackground(false);
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setExtraOffsets(5f, 10f, 5f, 10f);
        chart.getLegend().setEnabled(false);
        chart.getAxisRight().setEnabled(false);
        Description description = new Description();
        description.setEnabled(false);
        chart.setDescription(description);
    }

    private static void configureXAxis(XAxis xAxis) {
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(5f);
        xAxis.setGranularityEnabled(true);
        xAxis.setLabelCount(5, false);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setGridColor(Color.TRANSPARENT);
    }

    private static void configureLeftYAxis(YAxis yAxis) {
        yAxis.setDrawGridLines(false);
        yAxis.setDrawAxisLine(false);
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yAxis.setAxisMinimum(-127f); // For RSSI
        yAxis.setAxisMaximum(0f);
        yAxis.setGranularity(10f);
        yAxis.setLabelCount(5, true);
        yAxis.setTextSize(12f);
        yAxis.setTextColor(Color.WHITE);
        yAxis.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        // Value formatter turning raw rssi to positive percentages
        yAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int percent = (int) ((value + 100));
                if (value <= -101 && value >= -127) {
                    return 0 + "%";
                }
                return percent + "%";

            }
        });
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
        return dataSet;
    }

    private static void setMarkerView(LineChart chart, Context context) {
        ChartMarkerModel marker = new ChartMarkerModel(context, R.layout.marker_view);
        marker.setChartView(chart);
        chart.setMarker(marker);
    }

    private static LineDataSet createLineThresholdDataSet(Context context, RSSIQualityModel level) {
        LineDataSet set = new LineDataSet(new ArrayList<>(), context.getString(level.getRssiLabel()));
        set.setColor(ContextCompat.getColor(context, R.color.accent_pink));
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setLineWidth(3.5f);
        set.setDrawCircles(false);
        set.setDrawValues(false);
        set.setDrawFilled(false);
        return set;
    }

}
