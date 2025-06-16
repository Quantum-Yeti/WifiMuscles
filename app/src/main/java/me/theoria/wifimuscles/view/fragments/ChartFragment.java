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
import me.theoria.wifimuscles.utils.ChartConfig;
import me.theoria.wifimuscles.utils.ChartUpdater;
import me.theoria.wifimuscles.viewmodel.ChartViewModel;

/**
 * ChartFragment inflates the primary chart fragment and observes for LiveData updates.
 *
 */
public class ChartFragment extends Fragment {

    private TextView rssiTextView;
    private TextView frequencyTextView, bandwidthTextView;
    private ImageView rssiEmojiView;;
    private LineChart chart;
    private LineDataSet primaryLineDataSet;
    private LineDataSet secondaryLineDataSet;
    private LineDataSet excellentSet, goodSet, fairSet, weakSet, unusableSet;
    private LineData lineData;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the binding
        me.theoria.wifimuscles.databinding.FragmentChartBinding binding = FragmentChartBinding.inflate(inflater, container, false);

        // Retrieve the ViewModel
        ChartViewModel viewModel = new ViewModelProvider(this).get(ChartViewModel.class);

        // Initialize the primary chart and its UI elements
        initChartUI(binding);

        // Chart setup
        setupMainChart();

        // Observe the LiveData for RSSI reading
        observeLiveRSSI(viewModel);

        // Return top-level view of layout, required to display fragment UI
        return binding.getRoot();
    }

    /**
     * Method: initChartUI
     * Initialize chart UI components
     * @param binding
     */
    private void initChartUI (FragmentChartBinding binding){
        chart = binding.lineChart;
        rssiTextView = binding.rssiTextView;
        rssiEmojiView = binding.rssiEmoji;
        frequencyTextView = binding.frequencyBox;
        bandwidthTextView = binding.bandBox;
    }

    /**
     * Method: setupMainChart
     * Sets up chart with initialization of datasets
     */
    private void setupMainChart() {
        // Get chart config results
        ChartConfig.ChartSetupResult chartResults = ChartConfig.setupChartConfig(chart, requireContext());

        // Results to class variables
        primaryLineDataSet = chartResults.primaryDataSet;
        excellentSet = chartResults.excellentSet;
        goodSet = chartResults.goodSet;
        fairSet = chartResults.fairSet;
        weakSet = chartResults.weakSet;
        unusableSet = chartResults.unusableSet;
        lineData = chartResults.lineData;

        // Bind data to the chart
        chart.setData(lineData);
        chart.invalidate(); // Reflect changes
    }

    /**
     * Method: observeLiveRSSI
     * This method observes the LiveData from the ChartViewModel.
     * @param viewModel
     */
    private void observeLiveRSSI(ChartViewModel viewModel) {
        viewModel.getRssiLiveData().observe(getViewLifecycleOwner(), signals -> {
            // Update chart with new RSSI data
            ChartUpdater.updateChart(
                    rssiTextView,
                    rssiEmojiView,
                    frequencyTextView,
                    bandwidthTextView,
                    chart,
                    primaryLineDataSet,
                    excellentSet,
                    goodSet,
                    fairSet,
                    weakSet,
                    unusableSet,
                    lineData,
                    signals
            );
        });
    }
}