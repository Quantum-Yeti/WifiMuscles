package me.theoria.wifimuscles.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import me.theoria.wifimuscles.data.model.WifiSignalModel;
import me.theoria.wifimuscles.utils.RSSIUtils;

import java.util.List;

public class DataUIViewModel extends ViewModel {

    private final MutableLiveData<String> rssiText = new MutableLiveData<>();
    private final MutableLiveData<Integer> rssiEmoji = new MutableLiveData<>();
    private final MutableLiveData<String> ipText = new MutableLiveData<>();
    private final MutableLiveData<String> frequencyText = new MutableLiveData<>();
    private final MutableLiveData<String> bandwidthText = new MutableLiveData<>();

    // Expose LiveData to the UI
    public LiveData<String> getRssiText() {
        return rssiText;
    }

    public LiveData<Integer> getRssiEmoji() {
        return rssiEmoji;
    }

    public LiveData<String> getIpText() {
        return ipText;
    }

    public LiveData<String> getFrequencyText() {
        return frequencyText;
    }

    public LiveData<String> getBandwidthText() {
        return bandwidthText;
    }

    // Update the LiveData based on the signal data
    public void updateSignalUI(List<WifiSignalModel> signals) {
        if (signals.isEmpty()) return;

        WifiSignalModel latestSignal = signals.get(signals.size() - 1);
        int rssi = latestSignal.getRssi();
        int ip = latestSignal.getIP();
        int frequency = latestSignal.getFrequency();

        // Update LiveData
        rssiText.setValue("RSSI: " + rssi + " dBm");
        rssiEmoji.setValue(RSSIUtils.getRssiEmoji(rssi));
        ipText.setValue("IP: " + intToIp(ip));

        String frequencyTextValue = frequency + " MHz";
        frequencyText.setValue(frequencyTextValue);

        double frequencyInGHz = frequency / 1000.0;
        String frequencyGHzText = (int) frequencyInGHz + " GHz";
        bandwidthText.setValue(frequencyGHzText);
    }

    private String intToIp(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                ((ip >> 24) & 0xFF);
    }
}
