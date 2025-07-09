package me.theoria.wifimuscles.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import me.theoria.wifimuscles.R;
import me.theoria.wifimuscles.data.builders.LineChartBuilder;
import me.theoria.wifimuscles.data.managers.charts.LineChartManager;
import me.theoria.wifimuscles.data.managers.info.ChartPopupManager;
import me.theoria.wifimuscles.data.managers.info.StatsPopupManager;
import me.theoria.wifimuscles.data.managers.SignalProcessManager;
import me.theoria.wifimuscles.data.model.InfoCardItem;
import me.theoria.wifimuscles.databinding.FragmentChartBinding;
import me.theoria.wifimuscles.utils.ChartInfoUtils;
import me.theoria.wifimuscles.view.adapters.ChartInfoCardAdapter;
import me.theoria.wifimuscles.viewmodel.DataUIViewModel;
import me.theoria.wifimuscles.viewmodel.WifiViewModel;

/**
 * ChartFragment inflates the primary chart fragment and observes for LiveData updates.
 */
public class ChartFragment extends Fragment {

    // UI elements
    private TextView frequencyTextView, bandwidthTextView, ipTextView, rssiTextView, ssidTextView, macTextView, rxTextView, maxLinkSpeedTextView, standardTextView, progressBarText;
    private ImageView rssiEmojiView;
    private LineChart chart;

    private ChartInfoCardAdapter adapter;

    // Chart datasets
    private LineDataSet primaryLineDataSet;
    private LineDataSet excellentSet, goodSet, fairSet, weakSet, unusableSet;
    private LineData lineData;
    private LineDataSet linkSpeedDataSet;

    // ViewModels
    private DataUIViewModel dataUIViewModel;
    private WifiViewModel wifiViewModel;

    // Managers
    private LineChartManager lineChartManager;

    // Switch Chart
    private MaterialButton switchChartButton;

    // Progress Bar
    private ProgressBar progressBar;

    // Popup Info
    private ChartPopupManager chartPopupManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentChartBinding binding = FragmentChartBinding.inflate(inflater, container, false);

        // Progress Bar
        progressBar = binding.getRoot().findViewById(R.id.progressBar);
        progressBarText = binding.getRoot().findViewById(R.id.progressBarText);
        showProgressBar(true);

        // Recycler View
        RecyclerView infoRecyclerView = binding.getRoot().findViewById(R.id.chartInfoRecyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        infoRecyclerView.setLayoutManager(gridLayoutManager);
        adapter = new ChartInfoCardAdapter(new ArrayList<>());
        infoRecyclerView.setAdapter(adapter);

        // Basic animation for Recycler View
        infoRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // Popup Windows
        chartPopupManager = new ChartPopupManager(requireContext());
        // Set item click listener to show popup with description
        adapter.setOnItemClickListener((item, position, view) -> {
            String description = ChartInfoUtils.getDescriptionForKey(requireContext(), item.getTitle());
            chartPopupManager.showChartPopup(view, item.getTitle(), description);
        });

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

        switchChartButton = binding.switchChartButton;
        switchChartButton.setOnClickListener(v ->{
            openBarChartFragment();
        });

        return binding.getRoot();
    }

    private void initLineChartUI(FragmentChartBinding binding) {
        chart = binding.lineChart;
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
        lineData = chartResults.lineData;

        chart.setData(lineData);

        // Initalize chart manager
        lineChartManager = new LineChartManager(new SignalProcessManager(), dataUIViewModel);

        // Observe link speed
        //lineChartManager.observeLinkSpeed(chart);

        chart.invalidate();
    }

    private void observeSignalData() {
        showProgress(true);

        wifiViewModel.getRssiLiveData().observe(getViewLifecycleOwner(), signals -> {
            if (signals == null) {
                showProgress(false);
                return;
            }

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

            // Chart & ProgressBar Visibility
            chart.setVisibility(View.VISIBLE); // Chart becomes visible
            showProgress(false); // Hides progressBar once chart is ready
        });
    }

    private void showProgressBar(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);  // Show the progress bar
        } else {
            progressBar.setVisibility(View.GONE);     // Hide the progress bar
        }
    }

    private void observeSignalUI() {
        // Observe LiveData for each signal info
        dataUIViewModel.getRssiText().observe(getViewLifecycleOwner(), text -> updateInfoCards());
        dataUIViewModel.getRssiEmoji().observe(getViewLifecycleOwner(), resId -> updateInfoCards());
        dataUIViewModel.getIpText().observe(getViewLifecycleOwner(), ip -> updateInfoCards());
        dataUIViewModel.getFrequencyText().observe(getViewLifecycleOwner(), freq -> updateInfoCards());
        dataUIViewModel.getBandwidthText().observe(getViewLifecycleOwner(), bw -> updateInfoCards());
        dataUIViewModel.getSSIDText().observe(getViewLifecycleOwner(), ssid -> updateInfoCards());
        dataUIViewModel.getBssidText().observe(getViewLifecycleOwner(), mac -> updateInfoCards());
        dataUIViewModel.getLinkSpeed().observe(getViewLifecycleOwner(), rx -> updateInfoCards());
        dataUIViewModel.getMaxLinkSpeed().observe(getViewLifecycleOwner(), max -> updateInfoCards());
        dataUIViewModel.getToastLevelEvent().observe(getViewLifecycleOwner(), level -> {
            if (level == 3 || level == 2 || level == 1) {
                // Show toast or Snackbar for specific levels
            }
        });

        wifiViewModel.getWifiStandardLiveData().observe(getViewLifecycleOwner(), standard -> updateInfoCards());
    }

    private void updateInfoCards() {
        List<InfoCardItem> items = new ArrayList<>();
        items.add(new InfoCardItem("SSID", dataUIViewModel.getSSIDText().getValue(), R.drawable.icon_ssid));


        items.add(new InfoCardItem(
                "RSSI",
                dataUIViewModel.getRssiText().getValue(),
                dataUIViewModel.getRssiEmoji().getValue() != null ? dataUIViewModel.getRssiEmoji().getValue() : R.drawable.emoji_bad
        ));

        //items.add(new InfoCardItem("RSSI", dataUIViewModel.getRssiText().getValue(), R.drawable.emoji_red));
        items.add(new InfoCardItem(getString(R.string.frequency_card_short), dataUIViewModel.getFrequencyText().getValue(), R.drawable.icon_function));
        items.add(new InfoCardItem(getString(R.string.frequency_band_card), dataUIViewModel.getBandwidthText().getValue(), R.drawable.icon_function));
        items.add(new InfoCardItem(getString(R.string.ap_ip), dataUIViewModel.getIpText().getValue(), R.drawable.icon_dns));
        items.add(new InfoCardItem(getString(R.string.ap_mac), dataUIViewModel.getBssidText().getValue(), R.drawable.icon_dns));
        items.add(new InfoCardItem(getString(R.string.link_speed_card), dataUIViewModel.getLinkSpeed().getValue(), R.drawable.icon_rocket));
        items.add(new InfoCardItem(getString(R.string.max_speed), dataUIViewModel.getMaxLinkSpeed().getValue(), R.drawable.icon_rocket));

        // Update RecyclerView adapter with new data
        if (items.size() > 0) {
            adapter.updateItems(items);
        }
    }

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        progressBarText.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void openBarChartFragment() {
        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new BarChartFragment())
                .addToBackStack(null)
                .commit();
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
