package me.theoria.wifimuscles.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import me.theoria.wifimuscles.data.builders.LineChartBuilder;
import me.theoria.wifimuscles.data.managers.LineChartManager;
import me.theoria.wifimuscles.data.managers.SignalProcessManager;
import me.theoria.wifimuscles.databinding.FragmentChartBinding;
import me.theoria.wifimuscles.utils.ToastUtils;
import me.theoria.wifimuscles.viewmodel.DataUIViewModel;
import me.theoria.wifimuscles.viewmodel.WifiViewModel;

/**
 * ChartFragment inflates the primary chart fragment and observes for LiveData updates.
 */
public class ChartFragment extends Fragment {

    // UI elements
    private TextView frequencyTextView, bandwidthTextView, ipTextView, rssiTextView, ssidTextView, macTextView, rxTextView, maxLinkSpeedTextView;
    private ImageView rssiEmojiView;
    private LineChart chart;

    // Chart datasets
    private LineDataSet primaryLineDataSet;
    private LineDataSet excellentSet, goodSet, fairSet, weakSet, unusableSet;
    private LineData lineData;

    // ViewModels
    private DataUIViewModel dataUIViewModel;
    private WifiViewModel wifiViewModel;

    // Managers
    private LineChartManager lineChartManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentChartBinding binding = FragmentChartBinding.inflate(inflater, container, false);

        // ViewModels
        wifiViewModel = new ViewModelProvider(this).get(WifiViewModel.class);
        dataUIViewModel = new ViewModelProvider(this).get(DataUIViewModel.class);

        // Init chart UI
        initLineChartUI(binding);

        // Setup chart
        setupMainChart();

        // Observe data
        observeSignalData();
        observeSignalUI();

        // Start data updates
        wifiViewModel.startUpdates();

        return binding.getRoot();
    }

    private void initLineChartUI(FragmentChartBinding binding) {
        chart = binding.lineChart;
        rssiTextView = binding.rssiTextView;
        rssiEmojiView = binding.rssiEmoji;
        frequencyTextView = binding.frequencyBox;
        bandwidthTextView = binding.bandBox;
        ipTextView = binding.ipBox;
        ssidTextView = binding.ssidBox;
        macTextView = binding.macBox;
        rxTextView = binding.rxSpeedBox;
        maxLinkSpeedTextView = binding.maxSpeedBox;
    }

    private void setupMainChart() {
        // Configure chart
        LineChartBuilder.ChartSetupResult chartResults = LineChartBuilder.setupChartConfig(chart, requireContext());

        // Assign datasets
        primaryLineDataSet = chartResults.primaryDataSet;
        excellentSet = chartResults.excellentSet;
        goodSet = chartResults.goodSet;
        fairSet = chartResults.fairSet;
        weakSet = chartResults.weakSet;
        unusableSet = chartResults.unusableSet;
        //linkSpeedDataSet = chartResults.linkSpeedDataSet;
        lineData = chartResults.lineData;

        chart.setData(lineData);

        // Init manager
        lineChartManager = new LineChartManager(new SignalProcessManager(), dataUIViewModel);

        // Observe link speed
        //lineChartManager.observeLinkSpeed(chart);

        chart.invalidate();
    }

    private void observeSignalData() {
        wifiViewModel.getRssiLiveData().observe(getViewLifecycleOwner(), signals -> {
            if (signals == null) return;

            lineChartManager.updateLineChart(
                    signals,
                    chart,
                    primaryLineDataSet,
                    excellentSet,
                    goodSet,
                    fairSet,
                    weakSet,
                    unusableSet,
                    lineData
            );

            // UI signal info updates
            dataUIViewModel.updateSignalUI(signals);
        });
    }

    private void observeSignalUI() {
        dataUIViewModel.getRssiText().observe(getViewLifecycleOwner(), text -> rssiTextView.setText(text));
        dataUIViewModel.getRssiEmoji().observe(getViewLifecycleOwner(), resId -> rssiEmojiView.setImageResource(resId));
        dataUIViewModel.getIpText().observe(getViewLifecycleOwner(), ip -> ipTextView.setText(ip));
        dataUIViewModel.getFrequencyText().observe(getViewLifecycleOwner(), freq -> frequencyTextView.setText(freq));
        dataUIViewModel.getBandwidthText().observe(getViewLifecycleOwner(), bw -> bandwidthTextView.setText(bw));
        dataUIViewModel.getSSIDText().observe(getViewLifecycleOwner(), ssid -> ssidTextView.setText(ssid));
        dataUIViewModel.getMac().observe(getViewLifecycleOwner(), mac -> macTextView.setText(mac));
        dataUIViewModel.getLinkSpeed().observe(getViewLifecycleOwner(), rx -> rxTextView.setText(rx));
        dataUIViewModel.getMaxLinkSpeed().observe(getViewLifecycleOwner(), max -> maxLinkSpeedTextView.setText(max));

        dataUIViewModel.getToastLevelEvent().observe(getViewLifecycleOwner(), level -> {
            if (level == 3 || level == 2 || level == 1) {
                ToastUtils.showToastForExtender(requireContext(), level);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        wifiViewModel.startUpdates();
    }

    @Override
    public void onStop() {
        super.onStop();
        wifiViewModel.stopUpdates();
    }
}
