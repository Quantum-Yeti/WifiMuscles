package me.theoria.wifimuscles.core.viewmodel;

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
        // Ensure Wi-Fi is enabled
        if (!wifiManager.isWifiEnabled()) {
            Log.d(TAG, "Wi-Fi is turned off. Enabling Wi-Fi...");
            wifiManager.setWifiEnabled(true);
            scanError.setValue("Wi-Fi is turned off. Enabling Wi-Fi...");
            isLoading.setValue(false);
            return;  // Exit early as Wi-Fi is required for scanning
        }

        // Ensure location services are enabled
        if (!isLocationEnabled()) {
            Log.d(TAG, "Location services are disabled. Cannot start scan.");
            scanError.setValue("Location services are disabled.");
            isLoading.setValue(false);
            return;  // Exit early as location services are required for scanning
        }

        long now = System.currentTimeMillis();
        /*if (now - lastScanTime < SCAN_THROTTLE_MS) {
            Log.d(TAG, "Scan throttled. Try again later.");
            scanError.setValue("Scan throttled. Try again later.");
            isLoading.setValue(false);
            return;  // Exit early due to throttling
        }*/

        // Proceed with the scan
        lastScanTime = now;
        isLoading.setValue(true);  // Show loading state

        // Register the receiver to listen for scan results
        registerReceiver();

        // Start the scan
        boolean started = wifiManager.startScan();
        if (!started) {
            Log.e(TAG, "wifiManager.startScan() returned false.");
            scanError.setValue("Failed to start Wi-Fi scan.");
            isLoading.setValue(false);
            cleanupReceiver();
            return;
        }

        // Timeout fallback in case the scan takes too long
        timeoutHandler.postDelayed(() -> {
            if (isLoading.getValue() != null && isLoading.getValue()) {
                Log.w(TAG, "Scan timeout fallback triggered.");
                scanError.setValue("Wi-Fi scan timed out.");
                isLoading.setValue(false);
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
