package me.theoria.wifimuscles.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarDataSet;

import java.util.List;

import me.theoria.wifimuscles.R;
import me.theoria.wifimuscles.data.builders.BarChartBuilder;
import me.theoria.wifimuscles.data.managers.BarChartManager;
import me.theoria.wifimuscles.data.model.WifiSignalModel;
import me.theoria.wifimuscles.utils.ToastUtils;
import me.theoria.wifimuscles.viewmodel.DataUIViewModel;
import me.theoria.wifimuscles.viewmodel.WifiViewModel;

/**
 * This fragment displays the WiFi RSSI signal strength in a barchart.
 */
public class BarChartFragment extends Fragment {

    // Reference UI elements for displaying information
    private TextView rssiTextView, ipTextView, frequencyTextView, bandwidthTextView, ssidTextView, macTextView, rxTextView, maxLinkSpeedTextView;
    private ImageView rssiEmojiView;
    private BarChart barChart;

    // Reference to ViewModels
    private WifiViewModel wifiViewModel;
    private DataUIViewModel dataUIViewModel;

    // Reference to chart components
    private BarChartManager chartManager;

    private BarDataSet barDataSet;

    /**
     * Inflates the layout and initializes the chart, ViewModels, and LiveData observables.
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bar_chart, container, false);
        bindViews(root);

        // Initialize ViewModels
        wifiViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(WifiViewModel.class);
        dataUIViewModel = new ViewModelProvider(this).get(DataUIViewModel.class);

        // Sets up barchart appearance and the dataset.
        barDataSet = BarChartBuilder.setupBarChart(barChart, requireContext());
        chartManager = new BarChartManager(dataUIViewModel);

        // Observe LiveData being passed from the ViewModels
        observeLiveData();

        return root;
    }

    /**
     * Method to bind UI elements from the layout to data.
     * @param root
     */
    private void bindViews(View root) {
        barChart = root.findViewById(R.id.barChart);
        rssiTextView = root.findViewById(R.id.rssiTextView);
        rssiEmojiView = root.findViewById(R.id.rssiEmoji);
        ipTextView = root.findViewById(R.id.ipBox);
        frequencyTextView = root.findViewById(R.id.frequencyBox);
        bandwidthTextView = root.findViewById(R.id.bandBox);
        ssidTextView = root.findViewById(R.id.ssidBox);
        macTextView = root.findViewById(R.id.macBox);
        rxTextView = root.findViewById(R.id.rxSpeedBox);
        maxLinkSpeedTextView = root.findViewById(R.id.maxSpeedBox);
    }

    /**
     * Method to observe LiveData from the ViewModels.
     */
    private void observeLiveData() {
        // Observes Wifi RSSI data
        wifiViewModel.getRssiLiveData().observe(getViewLifecycleOwner(), this::updateChart);

        // Observes additional Wifi data
        dataUIViewModel.getRssiText().observe(getViewLifecycleOwner(), rssiTextView::setText);
        dataUIViewModel.getRssiEmoji().observe(getViewLifecycleOwner(), rssiEmojiView::setImageResource);
        dataUIViewModel.getIpText().observe(getViewLifecycleOwner(), ipTextView::setText);
        dataUIViewModel.getFrequencyText().observe(getViewLifecycleOwner(), frequencyTextView::setText);
        dataUIViewModel.getBandwidthText().observe(getViewLifecycleOwner(), bandwidthTextView::setText);
        dataUIViewModel.getSSIDText().observe(getViewLifecycleOwner(), ssidTextView::setText);
        dataUIViewModel.getMac().observe(getViewLifecycleOwner(), macTextView::setText);
        dataUIViewModel.getLinkSpeed().observe(getViewLifecycleOwner(), rxTextView::setText);
        dataUIViewModel.getMaxLinkSpeed().observe(getViewLifecycleOwner(), maxLinkSpeedTextView::setText);

        // Toast message for extender when level 3 or less.
        dataUIViewModel.getToastLevelEvent().observe(getViewLifecycleOwner(), level -> {
            if (level == 3 || level == 2 || level == 1) {
                ToastUtils.showToastForExtender(requireContext(), level);
            }
        });
    }

    /**
     * Method to update the chart with new information from the data being passed through
     * the ViewModels.
     * @param signals
     */
    private void updateChart(List<WifiSignalModel> signals) {
        if (signals == null || signals.isEmpty()) return;

        // Retrieve and store the latest item.
        WifiSignalModel latest = signals.get(signals.size() - 1);

        // Displays the latest RSSI data as it updates in a TextView
        rssiTextView.setText(getString(R.string.rssi_display, latest.getRssi()));

        // Delegates chart update to the manager.
        chartManager.updateBarChart(signals, barChart, barDataSet);
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
