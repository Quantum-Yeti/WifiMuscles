package me.theoria.wifimuscles.viewmodel;

import android.Manifest;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import me.theoria.wifimuscles.data.model.WifiSignalModel;
import me.theoria.wifimuscles.utils.RSSIUtils;
import me.theoria.wifimuscles.utils.WifiStandardUtil;

public class WifiViewModel extends AndroidViewModel {

    private final MutableLiveData<List<WifiSignalModel>> rssiLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Integer> interferenceLevelLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> wifiStandardLiveData = new MutableLiveData<>();

    private final List<WifiSignalModel> signalList = new ArrayList<>();

    private final WifiManager wifiManager;
    private final Handler handler = new Handler(Looper.getMainLooper());

    public WifiViewModel(@NonNull Application application) {
        super(application);
        wifiManager = (WifiManager) application.getSystemService(Application.WIFI_SERVICE);
        if (wifiManager == null || !wifiManager.isWifiEnabled()) {
            errorLiveData.postValue("Please enable Wi-Fi and Location");
        }
    }

    private final Runnable fetchRssi = new Runnable() {
        @Override
        public void run() {
            if (ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                errorLiveData.postValue("Location permission required");
                handler.postDelayed(this, 2000);
                return;
            }

            boolean success = wifiManager.startScan();
            if (!success) {
                errorLiveData.postValue("Wi-Fi scan failed.");
            }

            handler.postDelayed(this, 4000); // adjust scan frequency
        }
    };

    private final BroadcastReceiver scanReceiver = new BroadcastReceiver() {
        @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(intent.getAction())) return;

            WifiInfo info = wifiManager.getConnectionInfo();
            if (info == null) return;

            String wifiStandard = WifiStandardUtil.getWifiStandardName(info);
            wifiStandardLiveData.postValue(wifiStandard);

            int currentFreq = info.getFrequency();
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            List<ScanResult> results = wifiManager.getScanResults();
            int interferenceCount = 0;

            for (ScanResult result : results) {
                if (result.frequency == currentFreq) continue;
                if (Math.abs(result.frequency - currentFreq) <= 20) {
                    interferenceCount++;
                }
            }

            // Create model with interference
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
                    wifiStandard,
                    interferenceCount
            );

            if (signalList.size() >= 30) signalList.remove(0);
            signalList.add(signal);

            rssiLiveData.postValue(new ArrayList<>(signalList));
            interferenceLevelLiveData.postValue(interferenceCount);
        }
    };

    public void startUpdates() {
        getApplication().registerReceiver(scanReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        fetchRssi.run();
    }

    public void stopUpdates() {
        handler.removeCallbacks(fetchRssi);
        try {
            getApplication().unregisterReceiver(scanReceiver);
        } catch (IllegalArgumentException ignored) {}
    }

    public LiveData<List<WifiSignalModel>> getRssiLiveData() {
        return rssiLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public LiveData<Integer> getInterferenceLevelLiveData() {
        return interferenceLevelLiveData;
    }

    public LiveData<String> getWifiStandardLiveData() {
        return wifiStandardLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        stopUpdates();
    }
}
