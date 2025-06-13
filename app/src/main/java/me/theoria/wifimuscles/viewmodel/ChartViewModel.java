package me.theoria.wifimuscles.viewmodel;


import android.app.Application;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import me.theoria.wifimuscles.R;
import me.theoria.wifimuscles.model.WifiSignalModel;
import me.theoria.wifimuscles.utils.RssiUtils;

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
                int signalLevel = RssiUtils.mapRssiToLevels(rssi);
                long timestamp = System.currentTimeMillis();

                // Get current connected SSID name
                String currentSSID = info.getSSID();
                // SSID may be enclosed in double quotes on Android 8.0 and above
                if (currentSSID != null && currentSSID.startsWith("\"") && currentSSID.endsWith("\"")) {
                    currentSSID = currentSSID.substring(1, currentSSID.length() - 1);  // Remove quotes
                }

                signalList.add(new WifiSignalModel(timestamp, rssi, signalLevel, currentSSID));
                if (signalList.size() > 30) signalList.remove(0);
                //int count = Math.min(6, signalList.size()); // Limit to 6 entries for readability
                //int start = signalList.size() - count;

                rssiLiveData.setValue(new ArrayList<>(signalList));
            }
            handler.postDelayed(this, 1000); // every x second(s)
        }

    };

    public ChartViewModel(@NonNull Application application) {
        super(application);
        wifiManager = (WifiManager) application.getSystemService(Application.WIFI_SERVICE);

        if (wifiManager != null && wifiManager.isWifiEnabled()) {
            handler.post(fetchRssi);
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