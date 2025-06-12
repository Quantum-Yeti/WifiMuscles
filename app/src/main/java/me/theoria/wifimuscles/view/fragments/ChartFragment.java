package me.theoria.wifimuscles.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import me.theoria.wifimuscles.databinding.FragmentChartBinding;
import me.theoria.wifimuscles.utils.ChartConfigurator;
import me.theoria.wifimuscles.utils.ChartUpdater;
import me.theoria.wifimuscles.viewmodel.ChartViewModel;

/**
 * {@code ChartFragment} is a {@link Fragment} that displays the dynamic wifi RSSI (Wi-Fi signal strength)
 * combo chart with the help of the MPAndroidChart library.
 * The {@link ChartViewModel} is used to observe the live RSSI data and update the chart dynamically.
 * The chart contains the primary RSSI data as well as threshold designations for the drawing of the lines.
 */
public class ChartFragment extends Fragment {

    private LineChart chart;
    private LineDataSet lineDataSet;
    private LineDataSet excellentSet, goodSet, fairSet, weakSet, terribleSet;
    private LineData lineData;

    //private boolean chartResizing = true;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        me.theoria.wifimuscles.databinding.FragmentChartBinding binding = FragmentChartBinding.inflate(inflater, container, false);
        ChartViewModel viewModel = new ViewModelProvider(this).get(ChartViewModel.class);

        chart = binding.lineChart;


        ChartConfigurator.ChartSetupResult chartSetup = ChartConfigurator.setupLineChart(chart, requireContext());
        lineDataSet = chartSetup.primaryDataSet;
        excellentSet = chartSetup.excellentSet;
        goodSet = chartSetup.goodSet;
        fairSet = chartSetup.fairSet;
        weakSet = chartSetup.weakSet;
        terribleSet = chartSetup.terribleSet;
        lineData = chartSetup.lineData;

        // Observe LiveData for RSSI updates
        viewModel.getRssiLiveData().observe(getViewLifecycleOwner(), signals -> {
            ChartUpdater.updateChart(
                    chart,
                    lineDataSet,
                    excellentSet,
                    goodSet,
                    fairSet,
                    weakSet,
                    terribleSet,
                    lineData,
                    signals
            );
        });

        return binding.getRoot();
    }


}