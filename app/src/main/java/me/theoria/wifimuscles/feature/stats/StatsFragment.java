package me.theoria.wifimuscles.feature.stats;

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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import me.theoria.wifimuscles.R;
import me.theoria.wifimuscles.model.StatsInfoCardModel;
import me.theoria.wifimuscles.core.utils.CalculationUtils;
import me.theoria.wifimuscles.core.adapters.StatsAdapter;
import me.theoria.wifimuscles.core.viewmodel.ConnectivityViewModel;
import me.theoria.wifimuscles.core.viewmodel.DHCPViewModel;
import me.theoria.wifimuscles.core.viewmodel.DataUIViewModel;
import me.theoria.wifimuscles.core.viewmodel.NetworkViewModel;
import me.theoria.wifimuscles.core.viewmodel.WifiViewModel;

public class StatsFragment extends Fragment {

    // Stats Adapter and Manager
    private StatsAdapter statsAdapter;
    private StatsPopupManager statsPopupManager;

    // View Models
    private DataUIViewModel dataUIViewModel;
    private WifiViewModel wifiViewModel;
    private NetworkViewModel networkViewModel;
    private ConnectivityViewModel connectivityViewModel;
    private DHCPViewModel dhcpViewModel;

    // Google AdMob
    private InterstitialAd mInterstitialAd;
    private boolean adLoaded = false;

    // Array list for stats info cards
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

