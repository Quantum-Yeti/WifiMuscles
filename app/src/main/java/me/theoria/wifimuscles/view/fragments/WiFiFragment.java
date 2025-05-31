package me.theoria.wifimuscles.view.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

import me.theoria.wifimuscles.R;
import me.theoria.wifimuscles.viewmodel.WiFiViewModel;

public class WiFiFragment extends Fragment {

    private WiFiViewModel viewModel;
    private TextView rssiText;

    //PieChart
    private PieChart pieChart;


    //LineChart
    private LineChart lineChart;
    private LineDataSet dataSet;
    private LineData lineData;
    private WifiManager wifiManager;
    private Handler handler = new Handler();
    private int xValue = 0;

    private final Runnable updateTask = new Runnable() {
        @Override
        public void run() {
            updatePieChartWithRSSI();
            updateChartWithRSSI();
            handler.postDelayed(this, 1000);
        }
    };



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wifi, container, false);
        rssiText = view.findViewById(R.id.rssi_value);

        pieChart = view.findViewById(R.id.pieChart);
        lineChart = view.findViewById(R.id.lineChart);
        wifiManager = (WifiManager) requireContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        setupChart();
        requestLocationPermission();

        handler.post(updateTask);

        return view;

         // Start periodic updates


    }

    private void setupChart() {
        dataSet = new LineDataSet(new ArrayList<>(), "Wi-Fi RSSI (dBm)");
        dataSet.setColor(Color.BLUE);
        dataSet.setLineWidth(3f);
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setHoleRadius(40f);
        pieChart.setTransparentCircleRadius(45f);
        pieChart.setCenterText("WiFi Signal");
        pieChart.setCenterTextSize(18f);
        pieChart.getLegend().setEnabled(false);

        lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.getDescription().setEnabled(false);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getAxisRight().setEnabled(false);
    }

    private void updatePieChartWithRSSI() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            WifiInfo info = wifiManager.getConnectionInfo();
            int rssi = info.getRssi();

            String category = classifyRSSI(rssi);
            ArrayList<PieEntry> entries = new ArrayList<>();
            entries.add(new PieEntry(100f, category));

            PieDataSet dataSet = new PieDataSet(entries, "Signal Strength");

            dataSet.setDrawValues(false);

            PieData pieData = new PieData(dataSet);
            pieChart.setData(pieData);
            pieChart.invalidate();  // Refresh chart
        }
    }

    private String classifyRSSI(int rssi) {
        if (rssi >= -50) return "Excellent";
        else if (rssi >= -60) return "Good";
        else if (rssi >= -70) return "Fair";
        else return "Weak";
    }

    private void updateChartWithRSSI() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            WifiInfo info = wifiManager.getConnectionInfo();
            int rssi = info.getRssi();

            lineData.addEntry(new Entry(xValue++, rssi), 0);
            lineData.notifyDataChanged();
            lineChart.notifyDataSetChanged();
            lineChart.setVisibleXRangeMaximum(50);
            lineChart.moveViewToX(lineData.getEntryCount());
        }
    }

    private void requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
        }
    }

    @Override
    public void onDestroyView() {
        handler.removeCallbacks(updateTask);
        super.onDestroyView();
    }



    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(WiFiViewModel.class);
        viewModel.initRepository(requireContext());

        viewModel.getRssiLiveData().observe(getViewLifecycleOwner(), rssi -> {
            rssiText.setText("RSSI: " + rssi + " dBm");
        });



    }
}
