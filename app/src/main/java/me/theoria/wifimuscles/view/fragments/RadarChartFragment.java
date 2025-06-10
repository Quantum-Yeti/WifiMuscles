package me.theoria.wifimuscles.view.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;

import java.util.ArrayList;
import java.util.List;

import me.theoria.wifimuscles.R;


public class RadarChartFragment extends Fragment {

    private RadarChart radarChart;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_second_chart, container, false);
        radarChart = root.findViewById(R.id.radarChart);
        setupRadarChart();
        return root;
    }

    private void setupRadarChart() {
        List<RadarEntry> entries = new ArrayList<>();
        entries.add(new RadarEntry(4));
        entries.add(new RadarEntry(2));
        entries.add(new RadarEntry(6));
        entries.add(new RadarEntry(3));
        entries.add(new RadarEntry(5));

        RadarDataSet dataSet = new RadarDataSet(entries, "Signal Analysis");
        dataSet.setColor(Color.BLUE);
        dataSet.setFillColor(Color.CYAN);
        dataSet.setDrawFilled(true);
        dataSet.setLineWidth(2f);

        RadarData data = new RadarData(dataSet);
        data.setDrawValues(true);
        data.setValueTextColor(Color.BLACK);
        data.setValueTextSize(12f);

        radarChart.setData(data);
        radarChart.getDescription().setEnabled(false);
        radarChart.setWebColor(Color.GRAY);
        radarChart.setWebLineWidth(1f);
        radarChart.setWebColorInner(Color.LTGRAY);
        radarChart.setWebLineWidthInner(1f);

        XAxis xAxis = radarChart.getXAxis();
        xAxis.setTextSize(14f);


        YAxis yAxis = radarChart.getYAxis();
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(6f);
        yAxis.setLabelCount(6, true);
        yAxis.setDrawLabels(false);

        radarChart.invalidate(); // refresh
    }


}