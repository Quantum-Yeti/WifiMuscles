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
            int emoji = getRssiEmoji(textRSSI);
            rssiEmojiView.setImageResource(emoji);

            // Map integer RSSI to String
            int signalLevel = ChartViewModel.mapRssiToLevels((int) rssi);

            Entry entry = new Entry(i, signalLevel);

            RSSILevelModel rssiLevelModel = RSSILevelModel.mapRssi(rssi);
            if (Objects.requireNonNull(rssiLevelModel) == RSSILevelModel.EXCELLENT) {
                excellentSet.addEntry(entry);
            } else if (rssiLevelModel == RSSILevelModel.GOOD) {
                goodSet.addEntry(entry);
            } else if (rssiLevelModel == RSSILevelModel.FAIR) {
                fairSet.addEntry(entry);
            } else if (rssiLevelModel == RSSILevelModel.WEAK) {
                weakSet.addEntry(entry);
            } else if (rssiLevelModel == RSSILevelModel.TERRIBLE) {
                terribleSet.addEntry(entry);
            }

            rssiDataSet.addEntry(entry);
        }

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

    private static int getRssiEmoji(int rssi) {
        if (rssi >= -50) {
            return R.drawable.emoji_happy_24;
        }
        else if (rssi >= -70) {
            return R.drawable.emoji_blue_24;
        } else return R.drawable.emoji_red_24;
    }


}
