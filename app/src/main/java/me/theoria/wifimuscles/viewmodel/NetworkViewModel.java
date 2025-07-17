package me.theoria.wifimuscles.viewmodel;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import me.theoria.wifimuscles.data.model.NetworkModel;
import me.theoria.wifimuscles.utils.CalculationUtils;

public class NetworkViewModel extends AndroidViewModel {

    private final WifiManager wifiManager;
    private final MutableLiveData<NetworkModel> connectedNetworkLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final Handler handler = new Handler(Looper.getMainLooper());

    private final boolean isPasspointNetwork = false;
    private final boolean is80211mcResponder = false;

    private final Runnable networkFetcher = new Runnable() {
        @Override
        public void run() {
            fetchConnectedNetwork();
            handler.postDelayed(this, 3000); // Refresh every 3 seconds
        }
    };

    public NetworkViewModel(@NonNull Application application) {
        super(application);
        wifiManager = (WifiManager) application.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    public void fetchConnectedNetwork() {
        if (wifiManager == null || !wifiManager.isWifiEnabled()) {
            postError("Wi-Fi is disabled.");
            return;
        }

        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo == null || wifiInfo.getNetworkId() == -1 || wifiInfo.getBSSID() == null) {
            postError("No connected Wi-Fi network or BSSID unavailable.");
            return;
        }

        String connectedBSSID = wifiInfo.getBSSID();
        ScanResult matchedResult = getMatchingScanResult(connectedBSSID);

        NetworkModel model = new NetworkModel(
                System.currentTimeMillis(),
                wifiInfo.getSSID(),
                connectedBSSID,
                wifiInfo.getRssi(),
                WifiManager.calculateSignalLevel(wifiInfo.getRssi(), 5),
                wifiInfo.getFrequency(),
                matchedResult != null ? matchedResult.capabilities : "N/A",
                matchedResult != null ? matchedResult.channelWidth : -1,
                matchedResult != null ? matchedResult.centerFreq0 : -1,
                matchedResult != null ? matchedResult.centerFreq1 : -1,
                matchedResult != null && matchedResult.isPasspointNetwork(),
                matchedResult != null && matchedResult.is80211mcResponder(),
                CalculationUtils.calculateChannel(wifiInfo.getFrequency())
        );

        connectedNetworkLiveData.postValue(model);
    }

    private ScanResult getMatchingScanResult(String connectedBSSID) {
        if (ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            postError("Location permission is required to access scan results.");
            return null;
        }

        List<ScanResult> scanResults = wifiManager.getScanResults();
        if (scanResults != null) {
            for (ScanResult result : scanResults) {
                if (connectedBSSID.equalsIgnoreCase(result.BSSID)) {
                    return result;
                }
            }
        }
        return null;
    }

    private void postError(String message) {
        errorLiveData.postValue(message);
        connectedNetworkLiveData.postValue(null);
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
