package me.theoria.wifimuscles.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarDataSet;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import me.theoria.wifimuscles.R;
import me.theoria.wifimuscles.data.builders.BarChartBuilder;
import me.theoria.wifimuscles.data.managers.BarChartManager;
import me.theoria.wifimuscles.data.model.InfoCardItem;
import me.theoria.wifimuscles.data.model.WifiSignalModel;
import me.theoria.wifimuscles.databinding.FragmentBarChartBinding;
import me.theoria.wifimuscles.utils.ToastUtils;
import me.theoria.wifimuscles.view.adapters.InfoCardAdapter;
import me.theoria.wifimuscles.viewmodel.DataUIViewModel;
import me.theoria.wifimuscles.viewmodel.WifiViewModel;

public class BarChartFragment extends Fragment {

    private FragmentBarChartBinding binding;

    private WifiViewModel wifiViewModel;
    private DataUIViewModel dataUIViewModel;

    private BarChartManager barChartManager;
    private BarDataSet barDataSet;
    private BarChart barChart;

    private InfoCardAdapter adapter;

    private MaterialButton switchChartButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBarChartBinding.inflate(inflater, container, false);

        initBarChartUI();
        setupBarChart();

        setupRecyclerView();

        wifiViewModel = new ViewModelProvider(this).get(WifiViewModel.class);
        dataUIViewModel = new ViewModelProvider(this).get(DataUIViewModel.class);

        observeSignalData();
        observeSignalUI();

        switchChartButton = binding.switchChartButton;
        switchChartButton.setOnClickListener(v -> openLineChartFragment());

        return binding.getRoot();
    }

    private void initBarChartUI() {
        barChart = binding.barChart;
    }

    private void setupBarChart() {
        barDataSet = BarChartBuilder.setupBarChart(barChart, requireContext());
        barChartManager = new BarChartManager(dataUIViewModel);
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = binding.chartInfoRecyclerView;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new InfoCardAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void observeSignalData() {
        wifiViewModel.getRssiLiveData().observe(getViewLifecycleOwner(), signals -> {
            if (signals == null || signals.isEmpty()) return;

            barChartManager.updateBarChart(signals, barChart, barDataSet);
            dataUIViewModel.updateSignalUI(signals);
        });
    }

    private void observeSignalUI() {
        dataUIViewModel.getRssiText().observe(getViewLifecycleOwner(), v -> updateInfoCards());
        dataUIViewModel.getRssiEmoji().observe(getViewLifecycleOwner(), v -> updateInfoCards());
        dataUIViewModel.getIpText().observe(getViewLifecycleOwner(), v -> updateInfoCards());
        dataUIViewModel.getFrequencyText().observe(getViewLifecycleOwner(), v -> updateInfoCards());
        dataUIViewModel.getBandwidthText().observe(getViewLifecycleOwner(), v -> updateInfoCards());
        dataUIViewModel.getSSIDText().observe(getViewLifecycleOwner(), v -> updateInfoCards());
        dataUIViewModel.getBssidText().observe(getViewLifecycleOwner(), v -> updateInfoCards());
        dataUIViewModel.getLinkSpeed().observe(getViewLifecycleOwner(), v -> updateInfoCards());
        dataUIViewModel.getMaxLinkSpeed().observe(getViewLifecycleOwner(), v -> updateInfoCards());
        wifiViewModel.getWifiStandardLiveData().observe(getViewLifecycleOwner(), v -> updateInfoCards());

        dataUIViewModel.getToastLevelEvent().observe(getViewLifecycleOwner(), level -> {
            if (level == 3 || level == 2 || level == 1) {
                ToastUtils.snackBarExtenderNotice(binding.getRoot(), level);
            }
        });
    }

    private void updateInfoCards() {
        List<InfoCardItem> items = new ArrayList<>();
        items.add(new InfoCardItem("SSID", dataUIViewModel.getSSIDText().getValue(), R.drawable.icon_ssid));
        items.add(new InfoCardItem("RSSI", dataUIViewModel.getRssiText().getValue(),
                dataUIViewModel.getRssiEmoji().getValue() != null ? dataUIViewModel.getRssiEmoji().getValue() : R.drawable.emoji_bad));
        items.add(new InfoCardItem(getString(R.string.frequency_card_short), dataUIViewModel.getFrequencyText().getValue(), R.drawable.icon_function));
        items.add(new InfoCardItem(getString(R.string.frequency_band_card), dataUIViewModel.getBandwidthText().getValue(), R.drawable.icon_function));
        items.add(new InfoCardItem(getString(R.string.ip_add_card), dataUIViewModel.getIpText().getValue(), R.drawable.icon_dns));
        items.add(new InfoCardItem(getString(R.string.ap_mac), dataUIViewModel.getBssidText().getValue(), R.drawable.icon_dns));
        items.add(new InfoCardItem(getString(R.string.link_speed_card), dataUIViewModel.getLinkSpeed().getValue(), R.drawable.icon_rocket));
        items.add(new InfoCardItem(getString(R.string.max_speed), dataUIViewModel.getMaxLinkSpeed().getValue(), R.drawable.icon_rocket));

        adapter.updateItems(items);
    }

    private void openLineChartFragment() {
        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new ChartFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        wifiViewModel.startUpdates();
    }

    @Override
    public void onStop() {
        super.onStop();
        wifiViewModel.stopUpdates();
    }
}
