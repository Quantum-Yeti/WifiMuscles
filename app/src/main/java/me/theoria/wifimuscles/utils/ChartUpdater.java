package me.theoria.wifimuscles.utils;

import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.List;
import java.util.Objects;

import me.theoria.wifimuscles.R;
import me.theoria.wifimuscles.model.RSSILevelModel;
import me.theoria.wifimuscles.model.WifiSignalModel;
import me.theoria.wifimuscles.viewmodel.ChartViewModel;

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
            LineDataSet terribleSet,
            LineData lineData,
            List<WifiSignalModel> signals
    ) {
        if (chart == null || rssiDataSet == null || lineData == null) {
            return;
        }

        // Clear prior data
        excellentSet.clear();
        goodSet.clear();
        fairSet.clear();
        weakSet.clear();
        terribleSet.clear();
        rssiDataSet.clear(); // Clear to prevent duplication on next update

        for (int i = 0; i < signals.size(); i++) {
            // Retrieve float RSSI
            float rssi = signals.get(i).getRssi();
            // Retrieve integer RSSI
            int textRSSI = signals.get(i).getRssi();
            rssiTextView.setText("RSSI: " +textRSSI+ " dBm");
            // Retrieve emoji for RSSI level
            int emoji = RssiUtils.getRssiEmoji(textRSSI);
            rssiEmojiView.setImageResource(emoji);

            // Map integer RSSI to signal level using mapRssi from RSSILevelModel
            RSSILevelModel rssiLevelModel = RSSILevelModel.mapRssi(rssi);
            int signalLevel = RssiUtils.mapRssiToLevels((int) rssi);

            // Add entry to respective dataset based on RSSI level
            Entry entry = new Entry(i, signalLevel);
            switch (rssiLevelModel) {
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

            rssiDataSet.addEntry(entry);
        }

        // Notify data changed for each dataset
        excellentSet.notifyDataSetChanged();
        goodSet.notifyDataSetChanged();
        fairSet.notifyDataSetChanged();
        weakSet.notifyDataSetChanged();
        terribleSet.notifyDataSetChanged();
        rssiDataSet.notifyDataSetChanged();
        lineData.notifyDataChanged();

        chart.notifyDataSetChanged();
        chart.invalidate();

        chart.setVisibleXRangeMaximum(30);
        chart.moveViewToX(lineData.getEntryCount());
    }
}
