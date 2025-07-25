package me.theoria.wifimuscles.data.builders;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;

import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;

import me.theoria.wifimuscles.R;
import me.theoria.wifimuscles.data.model.ChartMarkerModel;

/**
 * A utility class that sets up a BarChart with default styling and returns a pre-configured BarDataSet.
 * Separates configuration logic from the BarChartFragment and BarChartManager.
 */
public class BarChartBuilder {

    /**
     * Configures the given BarChart instance with styling, axis configuration, and data binding.
     * @param chart
     * @param context
     * @return
     */
    public static BarDataSet setupBarChart(BarChart chart, Context context) {
        // Overall chart settings.
        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(false);
        chart.setMaxVisibleValueCount(6);
        chart.setPinchZoom(true);
        chart.setDrawGridBackground(false);
        chart.getLegend().setEnabled(false);
        chart.getDescription().setEnabled(false);

        // Configure X-Axis
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawLabels(false);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);

        // Configure Y-Axis
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setTextSize(14f);
        leftAxis.setAxisMinimum(-100f);
        leftAxis.setAxisMaximum(0f);
        leftAxis.setLabelCount(5, true);
        leftAxis.setGranularity(20f);
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawAxisLine(false);

        // Value formatter turning raw rssi to positive percentages
        leftAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int percent = (int) ((value + 100));
                return percent + "%";
            }
        });


        // Disables Y-Axis showing on the right side of chart.
        chart.getAxisRight().setEnabled(false);

        // Creates empty dataset to pass values
        BarDataSet dataSet = new BarDataSet(new ArrayList<>(), null);
        dataSet.setColor(ContextCompat.getColor(context, R.color.accent_light_blue));
        dataSet.setValueTextColor(ContextCompat.getColor(context, R.color.blackText));
        dataSet.setValueTextSize(12f);
        dataSet.setValueTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        dataSet.setDrawValues(true);

        // Bind the dataset to the BarData and set to the chart.
        BarData data = new BarData(dataSet);
        data.setBarWidth(0.8f);
        chart.setData(data);
        chart.invalidate();

        setMarkerView(chart, context);

        return dataSet;
    }

    private static void setMarkerView(BarChart chart, Context context) {
        ChartMarkerModel marker = new ChartMarkerModel(context, R.layout.marker_view);
        marker.setChartView(chart);
        chart.setMarker(marker);
    }

}
