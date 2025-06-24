package me.theoria.wifimuscles.viewmodel;

import android.app.Application;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import me.theoria.wifimuscles.data.model.NetworkModel;

public class NetworkViewModel extends AndroidViewModel {

    private final WifiManager wifiManager;
    private final MutableLiveData<NetworkModel> connectedNetworkLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final Handler handler = new Handler(Looper.getMainLooper());

    private final Runnable networkFetcher = new Runnable() {
        @Override
        public void run() {
            fetchConnectedNetwork();
            handler.postDelayed(this, 3000); // refresh every 3 seconds
        }
    };

    public NetworkViewModel(@NonNull Application application) {
        super(application);
        wifiManager = (WifiManager) application.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    public void fetchConnectedNetwork() {
        if (wifiManager == null || !wifiManager.isWifiEnabled()) {
            errorLiveData.postValue("Wi-Fi is disabled.");
            connectedNetworkLiveData.postValue(null);
            return;
        }

        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo == null || wifiInfo.getNetworkId() == -1 || wifiInfo.getBSSID() == null) {
            errorLiveData.postValue("No connected Wi-Fi network or BSSID unavailable.");
            connectedNetworkLiveData.postValue(null);
            return;
        }

        String connectedBSSID = wifiInfo.getBSSID();
        ScanResult matchedResult = null;
        List<ScanResult> scanResults = wifiManager.getScanResults();

        for (ScanResult result : scanResults) {
            if (connectedBSSID.equalsIgnoreCase(result.BSSID)) {
                matchedResult = result;
                break;
            }
        }

        String capabilities = matchedResult != null ? matchedResult.capabilities : "N/A";
        int channelWidth = matchedResult != null ? matchedResult.channelWidth : -1;
        int centerFreq0 = matchedResult != null ? matchedResult.centerFreq0 : -1;
        int centerFreq1 = matchedResult != null ? matchedResult.centerFreq1 : -1;
        boolean isPasspoint = matchedResult != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && matchedResult.isPasspointNetwork();
        boolean is80211mcResponder = matchedResult != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && matchedResult.is80211mcResponder();

        NetworkModel model = new NetworkModel(
                System.currentTimeMillis(),
                wifiInfo.getSSID(),
                wifiInfo.getBSSID(),
                wifiInfo.getRssi(),
                WifiManager.calculateSignalLevel(wifiInfo.getRssi(), 5),
                wifiInfo.getFrequency(),
                capabilities,
                channelWidth,
                centerFreq0,
                centerFreq1,
                isPasspoint,
                is80211mcResponder
        );

        connectedNetworkLiveData.postValue(model);
    }

    public void startAutoUpdate() {
        networkFetcher.run();
    }

    public void stopAutoUpdate() {
        handler.removeCallbacks(networkFetcher);
    }

    public LiveData<NetworkModel> getConnectedNetworkLiveData() {
        return connectedNetworkLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        stopAutoUpdate();
    }
}
