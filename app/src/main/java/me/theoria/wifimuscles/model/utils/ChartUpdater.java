package me.theoria.wifimuscles.model.utils;

import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.List;

import me.theoria.wifimuscles.model.RSSILevelModel;
import me.theoria.wifimuscles.model.WifiSignalModel;

public class ChartUpdater {

    public static void updateChart(
            TextView rssiTextView,
            ImageView rssiEmojiView,
            LineChart chart,
            LineDataSet rssiDataSet,
            LineDataSet excellentSet,
            LineDataSet goodSet,
            LineDataSet fairSet,
            LineDataSet weakSet,
            LineDataSet unusableSet,
            LineData lineData,
            List<WifiSignalModel> signals
    ) {
        if (chart == null || rssiDataSet == null || lineData == null) return;

        clearAllDataSets(rssiDataSet, excellentSet, goodSet, fairSet, weakSet, unusableSet);

        for (int i = 0; i < signals.size(); i++) {
            WifiSignalModel signal = signals.get(i);
            int rssi = signal.getRssi();
            float rssiFloat = signal.getRssi();
            RSSILevelModel levelModel = RSSILevelModel.mapRssi(rssi);
            //int mappedLevel = RSSIUtils.mapRssiToLevels(rssi);

            //Entry entry = new Entry(i, mappedLevel);
            //entry.setData(rssi);
            //rssiDataSet.addEntry(entry);

            updateRssiUI(rssiTextView, rssiEmojiView, rssi);

            RSSILevelModel rssiLevelModel = RSSILevelModel.mapRssi(rssiFloat);
            int signalLevel = RSSIUtils.mapRssiToLevels(rssi);

            addEntryToDataSets(i, signalLevel, rssiLevelModel, rssiDataSet, excellentSet, goodSet, fairSet, weakSet, unusableSet);
        }

        notifyChartUpdated(chart, lineData, rssiDataSet, excellentSet, goodSet, fairSet, weakSet, unusableSet);
    }

    private static void clearAllDataSets(LineDataSet... sets) {
        for (LineDataSet set : sets) {
            set.clear();
        }
    }

    /**
     * Method: updateRssiUI
     * This method updates the TextView and ImageView for the integer reading and emoji reading
     * based on the RSSI live data.
     * @param textView
     * @param imageView
     * @param rssi
     */
    private static void updateRssiUI(TextView textView, ImageView imageView, int rssi) {
        // Updates RSSI in dBm textview (integer)
        textView.setText("RSSI: " + rssi + " dBm");
        //Updates the emoji for dBm levels set in model
        imageView.setImageResource(RSSIUtils.getRssiEmoji(rssi));
    }

    /**
     * Method: addEntryToDataSets
     * This method takes the RSSI LiveData assigned in the model, binds it to the appropriate String level sets
     * and adds the levels to chart/graph entries.
     * @param index
     * @param signalLevel
     * @param levelModel
     * @param rssiDataSet
     * @param excellentSet
     * @param goodSet
     * @param fairSet
     * @param weakSet
     * @param terribleSet
     */
    private static void addEntryToDataSets(
            int index,
            int signalLevel,
            RSSILevelModel levelModel,
            LineDataSet rssiDataSet,
            LineDataSet excellentSet,
            LineDataSet goodSet,
            LineDataSet fairSet,
            LineDataSet weakSet,
            LineDataSet terribleSet
    ) {
        Entry entry = new Entry(index, signalLevel);

        if (levelModel != null) {
            switch (levelModel) {
                case EXCELLENT: excellentSet.addEntry(entry); break;
                case GOOD: goodSet.addEntry(entry); break;
                case FAIR: fairSet.addEntry(entry); break;
                case WEAK: weakSet.addEntry(entry); break;
                case UNUSABLE: terribleSet.addEntry(entry); break;
            }
        }

        rssiDataSet.addEntry(entry);
    }

    /**
     * Method: notifyChartUpdated
     * @param chart
     * @param lineData
     * @param sets
     */
    private static void notifyChartUpdated(
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
