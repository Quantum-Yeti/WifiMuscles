package me.theoria.wifimuscles.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import me.theoria.wifimuscles.R;
import me.theoria.wifimuscles.data.model.InfoCardItem;

public class ChartInfoCardAdapter extends RecyclerView.Adapter<ChartInfoCardAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(InfoCardItem item, int position, View view);
    }

    private List<InfoCardItem> items;
    private OnItemClickListener itemClickListener;

    public ChartInfoCardAdapter(List<InfoCardItem> items) {
        this.items = items;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_info_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InfoCardItem item = items.get(position);
        holder.bind(item, position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateItems(List<InfoCardItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, valueTextView;
        ImageView iconImageView, emoji;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.infoTitle);
            valueTextView = itemView.findViewById(R.id.infoValue);
            iconImageView = itemView.findViewById(R.id.infoIcon);
            emoji = itemView.findViewById(R.id.emojiIcon);
        }

        public void bind(InfoCardItem item, int position) {
            titleTextView.setText(item.getTitle());
            valueTextView.setText(item.getValue());
            iconImageView.setImageResource(item.getIconResId());

            if (item.getEmojiRes() != 0) {
                emoji.setImageResource(item.getEmojiRes());
                emoji.setVisibility(View.VISIBLE);
            } else {
                emoji.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(v -> {
                if (itemClickListener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    itemClickListener.onItemClick(item, getAdapterPosition(), v);
                }
            });
        }
    }
}

