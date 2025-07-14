package me.theoria.wifimuscles.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import me.theoria.wifimuscles.R;
import me.theoria.wifimuscles.data.model.StatsInfoCardModel;

public class StatsAdapter extends RecyclerView.Adapter<StatsAdapter.StatViewHolder> {

    private final List<StatsInfoCardModel> items = new ArrayList<>();

    public void updateItems(List<StatsInfoCardModel> newItems) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new StatsDiffCallback(items, newItems));
        items.clear();
        items.addAll(newItems);
        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public StatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_stats, parent, false);
        return new StatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatViewHolder holder, int position) {
        StatsInfoCardModel item = items.get(position);
        holder.label.setText(item.getLabel());
        holder.value.setText(item.getValue());

        // On Click null check
        if (item.getOnClickListener() != null) {
            holder.itemView.setOnClickListener(item.getOnClickListener());
        } else {
            holder.itemView.setOnClickListener(null);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class StatViewHolder extends RecyclerView.ViewHolder {
        TextView label, value;

        StatViewHolder(View itemView) {
            super(itemView);
            label = itemView.findViewById(R.id.stat_label);
            value = itemView.findViewById(R.id.stat_value);
        }
    }

    static class StatsDiffCallback extends DiffUtil.Callback {
        private final List<StatsInfoCardModel> oldList;
        private final List<StatsInfoCardModel> newList;

        StatsDiffCallback(List<StatsInfoCardModel> oldList, List<StatsInfoCardModel> newList) {
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
            return oldList.get(oldItemPosition).getLabel()
                    .equals(newList.get(newItemPosition).getLabel());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
        }
    }
}
