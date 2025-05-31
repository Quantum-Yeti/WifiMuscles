package me.theoria.wifimuscles.viewmodel;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import me.theoria.wifimuscles.model.WiFiRepository;

public class WiFiViewModel extends ViewModel {
    private final MutableLiveData<Integer> rssiLiveData = new MutableLiveData<>();
    private final Handler handler = new Handler(Looper.getMainLooper());
    private WiFiRepository wifiRepository;

    public LiveData<Integer> getRssiLiveData() {
        return rssiLiveData;
    }

    public void initRepository(Context context) {
        wifiRepository = new WiFiRepository(context);
        startRSSIUpdate();
    }

    private void startRSSIUpdate() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (wifiRepository != null) {
                    rssiLiveData.setValue(wifiRepository.getCurrentRssi());
                }
                handler.postDelayed(this, 1000); // Update every 1 second
            }
        }, 0);
    }
}
