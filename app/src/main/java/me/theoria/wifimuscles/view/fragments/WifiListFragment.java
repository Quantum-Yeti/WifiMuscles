package me.theoria.wifimuscles.view.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.*;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.*;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import me.theoria.wifimuscles.databinding.FragmentWifiListBinding;
import me.theoria.wifimuscles.view.adapters.WifiListAdapter;
import me.theoria.wifimuscles.viewmodel.WifiScanViewModel;

public class WifiListFragment extends Fragment {

    private static final String TAG = "WifiListFragment";

    private FragmentWifiListBinding binding;
    private WifiListAdapter adapter;
    private WifiScanViewModel viewModel;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable hideProgressRunnable;
    private long animationStartTime = 0;

    private boolean hasStartedScan = false;

    private final ActivityResultLauncher<String> permissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Log.d(TAG, "Permission granted via launcher");
                    startWifiScan();
                } else {
                    showToast("Location permission is required to scan Wi-Fi.");
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentWifiListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new WifiListAdapter();
        binding.wifiListRecyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(WifiScanViewModel.class);

        binding.swipeRefreshLayout.setOnRefreshListener(this::requestLocationPermission);

        observeViewModel();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!hasStartedScan) {
            hasStartedScan = true;
            Log.d(TAG, "onResume: initiating first scan");
            requestLocationPermission();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        hasStartedScan = false;
    }

    private void requestLocationPermission() {
        Log.d(TAG, "Checking location permission");
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Requesting location permission");
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        } else {
            Log.d(TAG, "Permission already granted");
            startWifiScan();
        }
    }

    private void startWifiScan() {
        if (viewModel != null) {
            Log.d(TAG, "Calling viewModel.startScan()");
            viewModel.startScan();
        }
    }

    private void observeViewModel() {
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (binding == null) return;
            Log.d(TAG, "isLoading: " + isLoading);

            showProgress(isLoading);

            if (!isLoading) {
                binding.swipeRefreshLayout.setRefreshing(false);
            }
        });

        viewModel.getScanResults().observe(getViewLifecycleOwner(), results -> {
            if (results != null) {
                Log.d(TAG, "Scan results received: " + results.size());
                results.sort((a, b) -> Integer.compare(b.level, a.level));
                adapter.setWifiList(results);
            }
        });

        viewModel.getScanError().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                showToast(error);
                viewModel.clearError();
            }
        });
    }

    private void showProgress(boolean show) {
        if (binding == null) return;

        if (show) {
            binding.progressOverlay.setVisibility(View.VISIBLE);
            binding.progressBar.playAnimation();
            animationStartTime = System.currentTimeMillis();

            if (hideProgressRunnable != null) {
                handler.removeCallbacks(hideProgressRunnable);
            }

        } else {
            long elapsed = System.currentTimeMillis() - animationStartTime;
            long remaining = 2000 - elapsed;

            if (hideProgressRunnable != null) {
                handler.removeCallbacks(hideProgressRunnable);
            }

            hideProgressRunnable = () -> {
                if (binding != null) {
                    binding.progressBar.cancelAnimation();
                    binding.progressOverlay.setVisibility(View.GONE);
                }
            };

            handler.postDelayed(hideProgressRunnable, Math.max(remaining, 0));
        }
    }

    private void showToast(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}
