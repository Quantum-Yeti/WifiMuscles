package me.theoria.wifimuscles.viewmodel;


import android.app.Application;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import me.theoria.wifimuscles.model.WifiSignalModel;
import me.theoria.wifimuscles.model.utils.RSSIUtils;

import java.util.ArrayList;
import java.util.List;

public class ChartViewModel extends AndroidViewModel {

    private final MutableLiveData<List<WifiSignalModel>> rssiLiveData = new MutableLiveData<>();
    private final List<WifiSignalModel> signalList = new ArrayList<>();
    private final WifiManager wifiManager;
    private final Handler handler = new Handler(Looper.getMainLooper());

    private final Runnable fetchRssi = new Runnable() {
        @Override
        public void run() {
            WifiInfo info = wifiManager.getConnectionInfo();
            if (info != null) {
                int rssi = info.getRssi();
                int signalLevel = RSSIUtils.mapRssiToLevels(rssi);
                long timestamp = System.currentTimeMillis();

                // Get current connected SSID name
                String currentSSID = info.getSSID();
                if (currentSSID != null && currentSSID.startsWith("\"") && currentSSID.endsWith("\"")) {
                    currentSSID = currentSSID.substring(1, currentSSID.length() - 1);  // Remove quotes
                }

                assert currentSSID != null;
                signalList.add(new WifiSignalModel(timestamp, rssi, signalLevel, currentSSID));
                if (signalList.size() > 30) {
                    signalList.remove(0);
                }

                rssiLiveData.setValue(new ArrayList<>(signalList));
            }
            handler.postDelayed(this, 5000); // ms to s
        }
    };

    public ChartViewModel(@NonNull Application application) {
        super(application);
        wifiManager = (WifiManager) application.getSystemService(Application.WIFI_SERVICE);

        if (wifiManager != null && wifiManager.isWifiEnabled()) {
            handler.post(fetchRssi);
            wifiManager.getConnectionInfo().getSSID();
        } else {
            Toast.makeText(application.getApplicationContext(), "Please enable Wi-Fi", Toast.LENGTH_SHORT).show();
        }
    }

    public LiveData<List<WifiSignalModel>> getRssiLiveData() {
        return rssiLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        handler.removeCallbacks(fetchRssi);
    }
}