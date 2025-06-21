package me.theoria.wifimuscles.data.managers;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.List;

import me.theoria.wifimuscles.data.model.WifiSignalModel;
import me.theoria.wifimuscles.viewmodel.DataUIViewModel;

/**
 * LineChartManager updates and renders the LineChart.
 */
public class LineChartManager {

    // References to threshold data and UI components.
    private final SignalProcessManager signalProcessManager;
    private final DataUIViewModel uiUpdater;

    /**
     * Constructor initializes the data dependencies.
     * @param signalProcessManager
     * @param uiUpdater
     */
    public LineChartManager(SignalProcessManager signalProcessManager, DataUIViewModel uiUpdater) {
        this.signalProcessManager = signalProcessManager;
        this.uiUpdater = uiUpdater;
    }

    /**
     * Method to update the LineChart and its datasets.
     *
     * @param signals
     * @param chart
     * @param rssiDataSet
     * @param excellentSet
     * @param goodSet
     * @param fairSet
     * @param weakSet
     * @param unusableSet
     * @param lineData
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
            LineData lineData
    ) {
        if (chart == null || rssiDataSet == null || lineData == null) return;

        // Clear previous datasets
        clearAllDataSets(rssiDataSet, excellentSet, goodSet, fairSet, weakSet, unusableSet);

        // Process, classify and add datasets.
        signalProcessManager.processSignals(signals, rssiDataSet, excellentSet, goodSet, fairSet, weakSet, unusableSet);

        // Updates text UI elements.
        if (uiUpdater != null) {
            uiUpdater.updateSignalUI(signals);
        }

        // Refresh the chart with new data.
        notifyChartUpdated(chart, lineData, rssiDataSet, excellentSet, goodSet, fairSet, weakSet, unusableSet);
    }

    /**
     * Method to clear entries in the datasets.
     * @param sets
     */
    private void clearAllDataSets(LineDataSet... sets) {
        for (LineDataSet set : sets) {
            set.clear();
        }
    }

    /**
     * Notify the linechart and data objects of updates then redraws the chart.
     *
     * @param chart
     * @param lineData
     * @param sets
     */
    private void notifyChartUpdated(
            LineChart chart,
            LineData lineData,
            LineDataSet... sets
    ) {
        // Notify of data being changed.
        for (LineDataSet set : sets) {
            set.notifyDataSetChanged();
        }

        // Notify the data object and chart of changes.
        lineData.notifyDataChanged();
        chart.notifyDataSetChanged();
        chart.invalidate();

        // Keeps only the most recent 30 entries visible on the X-axis.
        chart.setVisibleXRangeMaximum(30);
        chart.moveViewToX(lineData.getEntryCount());
    }
}

