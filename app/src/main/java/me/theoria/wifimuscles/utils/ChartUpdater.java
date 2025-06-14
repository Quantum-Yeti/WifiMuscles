package me.theoria.wifimuscles.utils;

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
            int mappedLevel = RSSIUtils.mapRssiToLevels(rssi);

            Entry entry = new Entry(i, mappedLevel);
            entry.setData(rssi);
            rssiDataSet.addEntry(entry);

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

    private static void updateRssiUI(TextView textView, ImageView imageView, int rssi) {
        textView.setText("RSSI: " + rssi + " dBm");
        imageView.setImageResource(RSSIUtils.getRssiEmoji(rssi));
    }

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
