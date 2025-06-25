package me.theoria.wifimuscles.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import me.theoria.wifimuscles.data.model.DHCPModel;
import me.theoria.wifimuscles.data.model.WifiSignalModel;
import me.theoria.wifimuscles.utils.FrequencyUtils;
import me.theoria.wifimuscles.utils.numToStringUtils;
import me.theoria.wifimuscles.utils.RSSIUtils;

public class DataUIViewModel extends ViewModel {

    // Signal Info
    private final MutableLiveData<String> rssiText = new MutableLiveData<>();
    private final MutableLiveData<Integer> rssiEmoji = new MutableLiveData<>();
    private final MutableLiveData<String> ipText = new MutableLiveData<>();
    private final MutableLiveData<String> frequencyText = new MutableLiveData<>();
    private final MutableLiveData<String> bandwidthText = new MutableLiveData<>();
    private final MutableLiveData<String> ssidText = new MutableLiveData<>();
    private final MutableLiveData<String> linkSpeed = new MutableLiveData<>();
    private final MutableLiveData<String> maxLinkSpeed = new MutableLiveData<String>();
    private final MutableLiveData<String> mac = new MutableLiveData<>();
    private final MutableLiveData<Integer> toastLevelEvent = new MutableLiveData<>();
    private final MutableLiveData<Integer> bssid = new MutableLiveData<>();

    // DHCP Info
    private final MutableLiveData<String> gatewayText = new MutableLiveData<>();
    private final MutableLiveData<String> netmaskText = new MutableLiveData<>();
    private final MutableLiveData<String> dns1Text = new MutableLiveData<>();
    private final MutableLiveData<String> dns2Text = new MutableLiveData<>();
    private final MutableLiveData<String> serverAddressText = new MutableLiveData<>();
    private final MutableLiveData<String> leaseDurationText = new MutableLiveData<>();


    private final numToStringUtils ipConverter = new numToStringUtils();

    // Signal getters
    public LiveData<String> getRssiText() { return rssiText; }
    public LiveData<Integer> getRssiEmoji() { return rssiEmoji; }
    public LiveData<String> getIpText() { return ipText; }
    public LiveData<String> getFrequencyText() { return frequencyText; }
    public LiveData<String> getBandwidthText() { return bandwidthText; }
    public LiveData<String> getSSIDText() { return ssidText; }
    public LiveData<String> getLinkSpeed() { return linkSpeed; }
    public LiveData<String> getMaxLinkSpeed() { return maxLinkSpeed; }
    public LiveData<String> getMac() { return mac; }
    public LiveData<Integer> getToastLevelEvent() { return toastLevelEvent; }
    public LiveData<Integer> getBssid() { return bssid; }

    // DHCP getters
    public LiveData<String> getGatewayText() { return gatewayText; }
    public LiveData<String> getNetMaskText() { return netmaskText; }
    public LiveData<String> getDns1Text() { return dns1Text; }
    public LiveData<String> getDns2Text() { return dns2Text; }
    public LiveData<String> getServerAddressText() { return serverAddressText; }
    public LiveData<String> getLeaseDurationText() { return leaseDurationText; }


    /**
     * Update the LiveData properties based on the latest signal model in the list.
     */
    public void updateSignalUI(List<WifiSignalModel> signals) {
        if (signals == null || signals.isEmpty()) return;
        WifiSignalModel latest = signals.get(signals.size() - 1);

        updateRssi(latest.getRssi());
        updateNetworkDetails(latest);
        updateFrequency(latest.getFrequency());
    }

    // --- Internal helpers ---
    private void updateRssi(int rssi) {
        rssiText.setValue("RSSI: " + rssi + " dBm");
        rssiEmoji.setValue(RSSIUtils.getRssiEmoji(rssi));

        int level = RSSIUtils.convertRssiToLevel(rssi);
        triggerToastLevelEvent(level);
    }

    public void triggerToastLevelEvent(int level) {
        toastLevelEvent.setValue(level);
    }

    private void updateNetworkDetails(WifiSignalModel signal) {
        ipText.setValue(numToStringUtils.intIPToString(signal.getIP()));  // returns String now
        ssidText.setValue("SSID: " + signal.getSSIDText());
        mac.setValue("MAC: " + signal.getMac());
        linkSpeed.setValue("Link Speed: " + signal.getLinkSpeed() + " Mbps");
        maxLinkSpeed.setValue("Avg Max Speed: " + signal.getMaxLinkSpeed() + " Mbps");
    }

    private void updateFrequency(int frequency) {
        frequencyText.setValue(frequency + " MHz");
        bandwidthText.setValue(FrequencyUtils.fqToGhz(frequency));
    }

    public void updateFromDhcpModel(DHCPModel model) {
        if (model == null) return;
        gatewayText.setValue("Gateway: " + numToStringUtils.intIPToString(model.getGateway()));
        netmaskText.setValue("Netmask: " + numToStringUtils.intIPToString(model.getNetmask()));
        dns1Text.setValue("DNS 1: " + numToStringUtils.intIPToString(model.getDns1()));
        dns2Text.setValue("DNS 2: " + numToStringUtils.intIPToString(model.getDns2()));
        serverAddressText.setValue("DHCP Server: " + numToStringUtils.intIPToString(model.getServerAddress()));
        leaseDurationText.setValue("Lease Duration: " + model.getLeaseDuration() + " sec");
    }
}
