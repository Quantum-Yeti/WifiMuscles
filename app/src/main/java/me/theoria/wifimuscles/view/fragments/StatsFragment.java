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
import me.theoria.wifimuscles.data.managers.info.StatsPopupManager;
import me.theoria.wifimuscles.utils.SpeedConverter;
import me.theoria.wifimuscles.viewmodel.ConnectivityViewModel;
import me.theoria.wifimuscles.viewmodel.DHCPViewModel;
import me.theoria.wifimuscles.viewmodel.DataUIViewModel;
import me.theoria.wifimuscles.viewmodel.NetworkViewModel;
import me.theoria.wifimuscles.viewmodel.WifiViewModel;

public class StatsFragment extends Fragment {

    private TextView levelTextView, interferenceTextView, capabilitiesTextView, channelWidthTextView,
            centerFreq0TextView, centerFreq1TextView, passpointTextView, responderTextView, channelNumberTextView;
    private TextView gatewayTextView, netmaskTextView, dns1TextView, dns2TextView,
            leaseDurationTextView;
    private TextView transportTypeTextView, internetCapabilityTextView,
            validatedCapabilityTextView, meteredTextView, downstreamTextView, upstreamTextView;
    private ImageView rssiEmojiView;

    private StatsPopupManager statsPopupManager;

