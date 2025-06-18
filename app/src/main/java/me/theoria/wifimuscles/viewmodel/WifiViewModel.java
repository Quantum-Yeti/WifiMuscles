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

import me.theoria.wifimuscles.data.model.WifiSignalModel;
import me.theoria.wifimuscles.utils.RSSIUtils;

import java.util.ArrayList;
import java.util.List;

public class WifiViewModel extends AndroidViewModel {

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
                int frequency = info.getFrequency();
                int ip = info.getIpAddress();
                String bssid = info.getBSSID();
                int networkID = info.getNetworkId();
                //String macAddress = info.getMacAddress();

                signalList.add(new WifiSignalModel(
                        timestamp,
                        rssi,
                        signalLevel,
                        frequency,
                        ip,
                        networkID));
                if (signalList.size() > 30) {
                    signalList.remove(0);
                }

                rssiLiveData.setValue(new ArrayList<>(signalList));
            }
            handler.postDelayed(this, 2000); // ms to s
        }
    };

    public WifiViewModel(@NonNull Application application) {
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