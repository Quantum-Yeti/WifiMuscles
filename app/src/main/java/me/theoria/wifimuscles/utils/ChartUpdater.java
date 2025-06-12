package me.theoria.wifimuscles.utils;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.List;

import me.theoria.wifimuscles.model.RSSILevel;
import me.theoria.wifimuscles.model.WifiSignal;

public class ChartUpdater {

    public static void updateChart(
            LineChart chart,
            LineDataSet mainDataSet,
            LineDataSet excellentSet,
            LineDataSet goodSet,
            LineDataSet fairSet,
            LineDataSet weakSet,
            LineDataSet terribleSet,
            LineData lineData,
            List<WifiSignal> signals
    ) {
        if (chart == null || mainDataSet == null || lineData == null) {
            return;
        }

        excellentSet.clear();
        goodSet.clear();
        fairSet.clear();
        weakSet.clear();
        terribleSet.clear();
        mainDataSet.clear(); // Clear to prevent duplication on next update

        for (int i = 0; i < signals.size(); i++) {
            float rssi = signals.get(i).getRssi();
            int signalLevel = ChartConfigurator.mapRssiToLevel((int) rssi);
            Entry entry = new Entry(i, signalLevel);

            RSSILevel rssiLevel = RSSILevel.mapRssi(rssi);
            switch (rssiLevel) {
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
                case TERRIBLE:
                    terribleSet.addEntry(entry);
                    break;
            }

            mainDataSet.addEntry(entry);
        }

        excellentSet.notifyDataSetChanged();
        goodSet.notifyDataSetChanged();
        fairSet.notifyDataSetChanged();
        weakSet.notifyDataSetChanged();
        terribleSet.notifyDataSetChanged();
        mainDataSet.notifyDataSetChanged();
        lineData.notifyDataChanged();

        chart.notifyDataSetChanged();
        chart.invalidate();

        chart.setVisibleXRangeMaximum(100);
        chart.moveViewToX(lineData.getEntryCount());


    }


}
