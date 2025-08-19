package me.theoria.wifimuscles.view.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
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

    private FragmentWifiListBinding binding;
    private WifiListAdapter adapter;
    private WifiScanViewModel viewModel;

    // Request location permission using Activity Result API
    private final ActivityResultLauncher<String> permissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    viewModel.startScan();
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

        // Setup RecyclerView
        adapter = new WifiListAdapter();
        binding.wifiListRecyclerView.setAdapter(adapter);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(WifiScanViewModel.class);

        // Optional: add swipe to refresh
        binding.swipeRefreshLayout.setOnRefreshListener(this::requestLocationPermission);

        tryStartScan();

        // Observe LiveData from ViewModel
        observeViewModel();

    }

    private void observeViewModel() {
        // Observe loading state to control Lottie animation visibility
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (binding == null) return;

            Log.d("WifiListFragment", "isLoading: " + isLoading);

            // Show or hide the progress bar (Lottie animation)
            if (isLoading) {
                binding.progressBar.setVisibility(View.VISIBLE); // Show the Lottie animation
                binding.progressBar.playAnimation();
            } else {
                binding.progressBar.setVisibility(View.GONE); // Hide the Lottie animation
                binding.progressBar.cancelAnimation();
            }

            // Hide the refresh layout spinner
            if (!isLoading) {
                binding.swipeRefreshLayout.setRefreshing(false);
            }
        });

        // Observe scan results and update RecyclerView
        viewModel.getScanResults().observe(getViewLifecycleOwner(), results -> {
            if (results != null) {
                // Sort results in descending order based on RSSI
                results.sort((a, b) -> Integer.compare(b.level, a.level));
                adapter.setWifiList(results);
            }
        });

        // Observe scan error to show error message
        viewModel.getScanError().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                showToast(error);
                viewModel.clearError();
            }
        });
    }


    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        } else {
            viewModel.startScan();
        }
    }

    public void tryStartScan() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            viewModel.startScan();
        } else {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }


    private void showToast(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        tryStartScan();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void refreshScan() {
        tryStartScan();
    }

}
