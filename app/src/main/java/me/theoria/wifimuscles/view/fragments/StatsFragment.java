package me.theoria.wifimuscles.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import me.theoria.wifimuscles.R;
import me.theoria.wifimuscles.data.managers.PopupManager;
import me.theoria.wifimuscles.data.model.WifiSignalModel;
import me.theoria.wifimuscles.utils.SpeedConverter;
import me.theoria.wifimuscles.viewmodel.ConnectivityViewModel;
import me.theoria.wifimuscles.viewmodel.DHCPViewModel;
import me.theoria.wifimuscles.viewmodel.DataUIViewModel;
import me.theoria.wifimuscles.viewmodel.NetworkViewModel;
import me.theoria.wifimuscles.viewmodel.WifiViewModel;

public class StatsFragment extends Fragment {

    private TextView levelTextView, capabilitiesTextView, channelWidthTextView,
            centerFreq0TextView, centerFreq1TextView, passpointTextView, responderTextView;
    private TextView gatewayTextView, netmaskTextView, dns1TextView, dns2TextView,
            serverAddressTextView, leaseDurationTextView;
    private TextView transportTypeTextView, internetCapabilityTextView,
            validatedCapabilityTextView, meteredTextView, downstreamTextView, upstreamTextView;
    private ImageView rssiEmojiView;

    private PopupManager popupManager;