        // Build the google admob interstitial popup
        // Set for once every 10 minutes per user
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(
                requireContext(),
                getString(R.string.admob_interstitial_id),
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                        adLoaded = true;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                        mInterstitialAd = null;
                        adLoaded = false;
                    }
        });

        return root;
    }

    /**
     * Method to observe the live data from the ViewModels.
     */
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

        wifiViewModel.getWifiStandardLiveData().observe(getViewLifecycleOwner(), standard -> updateStatsList());

        wifiViewModel.getInterferenceLevelLiveData().observe(getViewLifecycleOwner(), interference -> updateStatsList());
        wifiViewModel.getInterferencePercentLiveData().observe(getViewLifecycleOwner(), interferencePercent -> updateStatsList());

        networkViewModel.getConnectedNetworkLiveData().observe(getViewLifecycleOwner(), network -> updateStatsList());

        connectivityViewModel.getConnectivityStatus().observe(getViewLifecycleOwner(), model -> updateStatsList());
    }

    /**
     * Method to update the RecyclerView with fresh Wi-Fi statistics.
     */
    private void updateStatsList() {
        statsItems.clear();

        // --- Wi-Fi Section ---
        String rssi = dataUIViewModel.getRssiText().getValue();
        if (rssi == null) rssi = getString(R.string.no_data);
        statsItems.add(new StatsInfoCardModel("Wi-Fi", getString(R.string.rssi), rssi, v -> statsPopupManager.wifiLevelPopup(v)));

        String standard = wifiViewModel.getWifiStandardLiveData().getValue();
        if (standard == null) standard = getString(R.string.no_data);
        statsItems.add(new StatsInfoCardModel("Wi-Fi", getString(R.string.wifi_standard), standard, v -> statsPopupManager.standardPopup(v)));

        Float interferencePercent = wifiViewModel.getInterferencePercentLiveData().getValue();
        String interferencePercentText = (interferencePercent != null)
                ? String.format(Locale.getDefault(), "%.1f%%", interferencePercent)
                : getString(R.string.no_data);
        statsItems.add(new StatsInfoCardModel("Wi-Fi", getString(R.string.interference), interferencePercentText, v -> statsPopupManager.interferencePopup(v)));

        // --- Network Section ---
        var nm = networkViewModel.getConnectedNetworkLiveData().getValue();
        if (nm != null) {
            statsItems.add(new StatsInfoCardModel("Network", getString(R.string.capability), nm.getCapabilities(), v -> statsPopupManager.capabilitiesPopup(v)));
            statsItems.add(new StatsInfoCardModel("Network", getString(R.string.channel_width), String.valueOf(nm.getChannelWidth()), v -> statsPopupManager.channelWidthPopup(v)));
            statsItems.add(new StatsInfoCardModel("Network", getString(R.string.center_freq_0), String.valueOf(nm.getCenterFreq0()), v -> statsPopupManager.centerFreq0Popup(v)));
            statsItems.add(new StatsInfoCardModel("Network", getString(R.string.center_freq_1), String.valueOf(nm.getCenterFreq1()), v -> statsPopupManager.centerFreq1Popup(v)));
            statsItems.add(new StatsInfoCardModel("Network", getString(R.string.passpoint), yesNo(nm.getPassPoint()), v -> statsPopupManager.passpointPopup(v)));
            statsItems.add(new StatsInfoCardModel("Network", getString(R.string.responder), yesNo(nm.getIs80211mcResponder()), v -> statsPopupManager.responderPopup(v)));
            statsItems.add(new StatsInfoCardModel("Network", getString(R.string.channel_number), String.valueOf(nm.getChannelNumber()), null));
        }

        // --- DHCP Section ---
        var dhcp = dhcpViewModel.getDhcpModelLiveData().getValue();
        if (dhcp != null) {
            statsItems.add(new StatsInfoCardModel("DHCP", getString(R.string.gateway), dataUIViewModel.getGatewayText().getValue(), v -> statsPopupManager.gatewayPopup(v)));
            statsItems.add(new StatsInfoCardModel("DHCP", getString(R.string.netmask), dataUIViewModel.getNetmaskText().getValue(), v -> statsPopupManager.netmaskPopup(v)));
            statsItems.add(new StatsInfoCardModel("DHCP", getString(R.string.dns1), dataUIViewModel.getDns1Text().getValue(), v -> statsPopupManager.dns1Popup(v)));
            statsItems.add(new StatsInfoCardModel("DHCP", getString(R.string.dns2), dataUIViewModel.getDns2Text().getValue(), v -> statsPopupManager.dns2Popup(v)));
            statsItems.add(new StatsInfoCardModel("DHCP", getString(R.string.lease_duration), dataUIViewModel.getLeaseDurationText().getValue(), v -> statsPopupManager.leasePopup(v)));
            statsItems.add(new StatsInfoCardModel("DHCP", getString(R.string.link_speed_name), dataUIViewModel.getLinkSpeed().getValue(), v -> statsPopupManager.linkSpeedPopup(v)));
            statsItems.add(new StatsInfoCardModel("DHCP", getString(R.string.max_link_speed_name), dataUIViewModel.getMaxLinkSpeed().getValue(), v -> statsPopupManager.maxLinkSpeedPopup(v)));
        }

        // --- Connectivity Section ---
        var cm = connectivityViewModel.getConnectivityStatus().getValue();
        if (cm != null) {
            statsItems.add(new StatsInfoCardModel("Connectivity", getString(R.string.transport), cm.getTransportType().name(), v -> statsPopupManager.transportPopup(v)));
            statsItems.add(new StatsInfoCardModel("Connectivity", getString(R.string.internet), yesNo(cm.hasInternet()), v -> statsPopupManager.internetPopup(v)));
            statsItems.add(new StatsInfoCardModel("Connectivity", getString(R.string.validation), yesNo(cm.isValidated()), v -> statsPopupManager.validationPopup(v)));
            statsItems.add(new StatsInfoCardModel("Connectivity", getString(R.string.vpn), yesNo(cm.getIsVpn()), v -> statsPopupManager.vpnPopup(v)));
            statsItems.add(new StatsInfoCardModel("Connectivity", getString(R.string.metered), yesNo(cm.isMetered()), v -> statsPopupManager.meteredPopup(v)));
            statsItems.add(new StatsInfoCardModel("Connectivity", getString(R.string.downstream), CalculationUtils.speedConvert(cm.getDownstreamKbps()), v -> statsPopupManager.downstreamPopup(v)));
            statsItems.add(new StatsInfoCardModel("Connectivity", getString(R.string.upstream), CalculationUtils.speedConvert(cm.getUpstreamKbps()), v -> statsPopupManager.upstreamPopup(v)));
        }

        if (adLoaded && mInterstitialAd != null) {
            mInterstitialAd.show(requireActivity());
            adLoaded = false;
        }

        statsAdapter.updateItems(statsItems);
    }

    /**
     * Utility boolean method to return yes/no for the statistics in the Connectivity ViewModel.
     */
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
