package me.theoria.wifimuscles.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import me.theoria.wifimuscles.R;
import me.theoria.wifimuscles.data.model.InfoCardItem;

public class InfoCardAdapter extends RecyclerView.Adapter<InfoCardAdapter.ViewHolder> {
    private List<InfoCardItem> items;

    public InfoCardAdapter(List<InfoCardItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate your item layout and create ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_info_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InfoCardItem item = items.get(position);
        holder.titleTextView.setText(item.getTitle());
        holder.valueTextView.setText(item.getValue());
        holder.iconImageView.setImageResource(item.getIconResId());

        if (item.getEmojiRes() != 0) {
            holder.emoji.setImageResource(item.getEmojiRes());
            holder.emoji.setVisibility(View.VISIBLE);
        } else {
            holder.emoji.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // Method to update the list of items and notify changes
    public void updateItems(List<InfoCardItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, valueTextView;
        ImageView iconImageView, emoji;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.infoTitle);
            valueTextView = itemView.findViewById(R.id.infoValue);
            iconImageView = itemView.findViewById(R.id.infoIcon);
            emoji = itemView.findViewById(R.id.emojiIcon);
        }
    }
}
