package me.theoria.wifimuscles.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.*;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class WifiScanViewModel extends AndroidViewModel {

    private static final String TAG = "WifiScanViewModel";

    private final WifiManager wifiManager;

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<List<ScanResult>> scanResults = new MutableLiveData<>();
    private final MutableLiveData<String> scanError = new MutableLiveData<>();

    private boolean isReceiverRegistered = false;

    private final BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @SuppressLint("MissingPermission") // Permissions already checked in Fragment
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false);
            if (success) {
                scanResults.setValue(wifiManager.getScanResults());
            } else {
                scanError.setValue("Wi-Fi Scan Failed");
            }
            isLoading.setValue(false);

            // Unregister receiver after receiving results to avoid leaks & duplicates
            try {
                if (isReceiverRegistered) {
                    context.unregisterReceiver(this);
                    isReceiverRegistered = false;
                }
            } catch (IllegalArgumentException e) {
                Log.w(TAG, "Receiver already unregistered or not registered: " + e.getMessage());
            }
        }
    };

    public WifiScanViewModel(@NonNull Application application) {
        super(application);
        wifiManager = (WifiManager) application.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    /**
     * Call this only when the app has location permission.
     */
    public void startScan() {
        Context context = getApplication().getApplicationContext();

        if (!wifiManager.isWifiEnabled()) {
            scanError.setValue("Wi-Fi is turned off.");
            isLoading.setValue(false);
            return;
        }

        if (!isReceiverRegistered) {
            IntentFilter filter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
            context.registerReceiver(wifiReceiver, filter);
            isReceiverRegistered = true;
        }

        isLoading.setValue(true);

        boolean started = wifiManager.startScan();
        if (!started) {
            isLoading.setValue(false);
            scanError.setValue("Scan could not be started");
            // Unregister if scan failed to start
            try {
                if (isReceiverRegistered) {
                    context.unregisterReceiver(wifiReceiver);
                    isReceiverRegistered = false;
                }
            } catch (IllegalArgumentException e) {
                Log.w(TAG, "Receiver unregister failed after startScan failed: " + e.getMessage());
            }
        }
    }

    public void clearError() {
        scanError.setValue(null);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Context context = getApplication().getApplicationContext();
        try {
            if (isReceiverRegistered) {
                context.unregisterReceiver(wifiReceiver);
                isReceiverRegistered = false;
            }
        } catch (IllegalArgumentException e) {
            Log.w(TAG, "Receiver unregister failed on ViewModel cleared: " + e.getMessage());
        }
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<List<ScanResult>> getScanResults() {
        return scanResults;
    }

    public LiveData<String> getScanError() {
        return scanError;
    }
}
