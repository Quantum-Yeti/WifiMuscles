package me.theoria.wifimuscles.data.managers;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import me.theoria.wifimuscles.data.model.RSSILevelModel;
import me.theoria.wifimuscles.data.model.WifiSignalModel;
import me.theoria.wifimuscles.utils.RSSIUtils;

import java.util.List;

public class SignalProcessManager {

    public void processSignals(
            List<WifiSignalModel> signals,
            LineDataSet rssiDataSet,
            LineDataSet excellentSet,
            LineDataSet goodSet,
            LineDataSet fairSet,
            LineDataSet weakSet,
            LineDataSet unusableSet
    ) {
        for (int i = 0; i < signals.size(); i++) {
            WifiSignalModel signal = signals.get(i);
            int rssi = signal.getRssi();
            float rssiFloat = signal.getRssi();

            // Process each signal and map it to its appropriate level
            RSSILevelModel rssiLevelModel = RSSILevelModel.mapRssi(rssiFloat);
            int signalLevel = RSSIUtils.mapRssiToLevels(rssi);

            // Add entry to the appropriate dataset
            addEntryToDataSets(i, signalLevel, rssiLevelModel, rssiDataSet, excellentSet, goodSet, fairSet, weakSet, unusableSet);
        }
    }

    private void addEntryToDataSets(
            int index,
            int signalLevel,
            RSSILevelModel levelModel,
            LineDataSet rssiDataSet,
            LineDataSet excellentSet,
            LineDataSet goodSet,
            LineDataSet fairSet,
            LineDataSet weakSet,
            LineDataSet unusableSet
    ) {
        Entry entry = new Entry(index, signalLevel);

        if (levelModel != null) {
            switch (levelModel) {
                case EXCELLENT:
                    excellentSet.addEntry(entry);
                    break;
                case GOOD:
                    goodSet.addEntry(entry);
                    break;
                case FAIR:
                    fairSet.addEntry(entry);
                    break;
                case WEAK:
                    weakSet.addEntry(entry);
                    break;
                case UNUSABLE:
                    unusableSet.addEntry(entry);
                    break;
            }
        }

        rssiDataSet.addEntry(entry);
    }
}