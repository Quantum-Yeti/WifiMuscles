package me.theoria.wifimuscles.view.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import me.theoria.wifimuscles.R;
import me.theoria.wifimuscles.data.builders.LineChartBuilder;

import me.theoria.wifimuscles.data.managers.info.SignalProcessManager;
import me.theoria.wifimuscles.data.managers.charts.LineChartManager;
import me.theoria.wifimuscles.data.managers.info.ChartPopupManager;
import me.theoria.wifimuscles.data.model.ChartInfoCardModel;
import me.theoria.wifimuscles.data.model.WifiSignalModel;
import me.theoria.wifimuscles.databinding.FragmentChartBinding;
import me.theoria.wifimuscles.utils.CalculationUtils;
import me.theoria.wifimuscles.utils.ChartInfoUtils;
import me.theoria.wifimuscles.view.adapters.ChartInfoCardAdapter;
import me.theoria.wifimuscles.viewmodel.DataUIViewModel;
import me.theoria.wifimuscles.viewmodel.WifiViewModel;

/**
 * Fragment that displays WiFi signal strength using a line chart
 * and shows network-related info cards updated from LiveData.
 */
public class ChartFragment extends Fragment {

    private FragmentChartBinding binding;
    private ChartInfoCardAdapter adapter;

    // Chart datasets
    private LineDataSet primaryLineDataSet, excellentSet, goodSet, fairSet, weakSet, unusableSet, interferenceDataSet;
    private LineData lineData;
    private LineChartManager lineChartManager;

    // ViewModels
    private DataUIViewModel dataUIViewModel;
    private WifiViewModel wifiViewModel;

    // Popup helper
    private ChartPopupManager chartPopupManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentChartBinding.inflate(inflater, container, false);

        // Initialize Recycler View
        initRecyclerView();

        // Initialize Popups
        setupPopupManager();

        // Initialize ViewModels
        setupViewModels();

        // Initialize the Line Chart
        setupLineChart();

        // Initialize observables from ViewModels
        observeViewModels();

        // Initialize the chart switching button
        setupSwitchChartButton();

        // Start wifiViewModel data
        wifiViewModel.startUpdates();

        // Initialize the progress indicator during loading of data
        showProgress(true);


