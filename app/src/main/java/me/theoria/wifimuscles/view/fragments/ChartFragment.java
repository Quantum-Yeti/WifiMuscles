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
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import me.theoria.wifimuscles.databinding.FragmentChartBinding;
import me.theoria.wifimuscles.utils.ChartConfig;
import me.theoria.wifimuscles.utils.ChartUpdater;
import me.theoria.wifimuscles.viewmodel.ChartViewModel;

/**
 * {@code ChartFragment} is a {@link Fragment} that displays the primary dynamic wifi RSSI (Wi-Fi signal strength)
 * combo chart with the help of the MPAndroidChart library.
 * The {@link ChartViewModel} is used to observe the live RSSI data and update the chart dynamically.
 * The chart contains the primary RSSI data as well as threshold designations for the drawing of the lines.
 */
public class ChartFragment extends Fragment {

    private TextView rssiTextView;
    private ImageView rssiEmojiView;

    private LineChart chart;
    private LineDataSet primaryLineDataSet;
    private LineDataSet secondaryLineDataSet;
    private LineDataSet excellentSet, goodSet, fairSet, weakSet, unusableSet;
    private LineData lineData;

    //private boolean chartResizing = true;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the binding
        me.theoria.wifimuscles.databinding.FragmentChartBinding binding = FragmentChartBinding.inflate(inflater, container, false);

        // Retrieve the ViewModel
        ChartViewModel viewModel = new ViewModelProvider(this).get(ChartViewModel.class);

        // Initialize the primary chart and its UI elements
        initChartUI(binding);

        // Chart setup
        setupChart();

        // Observe the LiveData for RSSI reading
        observeRssiUpdates(viewModel);

        return binding.getRoot();
    }

    private void initChartUI (FragmentChartBinding binding){
        chart = binding.lineChart;
        rssiTextView = binding.rssiTextView;
        rssiEmojiView = binding.rssiEmoji;
    }

    private void setupChart() {
        ChartConfig.ChartSetupResult chartResults = ChartConfig.setupChartConfig(chart, requireContext(), "Unknown SSID");
        primaryLineDataSet = chartResults.primaryDataSet;
        excellentSet = chartResults.excellentSet;
        goodSet = chartResults.goodSet;
        fairSet = chartResults.fairSet;
        weakSet = chartResults.weakSet;
        unusableSet = chartResults.unusableSet;
        lineData = chartResults.lineData;

       /* // Test second line data set
        secondaryLineDataSet = new LineDataSet(getSecondLineData(), "Second Line");
        secondaryLineDataSet.setColor(getResources().getColor(R.color.purple));
        secondaryLineDataSet.setLineWidth(2f);

        lineData.addDataSet(secondaryLineDataSet);*/

        chart.setData(lineData);
        chart.invalidate();
    }

    /*// Test data
    private List<Entry> getSecondLineData() {
        List<Entry> secondLineEntries = new ArrayList<>();
        secondLineEntries.add(new Entry(0f, 5f));
        secondLineEntries.add(new Entry(2f, 3f));
        secondLineEntries.add(new Entry(0f, 5f));
        secondLineEntries.add(new Entry(2f, 3f));
        secondLineEntries.add(new Entry(0f, 5f));
        secondLineEntries.add(new Entry(2f, 3f));
        return secondLineEntries;
    }*/


    /**
     * Method: observeRssiUpdates
     * This method observes the LiveData from the ChartViewModel.
     * @param viewModel
     */
    private void observeRssiUpdates(ChartViewModel viewModel) {
        viewModel.getRssiLiveData().observe(getViewLifecycleOwner(), signals -> {
            ChartUpdater.updateChart(
                    rssiTextView,
                    rssiEmojiView,
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