package me.theoria.wifimuscles.view.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.*;
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

    // Handles requesting location permission with Activity Result API
    private final ActivityResultLauncher<String> permissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Start Wi-Fi scan if permission granted
                    viewModel.startScan();
                } else {
                    // Show toast if permission is denied
                    showToast("Permission denied");
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate layout using ViewBinding
        binding = FragmentWifiListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize RecyclerView and the Adapter
        adapter = new WifiListAdapter();
        binding.wifiListRecyclerView.setAdapter(adapter);

        // Obtain ViewModel
        viewModel = new ViewModelProvider(this).get(WifiScanViewModel.class);

        // Obtain LiveData from ViewModel
        observeViewModel();

        // Request location permission (required)
        requestLocationPermission();
    }

    /**
     * Method to observe LiveData from the ViewModel
     */
    private void observeViewModel() {
        // Show or hide progress overlay
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), this::showProgress);

        // Updates the Adapter with wifi scan results
        viewModel.getScanResults().observe(getViewLifecycleOwner(), results -> {
            if (results != null) {
                adapter.setWifiList(results);
            }
        });

        // Error message if wifi scan fails
        viewModel.getScanError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                showToast(error);
                viewModel.clearError();
            }
        });
    }

    /**
     * Method to check if location permission is granted, if not - request it
     */
    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        } else {
            viewModel.startScan();
        }
    }

    /**
     * Method to show or hide the Lottie library loading animation
     */
    private void showProgress(boolean show) {
        if (binding == null) return;

        if (show) {
            binding.progressOverlay.setVisibility(View.VISIBLE);
            binding.progressBar.playAnimation();
        } else {
            binding.progressBar.cancelAnimation();
            binding.progressOverlay.setVisibility(View.GONE);
        }
    }

    /**
     * Method to show toast for wifi scan
     */
    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Avoids memory leaks
        binding = null;
    }
}
