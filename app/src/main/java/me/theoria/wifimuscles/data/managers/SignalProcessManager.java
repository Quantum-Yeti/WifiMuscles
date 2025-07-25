package me.theoria.wifimuscles.data.managers;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import me.theoria.wifimuscles.data.model.RSSIQualityModel;
import me.theoria.wifimuscles.data.model.WifiSignalModel;
import me.theoria.wifimuscles.utils.RSSIUtils;

import java.util.List;

/**
 * SignalProcessManager is responsible for processing the raw Wifi signal data
 * and categorizing each signal into an RSSI String level.
 * (Excellent, Good, Fair, Weak, Unusable)
 */
public class SignalProcessManager {

    /**
     * Method to process and categorize the WifiSignalModel objects.
     */
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
            RSSIQualityModel rssiQualityModel = RSSIQualityModel.mapRssiToStringLevel(rssiFloat);
            int signalLevel = RSSIUtils.returnRssiSignal(rssi);

            // Add entry to the appropriate dataset
            addEntryToDataSets(i, signalLevel, rssiQualityModel, rssiDataSet, excellentSet, goodSet, fairSet, weakSet, unusableSet);
        }
    }

    /**
     * Method to add signal entries to the main dataset with their respective levels.
     */
    private void addEntryToDataSets(
            int index,
            int signalLevel,
            RSSIQualityModel levelModel,
            LineDataSet rssiDataSet,
            LineDataSet excellentSet,
            LineDataSet goodSet,
            LineDataSet fairSet,
            LineDataSet weakSet,
            LineDataSet unusableSet
    ) {
        Entry entry = new Entry(index, signalLevel);

        // Add entries to the appropriate threshold dataset.
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

        // Add the entries to the main dataset.
        rssiDataSet.addEntry(entry);
    }
}