    private DataUIViewModel dataUIViewModel;
    private WifiViewModel wifiViewModel;
    private NetworkViewModel networkViewModel;
    private ConnectivityViewModel connectivityViewModel;
    private DHCPViewModel dhcpViewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_stats, container, false);
        bindViews(root);



        popupManager = new PopupManager(requireContext());

        // Initialize ViewModels
        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication());
        ViewModelProvider provider = new ViewModelProvider(this, factory);

        wifiViewModel = provider.get(WifiViewModel.class);
        networkViewModel = provider.get(NetworkViewModel.class);
        connectivityViewModel = provider.get(ConnectivityViewModel.class);
        dhcpViewModel = provider.get(DHCPViewModel.class);

        dataUIViewModel = new ViewModelProvider(this).get(DataUIViewModel.class);

        // Start observing LiveData after everything is initialized
        observeLiveData();

        return root;
    }

    /**
     * Method to grab live data from the ViewModels.
     */
    public void observeLiveData() {

        Context context = requireContext();

        // DHCP Text updates from DataUI ViewModel
        dhcpViewModel.getDhcpModelLiveData().observe(getViewLifecycleOwner(), dhcpModel -> {
            if (dhcpModel != null) {
                dataUIViewModel.updateFromDhcpModel(dhcpModel);
            }
        });

        dataUIViewModel.getRssiEmoji().observe(getViewLifecycleOwner(), rssiEmojiView::setImageResource);

        dataUIViewModel.getNetMaskText().observe(getViewLifecycleOwner(), text -> {
            if (text != null) {
                netmaskTextView.setText(context.getString(R.string.netmask, text));
            } else {
                netmaskTextView.setText(""); // Default empty value if null
            }
        });

        dataUIViewModel.getGatewayText().observe(getViewLifecycleOwner(), text -> {
            if (text != null) {
                gatewayTextView.setText(context.getString(R.string.gateway, text));
            } else {
                gatewayTextView.setText(""); // Default empty value if null
            }
        });

        dataUIViewModel.getDns1Text().observe(getViewLifecycleOwner(), text -> {
            if (text != null) {
                dns1TextView.setText(context.getString(R.string.dns1, text));
            } else {
                dns1TextView.setText(""); // Default empty value if null
            }
        });

        dataUIViewModel.getDns2Text().observe(getViewLifecycleOwner(), text -> {
            if (text != null) {
                dns2TextView.setText(context.getString(R.string.dns2, text));
            } else {
                dns2TextView.setText(""); // Default empty value if null
            }
        });

        /*dataUIViewModel.getServerAddressText().observe(getViewLifecycleOwner(), text -> {
            if (text != null) {
                serverAddressTextView.setText(text);
             } else {
                 serverAddressTextView.setText(""); // Default empty value if null
            }
        });*/

        dataUIViewModel.getLeaseDurationText().observe(getViewLifecycleOwner(), text -> {
            if (text != null) {
                leaseDurationTextView.setText(context.getString(R.string.lease_duration, text));
            } else {
                leaseDurationTextView.setText(""); // Default empty value if null
            }
        });

        // Observe RSSI info from the wifiViewModel
        wifiViewModel.getRssiLiveData().observe(getViewLifecycleOwner(), signals -> {
            if (signals != null && !signals.isEmpty()) {
                dataUIViewModel.updateSignalUI(signals);
            }
        });

        // Observe network info from the NetworkViewModel
        networkViewModel.getConnectedNetworkLiveData().observe(getViewLifecycleOwner(), network -> {
            if (network != null) {
                levelTextView.setText(context.getString(R.string.strength_level, network.getSignalLevel()));
                capabilitiesTextView.setText(context.getString(R.string.capability, network.getCapabilities()));
                channelWidthTextView.setText(context.getString(R.string.channel_width, network.getChannelWidth()));

                centerFreq0TextView.setText(context.getString(R.string.center_freq_0, network.getCenterFreq0()));
                centerFreq1TextView.setText(context.getString(R.string.center_freq_1, network.getCenterFreq1()));

                String passpointStatus = network.getPassPoint() ? "Yes" : "No";
                passpointTextView.setText(String.format(getString(R.string.passpoint), passpointStatus));

                String responderStatus = network.getIs80211mcResponder() ? "Yes" : "No";
                responderTextView.setText(String.format(getString(R.string.responder), responderStatus));
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

        // Observe live data from the ConnectivityViewModel
        connectivityViewModel.getConnectivityStatus().observe(getViewLifecycleOwner(), model -> {
            if (model != null) {

                transportTypeTextView.setText(
                        context.getString(R.string.transport, model.getTransportType().name())
                );

                String capabilityStatus = model.hasInternet() ? "Yes" : "No";
                internetCapabilityTextView.setText(String.format(getString(R.string.internet), capabilityStatus));

                String validateStatus = model.isValidated() ? "Yes" : "No";
                validatedCapabilityTextView.setText(String.format(getString(R.string.validation), validateStatus));

                String meteredStatus = model.isMetered() ? "Yes" : "No";
                meteredTextView.setText(String.format(getString(R.string.metered), meteredStatus));

                int downstream = model.getDownstreamKbps();
                String speedTextDown = SpeedConverter.speedConvert(downstream);
                downstreamTextView.setText(String.format(getString(R.string.downstream_s), speedTextDown));

                int upstream = model.getUpstreamKbps();
                String speedTextUp = SpeedConverter.speedConvert(upstream);
                upstreamTextView.setText(String.format(getString(R.string.upstream), speedTextUp));
            } else {
                transportTypeTextView.setText(String.format(getString(R.string.nothing)));
                internetCapabilityTextView.setText(String.format(getString(R.string.nothing)));
                validatedCapabilityTextView.setText(String.format(getString(R.string.nothing)));
                meteredTextView.setText(String.format(getString(R.string.nothing)));
                downstreamTextView.setText(String.format(getString(R.string.nothing)));
                upstreamTextView.setText(String.format(getString(R.string.nothing)));
            }
        });
    }

    /**
     * Method to bind UI components.
     * @param root
     */
    private void bindViews(View root) {
        rssiEmojiView = root.findViewById(R.id.rssiEmoji);
        levelTextView = root.findViewById(R.id.levelBox);
        capabilitiesTextView = root.findViewById(R.id.capabilityBox);
        channelWidthTextView = root.findViewById(R.id.channelBox);
        centerFreq0TextView = root.findViewById(R.id.centerBox0);
        centerFreq1TextView = root.findViewById(R.id.centerBox1);
        passpointTextView = root.findViewById(R.id.passpointBox);
        responderTextView = root.findViewById(R.id.responderBox);
        gatewayTextView = root.findViewById(R.id.gatewayBox);
        netmaskTextView = root.findViewById(R.id.netmaskBox);
        dns1TextView = root.findViewById(R.id.dns1Box);
        dns2TextView = root.findViewById(R.id.dns2Box);
        serverAddressTextView = root.findViewById(R.id.serverAddressBox);
        leaseDurationTextView = root.findViewById(R.id.leaseDurationBox);
        transportTypeTextView = root.findViewById(R.id.transportBox);
        internetCapabilityTextView = root.findViewById(R.id.isInternetBox);
        validatedCapabilityTextView = root.findViewById(R.id.validateBox);
        meteredTextView = root.findViewById(R.id.meteredBox);
        downstreamTextView = root.findViewById(R.id.downstreamBox);
        upstreamTextView = root.findViewById(R.id.upstreamBox);

        // Popup Window Binding onClick
        levelTextView.setOnClickListener(v -> popupManager.wifiLevelPopup(v));
        capabilitiesTextView.setOnClickListener(v -> popupManager.capabilitiesPopup(v));
        channelWidthTextView.setOnClickListener(v -> popupManager.channelwidthPopup(v));
        centerFreq0TextView.setOnClickListener(v -> popupManager.centerfreq0Popup(v));
        centerFreq1TextView.setOnClickListener(v -> popupManager.centerfreq1Popup(v));
        passpointTextView.setOnClickListener(v -> popupManager.passpointPopup(v));
        responderTextView.setOnClickListener(v -> popupManager.responderPopup(v));
        gatewayTextView.setOnClickListener(v -> popupManager.gatewayPopup(v));
        netmaskTextView.setOnClickListener(v -> popupManager.netmaskPopup(v));
        dns1TextView.setOnClickListener(v -> popupManager.dns1Popup(v));
        dns2TextView.setOnClickListener(v -> popupManager.dns2Popup(v));
        //serverAddressTextView.setOnClickListener(v -> popupManager.wifiLevelPopup(v));
        leaseDurationTextView.setOnClickListener(v -> popupManager.leasePopup(v));
        transportTypeTextView.setOnClickListener(v -> popupManager.transportPopup(v));
        internetCapabilityTextView.setOnClickListener(v -> popupManager.internetPopup(v));
        validatedCapabilityTextView.setOnClickListener(v -> popupManager.validationPopup(v));
        meteredTextView.setOnClickListener(v -> popupManager.meteredPopup(v));
        downstreamTextView.setOnClickListener(v -> popupManager.downstreamPopup(v));
        upstreamTextView.setOnClickListener(v -> popupManager.upstreamPopup(v));

    }

    @Override
    public void onStart() {
        super.onStart();
        wifiViewModel.startUpdates();
        networkViewModel.startAutoUpdate();
        dhcpViewModel.startAutoUpdate();
        connectivityViewModel.startAutoUpdate();
    }

    @Override
    public void onStop() {
        super.onStop();
        wifiViewModel.stopUpdates();
        networkViewModel.stopAutoUpdate();
        dhcpViewModel.stopAutoUpdate();
        connectivityViewModel.stopAutoUpdate();
    }
}