    private DataUIViewModel dataUIViewModel;
    private WifiViewModel wifiViewModel;
    private NetworkViewModel networkViewModel;
    private ConnectivityViewModel connectivityViewModel;
    private DHCPViewModel dhcpViewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_stats, container, false);
        bindViews(root);

        statsPopupManager = new StatsPopupManager(requireContext());

        ViewModelProvider.AndroidViewModelFactory factory =
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication());
        ViewModelProvider provider = new ViewModelProvider(this, factory);

        wifiViewModel = provider.get(WifiViewModel.class);
        networkViewModel = provider.get(NetworkViewModel.class);
        connectivityViewModel = provider.get(ConnectivityViewModel.class);
        dhcpViewModel = provider.get(DHCPViewModel.class);
        dataUIViewModel = provider.get(DataUIViewModel.class);

        observeLiveData();

        return root;
    }

    private void observeLiveData() {
        observeDhcp();
        observeDataUI();
        observeWifi();
        observeNetwork();
        observeConnectivity();
    }

    private void observeDhcp() {
        dhcpViewModel.getDhcpModelLiveData().observe(getViewLifecycleOwner(), dhcpModel -> {
            if (dhcpModel != null) {
                dataUIViewModel.updateFromDhcpModel(dhcpModel);
            }
        });

        dataUIViewModel.getGatewayText().observe(getViewLifecycleOwner(),
                text -> setTextOrDefault(gatewayTextView, text));
        dataUIViewModel.getNetmaskText().observe(getViewLifecycleOwner(),
                text -> setTextOrDefault(netmaskTextView, text));
        dataUIViewModel.getDns1Text().observe(getViewLifecycleOwner(),
                text -> setTextOrDefault(dns1TextView, text));
        dataUIViewModel.getDns2Text().observe(getViewLifecycleOwner(),
                text -> setTextOrDefault(dns2TextView, text));
        dataUIViewModel.getLeaseDurationText().observe(getViewLifecycleOwner(),
                text -> setTextOrDefault(leaseDurationTextView, text));
    }

    private void observeDataUI() {
        dataUIViewModel.getRssiEmoji().observe(getViewLifecycleOwner(), rssiEmojiView::setImageResource);
    }

    private void observeWifi() {
        wifiViewModel.getRssiLiveData().observe(getViewLifecycleOwner(), signals -> {
            if (signals != null && !signals.isEmpty()) {
                dataUIViewModel.updateSignalUI(signals);
            }
        });

        wifiViewModel.getInterferenceLevelLiveData().observe(getViewLifecycleOwner(),
                interference -> interferenceTextView.setText(getString(R.string.interference, interference)));
    }

    private void observeNetwork() {
        Context context = requireContext();

        networkViewModel.getConnectedNetworkLiveData().observe(getViewLifecycleOwner(), network -> {
            if (network != null) {
                levelTextView.setText(context.getString(R.string.strength_level, network.getSignalLevel()));
                capabilitiesTextView.setText(context.getString(R.string.capability, network.getCapabilities()));
                channelNumberTextView.setText(context.getString(R.string.channel_number_txt, network.getChannelNumber()));
                channelWidthTextView.setText(context.getString(R.string.channel_width, network.getChannelWidth()));
                centerFreq0TextView.setText(context.getString(R.string.center_freq_0, network.getCenterFreq0()));
                centerFreq1TextView.setText(context.getString(R.string.center_freq_1, network.getCenterFreq1()));
                passpointTextView.setText(String.format(getString(R.string.passpoint), yesNo(network.getPassPoint())));
                responderTextView.setText(String.format(getString(R.string.responder), yesNo(network.getIs80211mcResponder())));
            } else {
                setNoDataToNetworkViews();
            }
        });
    }

    private void observeConnectivity() {
        Context context = requireContext();

        connectivityViewModel.getConnectivityStatus().observe(getViewLifecycleOwner(), model -> {
            if (model != null) {
                transportTypeTextView.setText(context.getString(R.string.transport, model.getTransportType().name()));

                internetCapabilityTextView.setText(String.format(getString(R.string.internet), yesNo(model.hasInternet())));
                validatedCapabilityTextView.setText(String.format(getString(R.string.validation), yesNo(model.isValidated())));
                meteredTextView.setText(String.format(getString(R.string.metered), yesNo(model.isMetered())));

                downstreamTextView.setText(String.format(getString(R.string.downstream_s), SpeedConverter.speedConvert(model.getDownstreamKbps())));
                upstreamTextView.setText(String.format(getString(R.string.upstream), SpeedConverter.speedConvert(model.getUpstreamKbps())));
            } else {
                transportTypeTextView.setText(R.string.no_data);
                internetCapabilityTextView.setText(R.string.no_data);
                validatedCapabilityTextView.setText(R.string.no_data);
                meteredTextView.setText(R.string.no_data);
                downstreamTextView.setText(R.string.no_data);
                upstreamTextView.setText(R.string.no_data);
            }
        });
    }

    private void setNoDataToNetworkViews() {
        levelTextView.setText(R.string.no_data);
        capabilitiesTextView.setText(R.string.no_data);
        channelWidthTextView.setText(R.string.no_data);
        centerFreq0TextView.setText(R.string.no_data);
        centerFreq1TextView.setText(R.string.no_data);
        passpointTextView.setText(R.string.no_data);
        responderTextView.setText(R.string.no_data);
        channelNumberTextView.setText(R.string.no_data);
    }

    private void setTextOrDefault(TextView view, String text) {
        if (text != null) {
            view.setText(text);
        } else {
            view.setText(R.string.no_data);
        }
    }

    private String yesNo(boolean condition) {
        return condition ? "Yes" : "No";
    }

    private void bindViews(View root) {
        rssiEmojiView = root.findViewById(R.id.rssiEmoji);
        levelTextView = root.findViewById(R.id.levelBox);
        interferenceTextView = root.findViewById(R.id.interferenceBox);
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
        leaseDurationTextView = root.findViewById(R.id.leaseDurationBox);
        transportTypeTextView = root.findViewById(R.id.transportBox);
        internetCapabilityTextView = root.findViewById(R.id.isInternetBox);
        validatedCapabilityTextView = root.findViewById(R.id.validateBox);
        meteredTextView = root.findViewById(R.id.meteredBox);
        downstreamTextView = root.findViewById(R.id.downstreamBox);
        upstreamTextView = root.findViewById(R.id.upstreamBox);
        channelNumberTextView = root.findViewById(R.id.channelNumberBox);

        // Popup Window Binding onClick
        levelTextView.setOnClickListener(v -> statsPopupManager.wifiLevelPopup(v));
        capabilitiesTextView.setOnClickListener(v -> statsPopupManager.capabilitiesPopup(v));
        channelWidthTextView.setOnClickListener(v -> statsPopupManager.channelWidthPopup(v));
        centerFreq0TextView.setOnClickListener(v -> statsPopupManager.centerFreq0Popup(v));
        centerFreq1TextView.setOnClickListener(v -> statsPopupManager.centerFreq1Popup(v));
        passpointTextView.setOnClickListener(v -> statsPopupManager.passpointPopup(v));
        responderTextView.setOnClickListener(v -> statsPopupManager.responderPopup(v));
        gatewayTextView.setOnClickListener(v -> statsPopupManager.gatewayPopup(v));
        netmaskTextView.setOnClickListener(v -> statsPopupManager.netmaskPopup(v));
        dns1TextView.setOnClickListener(v -> statsPopupManager.dns1Popup(v));
        dns2TextView.setOnClickListener(v -> statsPopupManager.dns2Popup(v));
        leaseDurationTextView.setOnClickListener(v -> statsPopupManager.leasePopup(v));
        transportTypeTextView.setOnClickListener(v -> statsPopupManager.transportPopup(v));
        internetCapabilityTextView.setOnClickListener(v -> statsPopupManager.internetPopup(v));
        validatedCapabilityTextView.setOnClickListener(v -> statsPopupManager.validationPopup(v));
        meteredTextView.setOnClickListener(v -> statsPopupManager.meteredPopup(v));
        downstreamTextView.setOnClickListener(v -> statsPopupManager.downstreamPopup(v));
        upstreamTextView.setOnClickListener(v -> statsPopupManager.upstreamPopup(v));
        interferenceTextView.setOnClickListener(v -> statsPopupManager.interferencePopup(v));
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Nullify all views to prevent memory leaks
        rssiEmojiView = null;
        levelTextView = null;
        interferenceTextView = null;
        capabilitiesTextView = null;
        channelWidthTextView = null;
        centerFreq0TextView = null;
        centerFreq1TextView = null;
        passpointTextView = null;
        responderTextView = null;
        gatewayTextView = null;
        netmaskTextView = null;
        dns1TextView = null;
        dns2TextView = null;
        leaseDurationTextView = null;
        transportTypeTextView = null;
        internetCapabilityTextView = null;
        validatedCapabilityTextView = null;
        meteredTextView = null;
        downstreamTextView = null;
        upstreamTextView = null;
        channelNumberTextView = null;

        statsPopupManager = null;

        dataUIViewModel = null;
        wifiViewModel = null;
        networkViewModel = null;
        connectivityViewModel = null;
        dhcpViewModel = null;
    }
}
