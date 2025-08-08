package me.theoria.wifimuscles.view.fragments;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import me.theoria.wifimuscles.R;
import me.theoria.wifimuscles.view.adapters.WifiListAdapter;

public class WifiListFragment extends Fragment {

    private WifiManager wifiManager;
    private WifiListAdapter adapter;

    // Register the new permission launcher
    private final ActivityResultLauncher<String> permissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    startWifiScan();
                } else {
                    Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
            });

    private final BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false);
            if (success) {
                loadWifiList();
            } else {
                Toast.makeText(getContext(), "Wi-Fi Scan Failed", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wifi_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.wifiListRecyclerView);
        adapter = new WifiListAdapter();
        recyclerView.setAdapter(adapter);

        wifiManager = (WifiManager) requireContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        checkPermissionsAndScan();
    }

    private void checkPermissionsAndScan() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Use the modern Activity Result API
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        } else {
            startWifiScan();
        }
    }

    private void startWifiScan() {
        IntentFilter intentFilter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        requireContext().registerReceiver(wifiReceiver, intentFilter);
        boolean started = wifiManager.startScan();
        if (!started) {
            Toast.makeText(getContext(), "Scan could not be started", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadWifiList() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Just return if permission is missing (don't re-request here)
            return;
        }

        List<ScanResult> results = wifiManager.getScanResults();
        adapter.setWifiList(results);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            requireContext().unregisterReceiver(wifiReceiver);
        } catch (Exception ignored) {}
    }
}
