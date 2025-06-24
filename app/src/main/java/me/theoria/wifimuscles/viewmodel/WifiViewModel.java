package me.theoria.wifimuscles.viewmodel;


import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import me.theoria.wifimuscles.data.model.WifiSignalModel;
import me.theoria.wifimuscles.utils.RSSIUtils;

import java.util.ArrayList;
import java.util.List;

public class WifiViewModel extends AndroidViewModel {

    private final MutableLiveData<List<WifiSignalModel>> rssiLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final List<WifiSignalModel> signalList = new ArrayList<>();

    private final WifiManager wifiManager;
    private final Handler handler = new Handler(Looper.getMainLooper());

    private final Runnable fetchRssi = new Runnable() {
        @Override
        public void run() {
            WifiInfo info = wifiManager.getConnectionInfo();
            if (info != null) {
                String ssid = info.getSSID();
                if (ssid == null || ssid.equals("<unknown ssid>")) {
                    errorLiveData.postValue("Enable Location to access SSID");
                    handler.postDelayed(this, 2000);
                    return;
                }

                WifiSignalModel signal = new WifiSignalModel(
                        System.currentTimeMillis(),
                        info.getRssi(),
                        RSSIUtils.returnRssiSignal(info.getRssi()),
                        info.getFrequency(),
                        info.getIpAddress(),
                        info.getNetworkId(),
                        info.getSSID(),
                        info.getBSSID(),
                        info.getLinkSpeed(),
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.R ?
                                info.getMaxSupportedRxLinkSpeedMbps() :
                                info.getLinkSpeed(),
                        info.getBSSID()
                );

                if (signalList.size() >= 30) {
                    signalList.remove(0);
                }

                signalList.add(signal);
                rssiLiveData.postValue(new ArrayList<>(signalList));
            }
            handler.postDelayed(this, 2000);
        }
    };

    public WifiViewModel(@NonNull Application application) {
        super(application);
        wifiManager = (WifiManager) application.getSystemService(Application.WIFI_SERVICE);
        if (wifiManager == null || !wifiManager.isWifiEnabled()) {
            errorLiveData.postValue("Please enable Wi-Fi and Location");
        }
    }

    public void startUpdates() {
        fetchRssi.run();
        //handler.postDelayed(fetchRssi, 2000);
    }

    public void stopUpdates() {
        handler.removeCallbacks(fetchRssi);
    }

    public LiveData<List<WifiSignalModel>> getRssiLiveData() {
        return rssiLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        stopUpdates();
    }
}

