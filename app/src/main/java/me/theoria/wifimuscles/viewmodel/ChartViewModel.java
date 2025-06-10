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

import me.theoria.wifimuscles.model.WifiSignal;

import java.util.ArrayList;
import java.util.List;

public class ChartViewModel extends AndroidViewModel {
    private final MutableLiveData<List<WifiSignal>> rssiLiveData = new MutableLiveData<>();
    private final List<WifiSignal> signalList = new ArrayList<>();

    private final WifiManager wifiManager;
    private final Handler handler = new Handler(Looper.getMainLooper());

    private final Runnable updateTask = new Runnable() {
        @Override
        public void run() {
            WifiInfo info = wifiManager.getConnectionInfo();
            if (info != null) {
                int rssi = info.getRssi();
                long timestamp = System.currentTimeMillis();

                signalList.add(new WifiSignal(timestamp, rssi));
                if (signalList.size() > 30) signalList.remove(0);

                rssiLiveData.setValue(new ArrayList<>(signalList));
            }

            handler.postDelayed(this, 1000); // every 1s
        }
    };

    public ChartViewModel(@NonNull Application application) {
        super(application);
        wifiManager = (WifiManager) application.getSystemService(Application.WIFI_SERVICE);
        handler.post(updateTask);
    }

    public LiveData<List<WifiSignal>> getRssiLiveData() {
        return rssiLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        handler.removeCallbacks(updateTask);
    }
}