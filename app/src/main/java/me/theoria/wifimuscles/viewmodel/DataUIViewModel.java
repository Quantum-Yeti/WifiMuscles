package me.theoria.wifimuscles.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import me.theoria.wifimuscles.data.model.DHCPModel;
import me.theoria.wifimuscles.data.model.WifiSignalModel;
import me.theoria.wifimuscles.utils.CalculationUtils;
import me.theoria.wifimuscles.utils.ExternalUtil;
import me.theoria.wifimuscles.utils.RSSIUtils;

public class DataUIViewModel extends ViewModel {

    // Nested class to hold signal-related LiveData
    public static class SignalInfo {
        public final MutableLiveData<String> rssiText = new MutableLiveData<>();
        public final MutableLiveData<Integer> rssiEmoji = new MutableLiveData<>();
        public final MutableLiveData<String> ipText = new MutableLiveData<>();
        public final MutableLiveData<String> frequencyText = new MutableLiveData<>();
        public final MutableLiveData<String> bandwidthText = new MutableLiveData<>();
        public final MutableLiveData<String> ssidText = new MutableLiveData<>();
        public final MutableLiveData<String> linkSpeed = new MutableLiveData<>();
        public final MutableLiveData<String> maxLinkSpeed = new MutableLiveData<>();
        public final MutableLiveData<String> bssidText = new MutableLiveData<>();
        public final MutableLiveData<Integer> toastLevelEvent = new MutableLiveData<>();
        public final MutableLiveData<String> pingResult = new MutableLiveData<>();
    }

    // Nested class to hold DHCP-related LiveData
    public static class DhcpInfo {
        public final MutableLiveData<String> gatewayText = new MutableLiveData<>();
        public final MutableLiveData<String> netmaskText = new MutableLiveData<>();
        public final MutableLiveData<String> dns1Text = new MutableLiveData<>();
        public final MutableLiveData<String> dns2Text = new MutableLiveData<>();
        public final MutableLiveData<String> serverAddressText = new MutableLiveData<>();
        public final MutableLiveData<String> leaseDurationText = new MutableLiveData<>();
    }

    private final SignalInfo signalInfo = new SignalInfo();
    private final DhcpInfo dhcpInfo = new DhcpInfo();

    // --- Signal getters ---
    public LiveData<String> getRssiText() { return signalInfo.rssiText; }
    public LiveData<Integer> getRssiEmoji() { return signalInfo.rssiEmoji; }
    public LiveData<String> getIpText() { return signalInfo.ipText; }
    public LiveData<String> getFrequencyText() { return signalInfo.frequencyText; }
    public LiveData<String> getBandwidthText() { return signalInfo.bandwidthText; }
    public LiveData<String> getSSIDText() { return signalInfo.ssidText; }
    public LiveData<String> getLinkSpeed() { return signalInfo.linkSpeed; }
    public LiveData<String> getMaxLinkSpeed() { return signalInfo.maxLinkSpeed; }
    public LiveData<String> getBssidText() { return signalInfo.bssidText; }
    public LiveData<Integer> getToastLevelEvent() { return signalInfo.toastLevelEvent; }
    public LiveData<String> getPingResult() { return signalInfo.pingResult; }

    // --- DHCP getters ---
    public LiveData<String> getGatewayText() { return dhcpInfo.gatewayText; }
    public LiveData<String> getNetmaskText() { return dhcpInfo.netmaskText; }
    public LiveData<String> getDns1Text() { return dhcpInfo.dns1Text; }
    public LiveData<String> getDns2Text() { return dhcpInfo.dns2Text; }
    //public LiveData<String> getServerAddressText() { return dhcpInfo.serverAddressText; }
    public LiveData<String> getLeaseDurationText() { return dhcpInfo.leaseDurationText; }


    /**
     * Update the UI based on the latest WifiSignalModel list.
     */
    public void updateSignalUI(List<WifiSignalModel> signals) {
        if (signals == null || signals.isEmpty()) return;

        WifiSignalModel latest = signals.get(signals.size() - 1);
        updateRssi(latest.getRssi());
        updateNetworkDetails(latest);
        updateFrequency(latest.getFrequency());
    }

    private void updateRssi(int rssi) {
        signalInfo.rssiText.setValue(rssi + " dBm");
        signalInfo.rssiEmoji.setValue(RSSIUtils.getRssiEmoji(rssi));
        signalInfo.toastLevelEvent.setValue(CalculationUtils.convertRssiToLevel(rssi));
    }

    private void updateNetworkDetails(WifiSignalModel signal) {
        signalInfo.ipText.setValue(CalculationUtils.intIPToString(signal.getIP()));
        signalInfo.ssidText.setValue(signal.getSSIDText());
        signalInfo.bssidText.setValue(signal.getBssid());
        signalInfo.linkSpeed.setValue(signal.getLinkSpeed() + " Mbps");
        signalInfo.maxLinkSpeed.setValue(signal.getMaxLinkSpeed() + " Mbps");
    }

    private void updateFrequency(int frequency) {
        signalInfo.frequencyText.setValue(frequency + " MHz");
        signalInfo.bandwidthText.setValue(CalculationUtils.fqToGhz(frequency));
    }

    public void updateFromDhcpModel(DHCPModel model) {
        if (model == null) return;

        dhcpInfo.gatewayText.setValue(CalculationUtils.intIPToString(model.getGateway()));
        dhcpInfo.netmaskText.setValue(CalculationUtils.intIPToString(model.getNetmask()));
        dhcpInfo.dns1Text.setValue(CalculationUtils.intIPToString(model.getDns1()));
        dhcpInfo.dns2Text.setValue(CalculationUtils.intIPToString(model.getDns2()));
        dhcpInfo.serverAddressText.setValue(CalculationUtils.intIPToString(model.getServerAddress()));

        String formatLease = CalculationUtils.formatLeaseDuration(model.getLeaseDuration());
        dhcpInfo.leaseDurationText.setValue(formatLease);
    }

    public void runPingTest(String ip) {
        ExternalUtil.ping(ip, signalInfo.pingResult::postValue);
    }
}
