package me.theoria.wifimuscles.view.fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

import me.theoria.wifimuscles.R;
import me.theoria.wifimuscles.data.model.WifiSignalModel;
import me.theoria.wifimuscles.viewmodel.DataUIViewModel;
import me.theoria.wifimuscles.viewmodel.WifiViewModel;

public class BarChartFragment extends Fragment {

    private TextView rssiTextView, ipTextView, frequencyTextView, bandwidthTextView, ssidTextView, macTextView;
    private BarChart barChart;

    private WifiViewModel wifiViewModel;
    private DataUIViewModel dataUIViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bar_chart, container, false);

        // Bind UI references
        barChart = root.findViewById(R.id.barChart);
        rssiTextView = root.findViewById(R.id.rssiTextView);
        ipTextView = root.findViewById(R.id.ipBox);
        frequencyTextView = root.findViewById(R.id.frequencyBox);
        bandwidthTextView = root.findViewById(R.id.bandBox);
        ssidTextView = root.findViewById(R.id.ssidBox);
        macTextView = root.findViewById(R.id.macBox);

        setupBarChart();

        wifiViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(WifiViewModel.class);

        dataUIViewModel = new ViewModelProvider(this).get(DataUIViewModel.class);

        // Observe raw RSSI list for bar chart
        wifiViewModel.getRssiLiveData().observe(getViewLifecycleOwner(), this::updateBarChart);

        // Observe processed UI LiveData
        dataUIViewModel.getRssiText().observe(getViewLifecycleOwner(), rssiTextView::setText);
        dataUIViewModel.getIpText().observe(getViewLifecycleOwner(), ipTextView::setText);
        dataUIViewModel.getFrequencyText().observe(getViewLifecycleOwner(), frequencyTextView::setText);
        dataUIViewModel.getBandwidthText().observe(getViewLifecycleOwner(), bandwidthTextView::setText);
        dataUIViewModel.getSSIDText().observe(getViewLifecycleOwner(), ssidTextView::setText);
        dataUIViewModel.getMac().observe(getViewLifecycleOwner(), macTextView::setText);

        return root;
    }

    private void setupBarChart() {
        // same chart setup from your latest snippet...
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(false);
        barChart.setMaxVisibleValueCount(6);
        barChart.setPinchZoom(true);
        barChart.setDrawGridBackground(false);
        barChart.getLegend().setEnabled(false);
        barChart.getDescription().setEnabled(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawLabels(false);
        xAxis.setDrawGridLines(false);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setTextSize(14f);
        leftAxis.setAxisMinimum(-100f);
        leftAxis.setAxisMaximum(0f);
        leftAxis.setGranularity(20f);
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawAxisLine(false);

        barChart.getAxisRight().setEnabled(false);
    }

    private void updateBarChart(List<WifiSignalModel> signals) {
        if (signals == null || signals.isEmpty()) return;

        int count = Math.min(6, signals.size());
        int start = signals.size() - count;
        List<BarEntry> entries = new ArrayList<>();

        for (int i = start; i < signals.size(); i++) {
            WifiSignalModel sig = signals.get(i);
            entries.add(new BarEntry(i - start, sig.getRssi()));
        }

        WifiSignalModel latest = signals.get(signals.size() - 1);
        rssiTextView.setText("RSSI: " + latest.getRssi() + " dBm");

        BarDataSet dataSet = new BarDataSet(entries, null);
        dataSet.setColor(ContextCompat.getColor(requireContext(), R.color.accent_pink));
        dataSet.setValueTextColor(ContextCompat.getColor(requireContext(), R.color.blackText));
        dataSet.setValueTextSize(12f);
        dataSet.setValueTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        dataSet.setDrawValues(true);

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.8f);

        barChart.setData(barData);
        barChart.invalidate();

        // Push same signals into DataUIViewModel so its observers update
        dataUIViewModel.updateSignalUI(signals);
    }
}
