package me.theoria.wifimuscles.data.managers.charts;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

import me.theoria.wifimuscles.data.model.WifiSignalModel;
import me.theoria.wifimuscles.viewmodel.DataUIViewModel;

public class BarChartManager {

    private final DataUIViewModel uiUpdater;

    public BarChartManager(DataUIViewModel uiUpdater) {
        this.uiUpdater = uiUpdater;
    }

    /**
     * Updates the BarChart with the latest WiFi signal data and refreshes the UI via ViewModel.
     *
     * @param signals List of WifiSignalModel representing RSSI values.
     * @param chart   The BarChart to update.
     * @param dataSet The BarDataSet to update with new entries.
     */
    public void updateBarChart(List<WifiSignalModel> signals, BarChart chart, BarDataSet dataSet) {
        if (signals == null || signals.isEmpty() || chart == null || dataSet == null) return;

        final int maxEntries = 8;
        int count = Math.min(maxEntries, signals.size());
        int startIndex = signals.size() - count;

        List<BarEntry> entries = new ArrayList<>(count);

        for (int i = startIndex; i < signals.size(); i++) {
            float rssi = clampRssi(signals.get(i).getRssi());
            float transformedValue = transformRssiToBarValue(rssi);
            entries.add(new BarEntry(i - startIndex, transformedValue));
        }

        dataSet.setValues(entries);

        BarData barData = chart.getData();
        if (barData != null) {
            barData.notifyDataChanged();
        }

        chart.notifyDataSetChanged();
        chart.invalidate();

        if (uiUpdater != null) {
            uiUpdater.updateSignalUI(signals);
        }
    }

    /**
     * Ensures the RSSI value is within the expected range [-127, 0].
     */
    private float clampRssi(float rssi) {
        if (rssi < -127f) return -127f;
        return Math.min(rssi, 0f);
    }

    /**
     * Transforms an RSSI value from [-127, 0] to [0, 127] for BarChart usage.
     */
    private float transformRssiToBarValue(float rssi) {
        return 127f + rssi;
    }
}
