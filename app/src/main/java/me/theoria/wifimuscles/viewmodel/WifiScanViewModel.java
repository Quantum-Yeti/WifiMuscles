package me.theoria.wifimuscles.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.*;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class WifiScanViewModel extends AndroidViewModel {

    private static final String TAG = "WifiScanViewModel";
    private static final long SCAN_THROTTLE_MS = 30_000; // 30 seconds
    private static final long SCAN_TIMEOUT_MS = 10_000; // 10 seconds fallback

    private final WifiManager wifiManager;
    private final Context context;
    private final Handler timeoutHandler = new Handler(Looper.getMainLooper());

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<List<ScanResult>> scanResults = new MutableLiveData<>();
    private final MutableLiveData<String> scanError = new MutableLiveData<>();

    private boolean isReceiverRegistered = false;
    private long lastScanTime = 0;

    private final BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @SuppressLint("MissingPermission") // Permissions are checked in the Fragment
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false);

            if (success) {
                List<ScanResult> results = wifiManager.getScanResults();
                Log.d(TAG, "Scan success. Found " + results.size() + " networks.");
                scanResults.setValue(results);
                if (results.isEmpty()) {
                    scanError.setValue("No Wi-Fi networks found.");
                }
            } else {
                Log.w(TAG, "Scan broadcast received but marked as failure.");
                scanError.setValue("Wi-Fi Scan Failed.");
            }

            isLoading.setValue(false);
            cleanupReceiver();
        }
    };

    public WifiScanViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getApplicationContext();
        this.wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    // Update isLoading LiveData when the scan starts
    public void startScan() {
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
            scanError.setValue("Wi-Fi is turned off.");
            isLoading.setValue(false);  // Set loading to false on failure
            return;
        }

        if (!isLocationEnabled()) {
            scanError.setValue("Location services are disabled.");
            isLoading.setValue(false);  // Set loading to false on failure
            return;
        }

        long now = System.currentTimeMillis();
        if (now - lastScanTime < SCAN_THROTTLE_MS) {
            scanError.setValue("Scan throttled. Try again later.");
            isLoading.setValue(false);  // Set loading to false on failure
            return;
        }

        lastScanTime = now;
        isLoading.setValue(true);  // Set loading to true to show animation

        registerReceiver();

        boolean started = wifiManager.startScan();
        if (!started) {
            Log.e(TAG, "wifiManager.startScan() returned false.");
            isLoading.setValue(false);  // Set loading to false on failure
            scanError.setValue("Failed to start Wi-Fi scan.");
            cleanupReceiver();
            return;
        }

        // Timeout fallback: unregister and stop loading after timeout
        timeoutHandler.postDelayed(() -> {
            if (isLoading.getValue() != null && isLoading.getValue()) {
                Log.w(TAG, "Scan timeout fallback triggered.");
                scanError.setValue("Wi-Fi scan timed out.");
                isLoading.setValue(false);  // Set loading to false on timeout
                cleanupReceiver();
            }
        }, SCAN_TIMEOUT_MS);
    }


    private void registerReceiver() {
        if (!isReceiverRegistered) {
            IntentFilter filter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
            context.registerReceiver(wifiReceiver, filter);
            isReceiverRegistered = true;
        }
    }

    private void cleanupReceiver() {
        if (isReceiverRegistered) {
            try {
                context.unregisterReceiver(wifiReceiver);
            } catch (IllegalArgumentException e) {
                Log.w(TAG, "Receiver already unregistered: " + e.getMessage());
            }
            isReceiverRegistered = false;
        }
        timeoutHandler.removeCallbacksAndMessages(null); // clear timeout
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void clearError() {
        scanError.setValue(null);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        cleanupReceiver();
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
