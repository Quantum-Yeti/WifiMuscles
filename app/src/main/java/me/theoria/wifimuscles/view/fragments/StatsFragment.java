package me.theoria.wifimuscles.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.List;

import me.theoria.wifimuscles.R;
import me.theoria.wifimuscles.data.model.WifiSignalModel;
import me.theoria.wifimuscles.viewmodel.DataUIViewModel;
import me.theoria.wifimuscles.viewmodel.NetworkViewModel;
import me.theoria.wifimuscles.viewmodel.WifiViewModel;

/**
 * This fragment inflates the Stats fragment and provides observables for real-time data.
 */
public class StatsFragment extends Fragment {

    private TextView frequencyTextView, bandwidthTextView, ipTextView, rssiTextView, ssidTextView, macTextView, rxTextView, maxLinkSpeedTextView;
    private TextView levelTextView, capabilitiesTextView, channelWidthTextView, centerFreq0TextView, centerFreq1TextView, passpointTextView, responderTextView;
    private ImageView rssiEmojiView;
    private LineChart chart;
    private LineDataSet primaryLineDataSet;
    private LineDataSet excellentSet, goodSet, fairSet, weakSet, unusableSet;
    private LineData lineData;

    private DataUIViewModel dataUIViewModel;
    private WifiViewModel wifiViewModel;
    private NetworkViewModel networkViewModel;

    /**
     * Method to inflate the fragment, initialize the ViewModels, and initialize
     * the return of the live data observables.
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
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_stats, container, false);
        bindViews(root);

        // Initialize Wifi View Model
        wifiViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(WifiViewModel.class);

        // ViewModel Helper Class
        dataUIViewModel = new ViewModelProvider(this).get(DataUIViewModel.class);

        // Initialize Network View Model
        networkViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(NetworkViewModel.class);

        // Observe LiveData being passed from the ViewModels
        observeLiveData();

        return root;

    }

    /**
     * Method that binds the various UI components.
     *
     * @param root
     */
    private void bindViews(View root) {
        rssiEmojiView = root.findViewById(R.id.rssiEmoji);
        rssiTextView = root.findViewById(R.id.rssiTextView);
        ipTextView = root.findViewById(R.id.ipBox);
        frequencyTextView = root.findViewById(R.id.frequencyBox);
        bandwidthTextView = root.findViewById(R.id.bandBox);
        ssidTextView = root.findViewById(R.id.ssidBox);
        macTextView = root.findViewById(R.id.macBox);
        rxTextView = root.findViewById(R.id.rxSpeedBox);
        maxLinkSpeedTextView = root.findViewById(R.id.maxSpeedBox);

        levelTextView = root.findViewById(R.id.levelBox);
        capabilitiesTextView = root.findViewById(R.id.capabilityBox);
        channelWidthTextView = root.findViewById(R.id.channelBox);
        centerFreq0TextView = root.findViewById(R.id.centerBox0);
        centerFreq1TextView = root.findViewById(R.id.centerBox1);
        passpointTextView = root.findViewById(R.id.passpointBox);
        responderTextView = root.findViewById(R.id.responderBox);
    }

    /**
     * Method to observe live data and help return the live data sets to their
     * respective view binding.
     *
     */
    public void observeLiveData() {
        // Observes additional Wifi data
        dataUIViewModel.getRssiEmoji().observe(getViewLifecycleOwner(), rssiEmojiView::setImageResource);
        dataUIViewModel.getRssiText().observe(getViewLifecycleOwner(), rssiTextView::setText);
        dataUIViewModel.getIpText().observe(getViewLifecycleOwner(), ipTextView::setText);
        dataUIViewModel.getFrequencyText().observe(getViewLifecycleOwner(), frequencyTextView::setText);
        dataUIViewModel.getBandwidthText().observe(getViewLifecycleOwner(), bandwidthTextView::setText);
        dataUIViewModel.getSSIDText().observe(getViewLifecycleOwner(), ssidTextView::setText);
        dataUIViewModel.getMac().observe(getViewLifecycleOwner(), macTextView::setText);
        dataUIViewModel.getLinkSpeed().observe(getViewLifecycleOwner(), rxTextView::setText);
        dataUIViewModel.getMaxLinkSpeed().observe(getViewLifecycleOwner(), maxLinkSpeedTextView::setText);

        wifiViewModel.getRssiLiveData().observe(getViewLifecycleOwner(), signals -> {
            if (signals != null && !signals.isEmpty()) {
                dataUIViewModel.updateSignalUI(signals);
            }
        });

        networkViewModel.getConnectedNetworkLiveData().observe(getViewLifecycleOwner(), network -> {
            if (network != null) {
                Context context = requireContext();

                levelTextView.setText(String.valueOf(network.getSignalLevel()));
                capabilitiesTextView.setText(network.getCapabilities());
                channelWidthTextView.setText(context.getString(R.string.channel_width, network.getChannelWidth()));
                centerFreq0TextView.setText(context.getString(R.string.center_freq_0, network.getCenterFreq0()));
                centerFreq1TextView.setText(context.getString(R.string.center_freq_1, network.getCenterFreq1()));
                passpointTextView.setText("Passpoint: " + (network.getPassPoint() ? "Yes" : "No"));
                responderTextView.setText("802.11mc: " + (network.getIs80211mcResponder() ? "Yes" : "No"));
            } else {
                levelTextView.setText(R.string.strength_level);
                capabilitiesTextView.setText("-");
                channelWidthTextView.setText("-");
                centerFreq0TextView.setText("-");
                centerFreq1TextView.setText("-");
                passpointTextView.setText("-");
                responderTextView.setText("-");
            }
        });

    }

    private void updateData(List<WifiSignalModel> signals) {
        if (signals == null || signals.isEmpty()) return;


        // Displays the latest RSSI data as it updates in a TextView
        WifiSignalModel latest = signals.get(signals.size() - 1);

        // Sets rssi - not needed
        //rssiTextView.setText("RSSI: " + latest.getRssi() + " dBm");

    }

    @Override
    public void onStart() {
        super.onStart();
        wifiViewModel.startUpdates();
        networkViewModel.startAutoUpdate();
    }

    @Override
    public void onStop() {
        super.onStop();
        wifiViewModel.stopUpdates();
        networkViewModel.stopAutoUpdate();
    }

}