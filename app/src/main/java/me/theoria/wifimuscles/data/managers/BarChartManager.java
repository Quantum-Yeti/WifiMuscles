package me.theoria.wifimuscles.data.managers;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

import me.theoria.wifimuscles.data.model.WifiSignalModel;
import me.theoria.wifimuscles.viewmodel.DataUIViewModel;

public class BarChartManager {

    // Reference UI ViewModel to update text components.
    private final DataUIViewModel uiUpdater;

    /**
     * Constructor takes in the UI ViewModel to update text components.
     * @param uiUpdater
     */
    public BarChartManager(DataUIViewModel uiUpdater) {
        this.uiUpdater = uiUpdater;
    }

    /**
     * Updates the BarChart with the latest list of WiFi signal models.
     * Also updates the associated UI LiveData via the ViewModel.
     * @param signals
     * @param chart
     * @param dataSet
     */
    public void updateBarChart(List<WifiSignalModel> signals, BarChart chart, BarDataSet dataSet) {
        if (signals == null || signals.isEmpty() || chart == null || dataSet == null) return;

        // Limit 6 last entries in the chart.
        int count = Math.min(6, signals.size());
        int start = signals.size() - count;
        List<BarEntry> entries = new ArrayList<>();

        // Conversion for RSSI values into barchart entries.
        for (int i = start; i < signals.size(); i++) {
            entries.add(new BarEntry(i - start, signals.get(i).getRssi()));
        }

        // Applies entries to the dataset.
        dataSet.setValues(entries);

        // Notifies the barchart of updates.
        BarData barData = chart.getData();
        if (barData != null) {
            barData.notifyDataChanged();
        }

        // Refreshes the barchart.
        chart.notifyDataSetChanged();
        chart.invalidate();

        // Updates for the textual UI LiveData values
        if (uiUpdater != null) {
            uiUpdater.updateSignalUI(signals);
        }
    }
}
