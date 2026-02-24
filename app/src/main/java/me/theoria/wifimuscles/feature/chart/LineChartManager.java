package me.theoria.wifimuscles.feature.chart;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.List;

import me.theoria.wifimuscles.feature.signal.SignalProcessManager;
import me.theoria.wifimuscles.model.WifiSignalModel;
import me.theoria.wifimuscles.viewmodel.DataUIViewModel;

/**
 * LineChartManager updates and renders the LineChart.
 */
public class LineChartManager {

    private final SignalProcessManager signalProcessManager;
    private final DataUIViewModel uiUpdater;

    public LineChartManager(SignalProcessManager signalProcessManager, DataUIViewModel uiUpdater) {
        this.signalProcessManager = signalProcessManager;
        this.uiUpdater = uiUpdater;
    }

    /**
     * Updates the LineChart with RSSI signal data and categorized thresholds.
     */
    public void updateLineChart(
            List<WifiSignalModel> signals,
            LineChart chart,
            LineDataSet rssiDataSet,
            LineDataSet excellentSet,
            LineDataSet goodSet,
            LineDataSet fairSet,
            LineDataSet weakSet,
            LineDataSet unusableSet,
            LineDataSet interferenceSet,
            LineData lineData
    ) {
        if (chart == null || rssiDataSet == null || lineData == null) return;

        clearAllDataSets(rssiDataSet, excellentSet, goodSet, fairSet, weakSet, unusableSet, interferenceSet);

        signalProcessManager.processSignals(signals, rssiDataSet, excellentSet, goodSet, fairSet, weakSet, unusableSet, interferenceSet);

        // Add entries to interference dataset linegraph
        for (int i = 0; i < signals.size(); i++) {
            WifiSignalModel signal = signals.get(i);

            float interferenceValue = signal.getInterferenceLevel();
            Entry entry = new Entry(i, interferenceValue);
            interferenceSet.addEntry(entry);
        }

        if (uiUpdater != null) {
            uiUpdater.updateSignalUI(signals);
        }

        notifyChartUpdated(chart, lineData, rssiDataSet, excellentSet, goodSet, fairSet, weakSet, unusableSet, interferenceSet);
    }

    /**
     * Clears all entries from the given datasets.
     */
    private void clearAllDataSets(LineDataSet... sets) {
        for (LineDataSet set : sets) {
            if (set != null) set.clear();
        }
    }

    /**
     * Notifies the chart and its datasets to refresh display.
     */
    private void notifyChartUpdated(
            LineChart chart,
            LineData lineData,
            LineDataSet... sets
    ) {
        for (LineDataSet set : sets) {
            set.notifyDataSetChanged();
        }

        lineData.notifyDataChanged();
        chart.notifyDataSetChanged();
        chart.invalidate();

        chart.setVisibleXRangeMaximum(30);
        chart.moveViewToX(lineData.getEntryCount());
    }
}
