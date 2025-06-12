package me.theoria.wifimuscles.viewmodel;


import android.app.Application;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import me.theoria.wifimuscles.model.WifiSignalModel;

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
                int signalLevel = mapRssiToLevels(rssi);
                long timestamp = System.currentTimeMillis();

                signalList.add(new WifiSignalModel(timestamp, rssi, signalLevel));
                //if (signalList.size() > 30) signalList.remove(0);
                int count = Math.min(6, signalList.size()); // Limit to 6 entries for readability
                int start = signalList.size() - count;

                rssiLiveData.setValue(new ArrayList<>(signalList));
            }
            handler.postDelayed(this, 1000); // every x second(s)
        }
    };

    public ChartViewModel(@NonNull Application application) {
        super(application);
        wifiManager = (WifiManager) application.getSystemService(Application.WIFI_SERVICE);
        handler.post(fetchRssi);
    }

    public LiveData<List<WifiSignalModel>> getRssiLiveData() {
        return rssiLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        handler.removeCallbacks(fetchRssi);
    }

    public static int mapRssiToLevels(int rssi) {
        if (rssi >=-50) return 5;
        else if (rssi >= -70) return 4;
        else if (rssi >= -80) return 3;
        else if (rssi >= -90) return 2;
        else return 1;
    }

}