package me.theoria.wifimuscles.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import me.theoria.wifimuscles.R;
import me.theoria.wifimuscles.data.managers.info.StatsPopupManager;
import me.theoria.wifimuscles.data.model.StatsInfoCardModel;
import me.theoria.wifimuscles.utils.CalculationUtils;
import me.theoria.wifimuscles.view.adapters.StatsAdapter;
import me.theoria.wifimuscles.viewmodel.ConnectivityViewModel;
import me.theoria.wifimuscles.viewmodel.DHCPViewModel;
import me.theoria.wifimuscles.viewmodel.DataUIViewModel;
import me.theoria.wifimuscles.viewmodel.NetworkViewModel;
import me.theoria.wifimuscles.viewmodel.WifiViewModel;

public class StatsFragment extends Fragment {

    private StatsAdapter statsAdapter;
    private StatsPopupManager statsPopupManager;

    private DataUIViewModel dataUIViewModel;
    private WifiViewModel wifiViewModel;
    private NetworkViewModel networkViewModel;
    private ConnectivityViewModel connectivityViewModel;
    private DHCPViewModel dhcpViewModel;

    private final List<StatsInfoCardModel> statsItems = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_stats, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.statsRecyclerView);
        statsAdapter = new StatsAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(statsAdapter);

        statsPopupManager = new StatsPopupManager(requireContext());

        ViewModelProvider provider = new ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())
        );

        wifiViewModel = provider.get(WifiViewModel.class);
        networkViewModel = provider.get(NetworkViewModel.class);
        connectivityViewModel = provider.get(ConnectivityViewModel.class);
        dhcpViewModel = provider.get(DHCPViewModel.class);
        dataUIViewModel = provider.get(DataUIViewModel.class);

        observeLiveData();

        return root;
    }

    private void observeLiveData() {
        dhcpViewModel.getDhcpModelLiveData().observe(getViewLifecycleOwner(), dhcpModel -> {
            if (dhcpModel != null) {
                dataUIViewModel.updateFromDhcpModel(dhcpModel);
                updateStatsList();
            }
        });

        wifiViewModel.getRssiLiveData().observe(getViewLifecycleOwner(), signals -> {
            if (signals != null && !signals.isEmpty()) {
                dataUIViewModel.updateSignalUI(signals);
                updateStatsList();
            }
        });

        wifiViewModel.getInterferenceLevelLiveData().observe(getViewLifecycleOwner(), interference -> updateStatsList());

        networkViewModel.getConnectedNetworkLiveData().observe(getViewLifecycleOwner(), network -> updateStatsList());

        connectivityViewModel.getConnectivityStatus().observe(getViewLifecycleOwner(), model -> updateStatsList());
    }

    private void updateStatsList() {
        statsItems.clear();

        // Wi-Fi Signal Section
        String rssi = dataUIViewModel.getRssiText().getValue();
        if (rssi == null) rssi = getString(R.string.no_data);
        statsItems.add(new StatsInfoCardModel("Signal Strength", rssi, v -> statsPopupManager.wifiLevelPopup(v)));

        String interference = String.valueOf(wifiViewModel.getInterferenceLevelLiveData().getValue());
        statsItems.add(new StatsInfoCardModel("Interference", interference, v -> statsPopupManager.interferencePopup(v)));

        var network = networkViewModel.getConnectedNetworkLiveData().getValue();
        if (network != null) {
            statsItems.add(new StatsInfoCardModel("Capabilities", network.getCapabilities(), v -> statsPopupManager.capabilitiesPopup(v)));
            statsItems.add(new StatsInfoCardModel("Channel Width", String.valueOf(network.getChannelWidth()), v -> statsPopupManager.channelWidthPopup(v)));
            statsItems.add(new StatsInfoCardModel("Center Freq 0", String.valueOf(network.getCenterFreq0()), v -> statsPopupManager.centerFreq0Popup(v)));
            statsItems.add(new StatsInfoCardModel("Center Freq 1", String.valueOf(network.getCenterFreq1()), v -> statsPopupManager.centerFreq1Popup(v)));
            statsItems.add(new StatsInfoCardModel("Passpoint", yesNo(network.getPassPoint()), v -> statsPopupManager.passpointPopup(v)));
            statsItems.add(new StatsInfoCardModel("Responder", yesNo(network.getIs80211mcResponder()), v -> statsPopupManager.responderPopup(v)));
            statsItems.add(new StatsInfoCardModel("Channel Number", String.valueOf(network.getChannelNumber()), null));
        }

        // DHCP Section
        statsItems.add(new StatsInfoCardModel("Gateway", dataUIViewModel.getGatewayText().getValue(), v -> statsPopupManager.gatewayPopup(v)));
        statsItems.add(new StatsInfoCardModel("Netmask", dataUIViewModel.getNetmaskText().getValue(), v -> statsPopupManager.netmaskPopup(v)));
        statsItems.add(new StatsInfoCardModel("DNS 1", dataUIViewModel.getDns1Text().getValue(), v -> statsPopupManager.dns1Popup(v)));
        statsItems.add(new StatsInfoCardModel("DNS 2", dataUIViewModel.getDns2Text().getValue(), v -> statsPopupManager.dns2Popup(v)));
        statsItems.add(new StatsInfoCardModel("Lease Duration", dataUIViewModel.getLeaseDurationText().getValue(), v -> statsPopupManager.leasePopup(v)));

        // Connectivity Section
        var model = connectivityViewModel.getConnectivityStatus().getValue();
        if (model != null) {
            statsItems.add(new StatsInfoCardModel("Transport", model.getTransportType().name(), v -> statsPopupManager.transportPopup(v)));
            statsItems.add(new StatsInfoCardModel("Internet", yesNo(model.hasInternet()), v -> statsPopupManager.internetPopup(v)));
            statsItems.add(new StatsInfoCardModel("Validated", yesNo(model.isValidated()), v -> statsPopupManager.validationPopup(v)));
            statsItems.add(new StatsInfoCardModel("Metered", yesNo(model.isMetered()), v -> statsPopupManager.meteredPopup(v)));
            statsItems.add(new StatsInfoCardModel("Downstream", CalculationUtils.speedConvert(model.getDownstreamKbps()), v -> statsPopupManager.downstreamPopup(v)));
            statsItems.add(new StatsInfoCardModel("Upstream", CalculationUtils.speedConvert(model.getUpstreamKbps()), v -> statsPopupManager.upstreamPopup(v)));
        }

        statsAdapter.updateItems(statsItems);
    }

    private String yesNo(boolean value) {
        return value ? "Yes" : "No";
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
        statsItems.clear();
    }
}
