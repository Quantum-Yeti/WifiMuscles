package me.theoria.wifimuscles.view.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

import me.theoria.wifimuscles.R;
import me.theoria.wifimuscles.model.WifiSignalModel;
import me.theoria.wifimuscles.utils.RSSIUtils;
import me.theoria.wifimuscles.viewmodel.ChartViewModel;


public class RadarChartFragment extends Fragment {

    // Initialize UI component objects
    private TextView rssiTextView;
    private ImageView rssiEmojiView;
    private RadarChart radarChart;

    private ChartViewModel chartViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_radar_chart, container, false);

        // Bind UI components for the RadarChart fragment
        radarChart = root.findViewById(R.id.radarChart);
        rssiTextView = root.findViewById(R.id.rssiTextView);
        rssiEmojiView = root.findViewById(R.id.rssiEmoji);


        // Initialize the RadarChart
        setupRadarChart();

        // Initialize ChartViewModel (RSSI)
        ChartViewModel chartViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())).get(ChartViewModel.class);

        // Observe current SSID
        chartViewModel.getRssiLiveData().observe(getViewLifecycleOwner(), this::updateRadarChartDescription);
        chartViewModel.getRssiLiveData().observe(getViewLifecycleOwner(), this::updateRadarChart);

        return root;
    }

    private void updateRadarChartDescription(List<WifiSignalModel> wifiSignalModels) {
        Description description = new Description();
        description.setText("Connected to: ");
        radarChart.setDescription(description);
    }

    private void setupRadarChart() {
        // RadarChart Configuration
        radarChart.getDescription().setEnabled(false);
        radarChart.setWebColor(Color.BLACK);
        radarChart.setWebLineWidth(1f);
        radarChart.setWebColorInner(Color.LTGRAY);
        radarChart.setWebLineWidthInner(1f);

        // X Axis
        XAxis xAxis = radarChart.getXAxis();
        xAxis.setTextSize(14f);
        xAxis.setDrawLabels(true);
        xAxis.setValueFormatter(new ValueFormatter() {
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

        // Y Axis (signal level 1-5)
        YAxis yAxis = radarChart.getYAxis();
        yAxis.setAxisMinimum(-100f);
        yAxis.setAxisMaximum(0f);
        yAxis.setLabelCount(8, true);
        yAxis.setDrawLabels(false); // hide level numbers
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

    private void updateRadarChart(List<WifiSignalModel> signals) {
        if (signals == null || signals.isEmpty()) return;

        int count = Math.min(6, signals.size());
        int start = signals.size() - count;

        List<RadarEntry> entries = new ArrayList<>();
        List<String> radarLabels = new ArrayList<>();

        for (int i = start; i < signals.size(); i++) {
            WifiSignalModel signal = signals.get(i);

            int signalLevel = signal.getSignalLevel();
            entries.add(new RadarEntry(signalLevel));
            radarLabels.add("S" + i);
        }

        // Display RSSI integer value and emoji in TextView and ImageView
        WifiSignalModel latestSignal = signals.get(signals.size() - 1);
        int latestRssi = latestSignal.getRssi();
        rssiTextView.setText("RSSI: " + latestRssi + " dBm");
        rssiEmojiView.setImageResource(RSSIUtils.getRssiEmoji(latestRssi));

        //Show extender toast message
        //ToastUtil.showToastForLevel(requireContext(), latestSignal.getSignalLevel());

        // General UI configuration for the RadarChart datasets
        RadarDataSet dataSet = new RadarDataSet(entries, "WiFi Strength");
        dataSet.setColor(Color.BLUE);
        dataSet.setFillColor(Color.CYAN);
        dataSet.setDrawFilled(true);
        dataSet.setLineWidth(3f);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(14f);

        RadarData data = new RadarData(dataSet);
        radarChart.setData(data);
        radarChart.invalidate(); // Redraw chart
    }



}