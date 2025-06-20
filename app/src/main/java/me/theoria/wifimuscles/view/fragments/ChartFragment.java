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

import me.theoria.wifimuscles.databinding.FragmentChartBinding;
import me.theoria.wifimuscles.data.managers.ChartManager;
import me.theoria.wifimuscles.data.managers.ChartBuilderManager;
import me.theoria.wifimuscles.data.managers.SignalProcessManager;
import me.theoria.wifimuscles.viewmodel.DataUIViewModel;
import me.theoria.wifimuscles.viewmodel.WifiViewModel;

/**
 * ChartFragment inflates the primary chart fragment and observes for LiveData updates.
 */
public class ChartFragment extends Fragment {

    private TextView frequencyTextView, bandwidthTextView, ipTextView, rssiTextView, ssidTextView, macTextView, rxTextView;
    private ImageView rssiEmojiView;
    private LineChart chart;
    private LineDataSet primaryLineDataSet;
    private LineDataSet excellentSet, goodSet, fairSet, weakSet, unusableSet;
    private LineData lineData;

    private DataUIViewModel dataUIViewModel;
    private WifiViewModel wifiViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentChartBinding binding = FragmentChartBinding.inflate(inflater, container, false);

        // Initialize ViewModels
        wifiViewModel = new ViewModelProvider(this).get(WifiViewModel.class);
        dataUIViewModel = new ViewModelProvider(this).get(DataUIViewModel.class);

        // Initialize chart UI elements
        initChartUI(binding);

        // Setup chart
        setupMainChart();

        // Observe LiveData from both ViewModels
        observeSignalData();
        observeSignalUI();

        return binding.getRoot();
    }

    private void initChartUI(FragmentChartBinding binding) {
        chart = binding.lineChart;
        rssiTextView = binding.rssiTextView;
        //rssiEmojiView = binding.rssiEmoji;
        frequencyTextView = binding.frequencyBox;
        bandwidthTextView = binding.bandBox;
        ipTextView = binding.ipBox;
        ssidTextView = binding.ssidBox;
        macTextView = binding.macBox;
        rxTextView = binding.rxSpeedBox;
    }

    private void setupMainChart() {
        ChartBuilderManager.ChartSetupResult chartResults = ChartBuilderManager.setupChartConfig(chart, requireContext());

        primaryLineDataSet = chartResults.primaryDataSet;
        excellentSet = chartResults.excellentSet;
        goodSet = chartResults.goodSet;
        fairSet = chartResults.fairSet;
        weakSet = chartResults.weakSet;
        unusableSet = chartResults.unusableSet;
        lineData = chartResults.lineData;

        chart.setData(lineData);
        chart.invalidate();
    }

    private void observeSignalData() {
        wifiViewModel.getRssiLiveData().observe(getViewLifecycleOwner(), signals -> {
            if (signals == null) return;

            // Update chart only
            ChartManager chartManager = new ChartManager(
                    new SignalProcessManager(),
                    null // UI updates handled by the ViewModel
            );
            chartManager.updateChart(
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

            // Update UI state in the ViewModel
            dataUIViewModel.updateSignalUI(signals);
        });
    }

    private void observeSignalUI() {
        dataUIViewModel.getRssiText().observe(getViewLifecycleOwner(), text -> rssiTextView.setText(text));
        //dataUIViewModel.getRssiEmoji().observe(getViewLifecycleOwner(), resId -> rssiEmojiView.setImageResource(resId));
        dataUIViewModel.getIpText().observe(getViewLifecycleOwner(), ip -> ipTextView.setText(ip));
        dataUIViewModel.getFrequencyText().observe(getViewLifecycleOwner(), freq -> frequencyTextView.setText(freq));
        dataUIViewModel.getBandwidthText().observe(getViewLifecycleOwner(), bw -> bandwidthTextView.setText(bw));
        dataUIViewModel.getSSIDText().observe(getViewLifecycleOwner(), ssid -> ssidTextView.setText(ssid));
        dataUIViewModel.getMac().observe(getViewLifecycleOwner(), mac -> macTextView.setText(mac));
        dataUIViewModel.getLinkSpeed().observe(getViewLifecycleOwner(), rx -> rxTextView.setText(rx));
    }
}
