package me.theoria.wifimuscles.view.adapters;

import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import me.theoria.wifimuscles.R;
import me.theoria.wifimuscles.utils.RSSIUtils;

public class WifiListAdapter extends RecyclerView.Adapter<WifiListAdapter.WifiViewHolder> {

    private List<ScanResult> wifiList = new ArrayList<>();

    public void setWifiList(List<ScanResult> list) {
        this.wifiList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WifiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_wifi_list, parent, false);
        return new WifiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WifiViewHolder holder, int position) {
        ScanResult result = wifiList.get(position);
        String ssid = (result.SSID == null || result.SSID.trim().isEmpty()) ? "<Hidden SSID>" : result.SSID;
        holder.ssidView.setText(ssid);

        int rssi = result.level;
        
        int emojiRes = RSSIUtils.getRssiEmoji(rssi);
        holder.signalIcon.setImageResource(emojiRes);

        holder.rssiView.setText("Signal: " + result.level + " dBm");
    }

    @Override
    public int getItemCount() {
        return wifiList.size();
    }

    public static class WifiViewHolder extends RecyclerView.ViewHolder {
        public ImageView signalIcon;
        TextView ssidView, rssiView;

        WifiViewHolder(@NonNull View itemView) {
            super(itemView);
            ssidView = itemView.findViewById(R.id.ssidTextView);
            signalIcon = itemView.findViewById(R.id.signalIcon);
            rssiView = itemView.findViewById(R.id.rssiTextView);
        }
    }
}