        // Return the root binding
        return binding.getRoot();
    }

    /**
     * Method to initialize RecyclerView to display info cards in a grid 2x2 layout with onClick info popups.
     */
    private void initRecyclerView() {
        adapter = new ChartInfoCardAdapter(new ArrayList<>());
        binding.chartInfoRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.chartInfoRecyclerView.setAdapter(adapter);
        binding.chartInfoRecyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter.setOnItemClickListener((item, position, view) -> {
            String description = ChartInfoUtils.getDescriptionForKey(requireContext(), item.getTitle());
            chartPopupManager.showChartPopup(view, item.getTitle(), description);
        });
    }

    /**
     * Method to initialize the popup manager that shows descriptive text for each data item.
     */
    private void setupPopupManager() {
        chartPopupManager = new ChartPopupManager(requireContext());
    }

    /**
     * Method to initialize and obtain the ViewModels required for this fragment.
     */
    private void setupViewModels() {
        wifiViewModel = new ViewModelProvider(this).get(WifiViewModel.class);
        dataUIViewModel = new ViewModelProvider(this).get(DataUIViewModel.class);
    }

    /**
     * Method to configure the primary LineChart and the required datasets.
     */
    private void setupLineChart() {
        LineChartBuilder.ChartSetupResult chartResults =
                LineChartBuilder.setupChartConfig(binding.lineChart, requireContext());

        primaryLineDataSet = chartResults.primaryDataSet;
        excellentSet = chartResults.excellentSet;
        goodSet = chartResults.goodSet;
        fairSet = chartResults.fairSet;
        weakSet = chartResults.weakSet;
        unusableSet = chartResults.unusableSet;
        interferenceDataSet = chartResults.interferenceDataSet;
        lineData = chartResults.lineData;

        binding.lineChart.setData(lineData);
        binding.lineChart.invalidate();

        lineChartManager = new LineChartManager(new SignalProcessManager(), dataUIViewModel);
    }

    /**
     * Method to instantiate the LiveData observer method from the ViewModels that updates the UI.
     */
    private void observeViewModels() {
        observeSignalData();
        observeSignalUI();
    }

    /**
     * Method to observe the RSSI LiveData for the LineChart and update it with each change.
     */
    private void observeSignalData() {
        wifiViewModel.getRssiLiveData().observe(getViewLifecycleOwner(), signals -> {
            if (signals == null) {
                showProgress(false);
                return;
            }
            lineChartManager.updateLineChart(
                    signals, binding.lineChart,
                    primaryLineDataSet, excellentSet, goodSet, fairSet, weakSet, unusableSet,
                    interferenceDataSet,
                    lineData
            );

            updateLegendWithCurrentValues(signals);

            dataUIViewModel.updateSignalUI(signals);

            binding.lineChart.setVisibility(View.VISIBLE);
            showProgress(false);
        });
    }

    /**
     * Method to obtain and update the legend values for the line chart.
     */
    private void updateLegendWithCurrentValues(List<WifiSignalModel> signals) {
        float rssi = signals.get(0).getRssi(); // Example RSSI value
        float interference = signals.get(0).getInterferenceLevel(); // Example interference value

        String rssiText = String.format(Locale.getDefault(), "RSSI: %.1f dBm", rssi);
        String interferenceText = String.format(Locale.getDefault(), "Interference: %.1f%%", interference);

        // Create the legend entries
        LegendEntry rssiLegendEntry = new LegendEntry();
        rssiLegendEntry.label = rssiText;
        rssiLegendEntry.formColor = primaryLineDataSet.getColor();
        rssiLegendEntry.form = Legend.LegendForm.LINE;

        LegendEntry interferenceLegendEntry = new LegendEntry();
        interferenceLegendEntry.label = interferenceText;
        interferenceLegendEntry.formColor = interferenceDataSet.getColor();
        interferenceLegendEntry.form = Legend.LegendForm.LINE;

        // Update the legend with the new entries
        List<LegendEntry> legendEntries = new ArrayList<>(Arrays.asList(rssiLegendEntry, interferenceLegendEntry));
        binding.lineChart.getLegend().setCustom(legendEntries);
    }

    /**
     * Method to observe the LiveData for all data sets within the fragment and update accordingly.
     */
    private void observeSignalUI() {
        dataUIViewModel.getRssiText().observe(getViewLifecycleOwner(), text -> updateInfoCards());
        dataUIViewModel.getRssiEmoji().observe(getViewLifecycleOwner(), emoji -> updateInfoCards());
        dataUIViewModel.getDeviceIPText().observe(getViewLifecycleOwner(), ip -> updateInfoCards());
        dataUIViewModel.getFrequencyText().observe(getViewLifecycleOwner(), freq -> updateInfoCards());
        dataUIViewModel.getBandwidthText().observe(getViewLifecycleOwner(), bw -> updateInfoCards());
        dataUIViewModel.getSSIDText().observe(getViewLifecycleOwner(), ssid -> updateInfoCards());
        dataUIViewModel.getBssidText().observe(getViewLifecycleOwner(), bssid -> updateInfoCards());
        //dataUIViewModel.getLinkSpeed().observe(getViewLifecycleOwner(), speed -> updateInfoCards());
        wifiViewModel.getInterferencePercentLiveData().observe(getViewLifecycleOwner(), interference -> updateInfoCards());
        dataUIViewModel.getMaxLinkSpeed().observe(getViewLifecycleOwner(), maxSpeed -> updateInfoCards());
        dataUIViewModel.getPingResult().observe(getViewLifecycleOwner(), ping -> updateInfoCards());

        wifiViewModel.getWifiStandardLiveData().observe(getViewLifecycleOwner(), standard -> updateInfoCards());
    }

    /**
     * Method to build and update the RecyclerView based on the LiveData values.
     */
    private void updateInfoCards() {

        // Grab the interference percentage and format
        Float interferencePercent = wifiViewModel.getInterferencePercentLiveData().getValue();
        String interferencePercentText = (interferencePercent != null)
                ? String.format(Locale.getDefault(), "%.1f%%", interferencePercent)
                : getString(R.string.no_data);

        // Grab the channel number and format
        String frequency = safeGetValue(dataUIViewModel.getFrequencyText());
        int frequencyMHz = parseFrequency(frequency);
        int wifiChannel = CalculationUtils.calculateChannel(frequencyMHz);
        String channelText = (wifiChannel != -1)
                ? String.valueOf(wifiChannel)
                : getString(R.string.no_data);


        // Cache values locally to avoid slow repeated getValue() calls
        String ssid = safeGetValue(dataUIViewModel.getSSIDText());
        String rssi = safeGetValue(dataUIViewModel.getRssiText());
        Integer rssiEmoji = dataUIViewModel.getRssiEmoji().getValue();
        //String frequency = safeGetValue(dataUIViewModel.getFrequencyText());
        String bandwidth = safeGetValue(dataUIViewModel.getBandwidthText());
        String privateIP = safeGetValue(dataUIViewModel.getDeviceIPText());
        String bssid = safeGetValue(dataUIViewModel.getBssidText());
        String linkSpeed = safeGetValue(dataUIViewModel.getLinkSpeed());
        String maxLinkSpeed = safeGetValue(dataUIViewModel.getMaxLinkSpeed());
        String ping = safeGetValue(dataUIViewModel.getPingResult());



        List<ChartInfoCardModel> items = new ArrayList<>();
        items.add(new ChartInfoCardModel(getString(R.string.ssid), ssid, R.drawable.icon_ssid));
        items.add(new ChartInfoCardModel(
                getString(R.string.rssi),
                rssi,
                rssiEmoji != null ? rssiEmoji : R.drawable.emoji_bad
        ));
        //items.add(new ChartInfoCardModel(getString(R.string.frequency_card_short), frequency, R.drawable.icon_function));
        items.add(new ChartInfoCardModel(getString(R.string.frequency_band_card), bandwidth, R.drawable.icon_function));
        items.add(new ChartInfoCardModel(getString(R.string.private_ip), privateIP, R.drawable.icon_dns));
        items.add(new ChartInfoCardModel(getString(R.string.bssid), bssid, R.drawable.icon_dns));
        //items.add(new ChartInfoCardModel(getString(R.string.link_speed_card), linkSpeed, R.drawable.icon_rocket));
        items.add(new ChartInfoCardModel(getString(R.string.interference), interferencePercentText, R.drawable.icon_equalizer));
        items.add(new ChartInfoCardModel(getString(R.string.channel), channelText, R.drawable.icon_channel));
        //items.add(new ChartInfoCardModel(getString(R.string.max_link_speed_name), maxLinkSpeed, R.drawable.icon_rocket));
        items.add(new ChartInfoCardModel(getString(R.string.ping), ping, R.drawable.icon_avg_time));

        adapter.updateItems(items);
    }

    /**
     * Helper method for null-safety when LiveData is potentially empty.
     */
    private String safeGetValue(androidx.lifecycle.LiveData<String> liveData) {
        String val = liveData.getValue();
        return val != null ? val : "";
    }

    /**
     * Method to hide or show the progress bar and the calculating... text.
     */
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable hideProgressRunnable;
    private long animationStartTime = 0;

    private void showProgress(boolean show) {
        if (binding == null) return;

        if (show) {
            // Show progress overlay and start animation
            binding.progressOverlay.setVisibility(View.VISIBLE);
            binding.progressBar.playAnimation();
            animationStartTime = System.currentTimeMillis();

            // Cancel any existing delayed runnable
            if (hideProgressRunnable != null) {
                handler.removeCallbacks(hideProgressRunnable);
            }

        } else {
            long elapsed = System.currentTimeMillis() - animationStartTime;
            long remaining = 2000 - elapsed; // Ensure 2 seconds minimum display

            // Cancel any previously scheduled hide
            if (hideProgressRunnable != null) {
                handler.removeCallbacks(hideProgressRunnable);
            }

            hideProgressRunnable = () -> {
                if (binding != null) {
                    binding.progressBar.cancelAnimation();
                    binding.progressOverlay.setVisibility(View.GONE);
                }
            };

            handler.postDelayed(hideProgressRunnable, Math.max(remaining, 0));
        }
    }


    /**
     * Method to initialize the onClickListener for the chart switching button that opens the barchart and vice-versa.
     */
    private void setupSwitchChartButton() {
        binding.switchChartButton.setOnClickListener(v -> openBarChartFragment());
    }

    /**
     * Method to open the barchart fragment with chart switching button and add transaction to back-stack.
     */
    private void openBarChartFragment() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new BarChartFragment())
                .addToBackStack(null)
                .commit();
    }

    private int parseFrequency(String frequencyString) {
        if (frequencyString == null) return -1;
        try {
            return Integer.parseInt(frequencyString.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        wifiViewModel.startUpdates();
        dataUIViewModel.runPingTest("8.8.8.8");
    }

    @Override
    public void onStop() {
        super.onStop();
        wifiViewModel.stopUpdates();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Cancel pending callbacks for loading animation
        if (hideProgressRunnable != null) {
            handler.removeCallbacks(hideProgressRunnable);
        }

        binding = null;  // Avoid memory leaks
    }
}
