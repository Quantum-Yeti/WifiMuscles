package me.theoria.wifimuscles.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarDataSet;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import me.theoria.wifimuscles.R;
import me.theoria.wifimuscles.data.builders.BarChartBuilder;
import me.theoria.wifimuscles.data.managers.charts.BarChartManager;
import me.theoria.wifimuscles.data.managers.info.ChartPopupManager;
import me.theoria.wifimuscles.data.model.ChartInfoCardModel;
import me.theoria.wifimuscles.databinding.FragmentBarChartBinding;
import me.theoria.wifimuscles.utils.ChartInfoUtils;
import me.theoria.wifimuscles.utils.ToastUtils;
import me.theoria.wifimuscles.view.adapters.ChartInfoCardAdapter;
import me.theoria.wifimuscles.viewmodel.DataUIViewModel;
import me.theoria.wifimuscles.viewmodel.WifiViewModel;

/**
 * Fragment that displays WiFi signal strength using a bar chart
 * and shows network-related info cards updated from LiveData.
 */
public class BarChartFragment extends Fragment {

    private FragmentBarChartBinding binding;

    // UI Components
    private BarChart barChart;
    private MaterialButton switchChartButton;
    private ChartInfoCardAdapter adapter;

    // BarChart data and manager
    private BarChartManager barChartManager;
    private BarDataSet barDataSet;

    // Popups
    private ChartPopupManager chartPopupManager;

    // ViewModels
    private WifiViewModel wifiViewModel;
    private DataUIViewModel dataUIViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBarChartBinding.inflate(inflater, container, false);

        // Setup UI and data bindings
        initBarChartUI();
        setupBarChart();
        setupRecyclerView();
        setupPopupManager();
        setupViewModels();
        setupObservers();
        setupChartSwitchButton();

