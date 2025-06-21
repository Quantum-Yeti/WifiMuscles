package me.theoria.wifimuscles.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.List;

import me.theoria.wifimuscles.R;
import me.theoria.wifimuscles.data.model.WifiSignalModel;
import me.theoria.wifimuscles.viewmodel.DataUIViewModel;
import me.theoria.wifimuscles.viewmodel.WifiViewModel;

public class StatsFragment extends Fragment {

    private TextView frequencyTextView, bandwidthTextView, ipTextView, rssiTextView, ssidTextView, macTextView, rxTextView, maxLinkSpeedTextView;
    private ImageView rssiEmojiView;
    private LineChart chart;
    private LineDataSet primaryLineDataSet;
    private LineDataSet excellentSet, goodSet, fairSet, weakSet, unusableSet;
    private LineData lineData;

    private DataUIViewModel dataUIViewModel;
    private WifiViewModel wifiViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_stats, container, false);
        bindViews(root);

        // Initialize ViewModels
        wifiViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(WifiViewModel.class);
        dataUIViewModel = new ViewModelProvider(this).get(DataUIViewModel.class);

        // Observe LiveData being passed from the ViewModels
        observeLiveData();

        return root;

    }

    private void bindViews(View root) {
        rssiTextView = root.findViewById(R.id.rssiTextView);
        ipTextView = root.findViewById(R.id.ipBox);
        frequencyTextView = root.findViewById(R.id.frequencyBox);
        bandwidthTextView = root.findViewById(R.id.bandBox);
        ssidTextView = root.findViewById(R.id.ssidBox);
        macTextView = root.findViewById(R.id.macBox);
        rxTextView = root.findViewById(R.id.rxSpeedBox);
        maxLinkSpeedTextView = root.findViewById(R.id.maxSpeedBox);
    }

    public void observeLiveData() {
        // Observes additional Wifi data
        dataUIViewModel.getRssiText().observe(getViewLifecycleOwner(), rssiTextView::setText);
        dataUIViewModel.getIpText().observe(getViewLifecycleOwner(), ipTextView::setText);
        dataUIViewModel.getFrequencyText().observe(getViewLifecycleOwner(), frequencyTextView::setText);
        dataUIViewModel.getBandwidthText().observe(getViewLifecycleOwner(), bandwidthTextView::setText);
        dataUIViewModel.getSSIDText().observe(getViewLifecycleOwner(), ssidTextView::setText);
        dataUIViewModel.getMac().observe(getViewLifecycleOwner(), macTextView::setText);
        dataUIViewModel.getLinkSpeed().observe(getViewLifecycleOwner(), rxTextView::setText);
        dataUIViewModel.getMaxLinkSpeed().observe(getViewLifecycleOwner(), maxLinkSpeedTextView::setText);

        wifiViewModel.getRssiLiveData().observe(getViewLifecycleOwner(), signals -> {
            if (signals != null && !signals.isEmpty()) {
                dataUIViewModel.updateSignalUI(signals);
            }
        });
    }

    private void updateData(List<WifiSignalModel> signals) {
        if (signals == null || signals.isEmpty()) return;


        // Displays the latest RSSI data as it updates in a TextView
        WifiSignalModel latest = signals.get(signals.size() - 1);
        rssiTextView.setText("RSSI: " + latest.getRssi() + " dBm");

    }

}