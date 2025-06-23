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

import me.theoria.wifimuscles.data.managers.LineChartManager;
import me.theoria.wifimuscles.data.model.WifiSignalModel;
import me.theoria.wifimuscles.databinding.FragmentChartBinding;
import me.theoria.wifimuscles.data.builders.LineChartBuilder;
import me.theoria.wifimuscles.data.managers.SignalProcessManager;
import me.theoria.wifimuscles.viewmodel.DataUIViewModel;
import me.theoria.wifimuscles.viewmodel.WifiViewModel;

/**
 * ChartFragment inflates the primary chart fragment and observes for LiveData updates.
 */
public class ChartFragment extends Fragment {

    private TextView frequencyTextView, bandwidthTextView, ipTextView, rssiTextView, ssidTextView, macTextView, rxTextView, maxLinkSpeedTextView;
    private ImageView rssiEmojiView;
    private LineChart chart;
    private LineDataSet primaryLineDataSet;
    private LineDataSet excellentSet, goodSet, fairSet, weakSet, unusableSet;
    private LineData lineData;

    private DataUIViewModel dataUIViewModel;
    private WifiViewModel wifiViewModel;

    /**
     * Method to inflate the fragment, observe live data within the UI, initialize
     * the ViewModels, and outputs the line chart.
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentChartBinding binding = FragmentChartBinding.inflate(inflater, container, false);

        // Initialize ViewModels
        wifiViewModel = new ViewModelProvider(this).get(WifiViewModel.class);
        dataUIViewModel = new ViewModelProvider(this).get(DataUIViewModel.class);

        // Initialize chart UI elements
        initLineChartUI(binding);

        // Setup chart
        setupMainChart();


        // Observe LiveData from both ViewModels
        observeSignalData();
        observeSignalUI();

        return binding.getRoot();
    }

    /**
     * Method that initializes the view bindings
     *
     * @param binding
     */
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

    /**
     * Method to setup the Main line chart displayed in the fragment.
     *
     */
    private void setupMainChart() {
        LineChartBuilder.ChartSetupResult chartResults = LineChartBuilder.setupChartConfig(chart, requireContext());

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

    /**
     * This method provides the data from the wifiViewModel as an observable which returns
     * the signal dataset to the line chart.
     *
     */
    private void observeSignalData() {
        wifiViewModel.getRssiLiveData().observe(getViewLifecycleOwner(), signals -> {
            if (signals == null) return;

            // Update chart only
            LineChartManager lineChartManager = new LineChartManager(
                    new SignalProcessManager(),
                    null // UI updates handled by the ViewModel
            );
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

            // Update UI state in the ViewModel
            dataUIViewModel.updateSignalUI(signals);
        });
    }

    /**
     * This private method observes real-time data being passed from the dataUIViewModel and
     * helps bind the various UI elements to their respective data source.
     *
     */
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
    }

    @Override
    public void onStop() {
        super.onStop();
        wifiViewModel.stopUpdates();
    }

    @Override
    public void onStart() {
        super.onStart();
        wifiViewModel.startUpdates();
    }
}