        return binding.getRoot();
    }

    /** Initializes BarChart UI component from layout */
    private void initBarChartUI() {
        barChart = binding.barChart;
    }

    /** Sets up the BarChart with default configurations and dataset */
    private void setupBarChart() {
        barDataSet = BarChartBuilder.setupBarChart(barChart, requireContext());
        barChartManager = new BarChartManager(dataUIViewModel);
    }

    /** Configures RecyclerView for displaying WiFi info cards */
    private void setupRecyclerView() {
        RecyclerView recyclerView = binding.chartInfoRecyclerView;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new ChartInfoCardAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    /** Initializes popup manager and item click listener for info cards */
    private void setupPopupManager() {
        chartPopupManager = new ChartPopupManager(requireContext());

        adapter.setOnItemClickListener((item, position, view) -> {
            String description = ChartInfoUtils.getDescriptionForKey(requireContext(), item.getTitle());
            chartPopupManager.showChartPopup(view, item.getTitle(), description);
        });
    }

    /** Initializes ViewModels scoped to this Fragment */
    private void setupViewModels() {
        wifiViewModel = new ViewModelProvider(this).get(WifiViewModel.class);
        dataUIViewModel = new ViewModelProvider(this).get(DataUIViewModel.class);
    }

    /** Subscribes to LiveData to observe real-time WiFi and UI updates */
    private void setupObservers() {
        observeSignalData();
        observeSignalUI();
    }

    /** Observes real-time RSSI signal changes and updates the bar chart */
    private void observeSignalData() {
        wifiViewModel.getRssiLiveData().observe(getViewLifecycleOwner(), signals -> {
            if (signals == null || signals.isEmpty()) return;

            barChartManager.updateBarChart(signals, barChart, barDataSet);
            dataUIViewModel.updateSignalUI(signals);
        });
    }

    /** Observes UI-related LiveData and refreshes the info cards */
    private void observeSignalUI() {
        dataUIViewModel.getRssiText().observe(getViewLifecycleOwner(), v -> updateInfoCards());
        dataUIViewModel.getRssiEmoji().observe(getViewLifecycleOwner(), v -> updateInfoCards());
        dataUIViewModel.getDeviceIPText().observe(getViewLifecycleOwner(), v -> updateInfoCards());
        dataUIViewModel.getFrequencyText().observe(getViewLifecycleOwner(), v -> updateInfoCards());
        dataUIViewModel.getBandwidthText().observe(getViewLifecycleOwner(), v -> updateInfoCards());
        dataUIViewModel.getSSIDText().observe(getViewLifecycleOwner(), v -> updateInfoCards());
        dataUIViewModel.getBssidText().observe(getViewLifecycleOwner(), v -> updateInfoCards());
        dataUIViewModel.getLinkSpeed().observe(getViewLifecycleOwner(), v -> updateInfoCards());
        dataUIViewModel.getMaxLinkSpeed().observe(getViewLifecycleOwner(), v -> updateInfoCards());
        dataUIViewModel.getPingResult().observe(getViewLifecycleOwner(), v -> updateInfoCards());
        wifiViewModel.getWifiStandardLiveData().observe(getViewLifecycleOwner(), v -> updateInfoCards());


    }

    /** Updates the contents of the info card RecyclerView */
    private void updateInfoCards() {
        List<ChartInfoCardModel> items = new ArrayList<>();
        items.add(new ChartInfoCardModel(getString(R.string.ssid),
                safeGetValue(dataUIViewModel.getSSIDText()), R.drawable.icon_ssid));
        items.add(new ChartInfoCardModel(getString(R.string.rssi),
                safeGetValue(dataUIViewModel.getRssiText()),
                dataUIViewModel.getRssiEmoji().getValue() != null
                        ? dataUIViewModel.getRssiEmoji().getValue()
                        : R.drawable.emoji_bad));
        //items.add(new ChartInfoCardModel(getString(R.string.frequency_card_short), safeGetValue(dataUIViewModel.getFrequencyText()), R.drawable.icon_function));
        items.add(new ChartInfoCardModel(getString(R.string.frequency_band_card), safeGetValue(dataUIViewModel.getBandwidthText()), R.drawable.icon_function));
        items.add(new ChartInfoCardModel(getString(R.string.private_ip), safeGetValue(dataUIViewModel.getDeviceIPText()), R.drawable.icon_dns));
        items.add(new ChartInfoCardModel(getString(R.string.bssid), safeGetValue(dataUIViewModel.getBssidText()), R.drawable.icon_dns));
        items.add(new ChartInfoCardModel(getString(R.string.link_speed_card), safeGetValue(dataUIViewModel.getLinkSpeed()), R.drawable.icon_rocket));
        items.add(new ChartInfoCardModel(getString(R.string.max_link_speed_name), safeGetValue(dataUIViewModel.getMaxLinkSpeed()), R.drawable.icon_rocket));
        items.add(new ChartInfoCardModel(getString(R.string.ping), safeGetValue(dataUIViewModel.getPingResult()), R.drawable.icon_avg_time));

        adapter.updateItems(items);
    }

    /** Safely retrieves a LiveData<String> value or returns empty string */
    private String safeGetValue(androidx.lifecycle.LiveData<String> liveData) {
        String val = liveData.getValue();
        return val != null ? val : "";
    }

    /** Sets up the switch chart button to navigate to the LineChartFragment */
    private void setupChartSwitchButton() {
        switchChartButton = binding.switchChartButton;
        switchChartButton.setOnClickListener(v -> openLineChartFragment());
    }

    /** Replaces current fragment with the LineChartFragment */
    private void openLineChartFragment() {
        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new ChartFragment())
                .addToBackStack(null)
                .commit();
    }

    /** Start collecting data when fragment becomes visible */
    @Override
    public void onStart() {
        super.onStart();
        wifiViewModel.startUpdates();
        dataUIViewModel.runPingTest("8.8.8.8");
    }

    /** Stop data updates when fragment is no longer visible */
    @Override
    public void onStop() {
        super.onStop();
        wifiViewModel.stopUpdates();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;  // Avoid memory leaks
    }
}
