package me.theoria.wifimuscles.view.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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
import me.theoria.wifimuscles.model.WifiSignal;
import me.theoria.wifimuscles.utils.ChartConfigurator;
import me.theoria.wifimuscles.viewmodel.ChartViewModel;


public class RadarChartFragment extends Fragment {

    private RadarChart radarChart;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_radar_chart, container, false);
        radarChart = root.findViewById(R.id.radarChart);
        setupRadarChart();
        setupViewModel();
        return root;
    }

    private void setupRadarChart() {
        radarChart.getDescription().setEnabled(false);
        radarChart.setWebColor(Color.GRAY);
        radarChart.setWebLineWidth(1f);
        radarChart.setWebColorInner(Color.LTGRAY);
        radarChart.setWebLineWidthInner(1f);

        // X Axis (labels per entry)
        XAxis xAxis = radarChart.getXAxis();
        xAxis.setTextSize(14f);
        //xAxis.setValueFormatter((value, axis) -> "S" + ((int) value % 30)); // Label format: S0, S1, etc.

        // Y Axis (signal level)
        YAxis yAxis = radarChart.getYAxis();
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(5f);
        yAxis.setLabelCount(5, true);
        yAxis.setDrawLabels(false); // hide level numbers
    }

    private void setupViewModel() {
        ChartViewModel chartViewModel = new ViewModelProvider(requireActivity()).get(ChartViewModel.class);
        chartViewModel.getRssiLiveData().observe(getViewLifecycleOwner(), this::updateRadarChart);
    }

    private void updateRadarChart(List<WifiSignal> signals) {
        if (signals == null || signals.isEmpty()) return;

        List<RadarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        int count = Math.min(6, signals.size()); // Limit to 6 latest entries for readability
        int start = signals.size() - count;

        for (int i = start; i < signals.size(); i++) {
            WifiSignal signal = signals.get(i);
            int level = ChartConfigurator.mapRssiToLevel(signal.getRssi()); // Use mapped 1–5 level
            entries.add(new RadarEntry(level));
            labels.add("S" + i); // Label for axis
        }

        RadarDataSet dataSet = new RadarDataSet(entries, "WiFi Signal Level");
        dataSet.setColor(Color.BLUE);
        dataSet.setFillColor(Color.CYAN);
        dataSet.setDrawFilled(true);
        dataSet.setLineWidth(2f);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(12f);

        RadarData data = new RadarData(dataSet);
        radarChart.setData(data);

        // Update X Axis labels
        /*radarChart.getXAxis().setValueFormatter((value, axis) -> {
            int index = ((int) value) % labels.size();
            return labels.get(index);
        });*/

        radarChart.invalidate(); // Redraw chart
    }
}