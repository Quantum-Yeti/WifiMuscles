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

import org.w3c.dom.Text;

import java.util.List;

import me.theoria.wifimuscles.R;
import me.theoria.wifimuscles.data.model.WifiSignalModel;
import me.theoria.wifimuscles.viewmodel.DHCPViewModel;
import me.theoria.wifimuscles.viewmodel.DataUIViewModel;
import me.theoria.wifimuscles.viewmodel.NetworkViewModel;
import me.theoria.wifimuscles.viewmodel.WifiViewModel;

/**
 * This fragment inflates the Stats fragment and provides observables for real-time data.
 */
public class StatsFragment extends Fragment {

    //private TextView frequencyTextView, bandwidthTextView, ipTextView, rssiTextView, ssidTextView, macTextView, rxTextView, maxLinkSpeedTextView;
    private TextView levelTextView, capabilitiesTextView, channelWidthTextView, centerFreq0TextView, centerFreq1TextView, passpointTextView, responderTextView;
    private TextView ip2TextView, gatewayTextView, netmaskTextView, dns1TextView, dns2TextView, serverAddressTextView, leaseDurationTextView;
    private ImageView rssiEmojiView;
    private LineChart chart;
    private LineDataSet primaryLineDataSet;
    private LineDataSet excellentSet, goodSet, fairSet, weakSet, unusableSet;
    private LineData lineData;

    private DataUIViewModel dataUIViewModel;
    private WifiViewModel wifiViewModel;
    private NetworkViewModel networkViewModel;
    private DHCPViewModel dhcpViewModel;

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

        // Initialize DHCP View Model
        dhcpViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(DHCPViewModel.class);

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

        // General Connection Info Bindings
        rssiEmojiView = root.findViewById(R.id.rssiEmoji);

        /*rssiTextView = root.findViewById(R.id.rssiTextView);
        ipTextView = root.findViewById(R.id.ipBox);
        frequencyTextView = root.findViewById(R.id.frequencyBox);
        bandwidthTextView = root.findViewById(R.id.bandBox);
        ssidTextView = root.findViewById(R.id.ssidBox);
        macTextView = root.findViewById(R.id.macBox);
        rxTextView = root.findViewById(R.id.rxSpeedBox);
        maxLinkSpeedTextView = root.findViewById(R.id.maxSpeedBox);*/

        // Network Stats Bindings
        levelTextView = root.findViewById(R.id.levelBox);
        capabilitiesTextView = root.findViewById(R.id.capabilityBox);
        channelWidthTextView = root.findViewById(R.id.channelBox);
        centerFreq0TextView = root.findViewById(R.id.centerBox0);
        centerFreq1TextView = root.findViewById(R.id.centerBox1);
        passpointTextView = root.findViewById(R.id.passpointBox);
        responderTextView = root.findViewById(R.id.responderBox);

        // DHCP Stats Bindings
        gatewayTextView = root.findViewById(R.id.gatewayBox);
        netmaskTextView = root.findViewById(R.id.netmaskBox);
        dns1TextView = root.findViewById(R.id.dns1Box);
        dns2TextView = root.findViewById(R.id.dns2Box);
        serverAddressTextView = root.findViewById(R.id.serverAddressBox);
        leaseDurationTextView = root.findViewById(R.id.leaseDurationBox);


    }

    /**
     * Method to observe live data and help return the live data sets to their
     * respective view binding.
     *
     */
    public void observeLiveData() {
        // General Data Observers
        dataUIViewModel.getRssiEmoji().observe(getViewLifecycleOwner(), rssiEmojiView::setImageResource);

        dataUIViewModel.getNetMaskText().observe(getViewLifecycleOwner(), netmaskTextView::setText);
        dataUIViewModel.getGatewayText().observe(getViewLifecycleOwner(), gatewayTextView::setText);
        dataUIViewModel.getDns1Text().observe(getViewLifecycleOwner(), dns1TextView::setText);
        dataUIViewModel.getDns2Text().observe(getViewLifecycleOwner(), dns2TextView::setText);
        dataUIViewModel.getServerAddressText().observe(getViewLifecycleOwner(), serverAddressTextView::setText);
        dataUIViewModel.getLeaseDurationText().observe(getViewLifecycleOwner(), leaseDurationTextView::setText);


        wifiViewModel.getRssiLiveData().observe(getViewLifecycleOwner(), signals -> {
            if (signals != null && !signals.isEmpty()) {
                dataUIViewModel.updateSignalUI(signals);
            }
        });

        // Network Observer
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

        // DHCP Observer
        dhcpViewModel.getDhcpModelLiveData().observe(getViewLifecycleOwner(), dhcpModel -> {
            if (dhcpModel != null) {
                dataUIViewModel.updateFromDhcpModel(dhcpModel);
            } else {
                Log.w("StatsFragment", "DHCP model was null");
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
        dhcpViewModel.startAutoUpdate();
    }

    @Override
    public void onStop() {
        super.onStop();
        wifiViewModel.stopUpdates();
        networkViewModel.stopAutoUpdate();
        dhcpViewModel.stopAutoUpdate();
    }

}