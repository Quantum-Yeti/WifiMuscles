package me.theoria.wifimuscles.data.managers;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.List;

import me.theoria.wifimuscles.data.model.WifiSignalModel;
import me.theoria.wifimuscles.viewmodel.DataUIViewModel;

public class ChartManager {

    private final SignalProcessManager signalProcessManager;
    private final DataUIViewModel uiUpdater;

    public ChartManager(SignalProcessManager signalProcessManager, DataUIViewModel uiUpdater) {
        this.signalProcessManager = signalProcessManager;
        this.uiUpdater = uiUpdater;
    }

    public void updateChart(
            List<WifiSignalModel> signals,
            LineChart chart,
            LineDataSet rssiDataSet,
            LineDataSet excellentSet,
            LineDataSet goodSet,
            LineDataSet fairSet,
            LineDataSet weakSet,
            LineDataSet unusableSet,
            LineData lineData
    ) {
        if (chart == null || rssiDataSet == null || lineData == null) return;

        // Clear datasets
        clearAllDataSets(rssiDataSet, excellentSet, goodSet, fairSet, weakSet, unusableSet);

        // Process signals and update UI
        signalProcessManager.processSignals(signals, rssiDataSet, excellentSet, goodSet, fairSet, weakSet, unusableSet);

        if (uiUpdater != null) {
            uiUpdater.updateSignalUI(signals);
        }

        // Notify chart update
        notifyChartUpdated(chart, lineData, rssiDataSet, excellentSet, goodSet, fairSet, weakSet, unusableSet);
    }

    private void clearAllDataSets(LineDataSet... sets) {
        for (LineDataSet set : sets) {
            set.clear();
        }
    }

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

