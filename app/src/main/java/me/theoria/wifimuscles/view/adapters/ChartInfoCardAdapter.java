package me.theoria.wifimuscles.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import me.theoria.wifimuscles.R;
import me.theoria.wifimuscles.data.model.ChartInfoCardModel;
import me.theoria.wifimuscles.data.model.StatsInfoCardModel;

public class ChartInfoCardAdapter extends RecyclerView.Adapter<ChartInfoCardAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(ChartInfoCardModel item, int position, View view);
    }

    private List<ChartInfoCardModel> items;
    private OnItemClickListener itemClickListener;

    public ChartInfoCardAdapter(List<ChartInfoCardModel> items) {
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
        ChartInfoCardModel item = items.get(position);
        holder.bind(item, position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateItems(List<ChartInfoCardModel> newItems) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(
            new ChartDiffCallback(this.items, newItems)
                );

        this.items = newItems;
        diffResult.dispatchUpdatesTo(this);
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

        public void bind(ChartInfoCardModel item, int position) {
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

    static class ChartDiffCallback extends DiffUtil.Callback {
        private final List<ChartInfoCardModel> oldList;
        private final List<ChartInfoCardModel> newList;

        public ChartDiffCallback(List<ChartInfoCardModel> oldList, List<ChartInfoCardModel> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).getTitle()
                    .equals(newList.get(newItemPosition).getTitle());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
        }
    }
}

