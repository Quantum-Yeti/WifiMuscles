package me.theoria.wifimuscles.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import me.theoria.wifimuscles.R;
import me.theoria.wifimuscles.data.model.SupportModel;

public class SupportAdapter extends RecyclerView.Adapter<SupportAdapter.ViewHolder> {

    private final List<SupportModel> tips;
    private final OnItemClickListener clickListener;

    public interface OnItemClickListener {
        void onItemClick(SupportModel tip);
    }

    public SupportAdapter(List<SupportModel> tips, OnItemClickListener listener) {
        this.tips = tips;
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_support_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SupportModel tip = tips.get(position);
        holder.bind(tip, clickListener);
    }

    @Override
    public int getItemCount() {
        return tips.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleView;
        private final TextView descView;

        ViewHolder(View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.supportTitle);
            descView = itemView.findViewById(R.id.supportDescription);
        }

        void bind(SupportModel tip, OnItemClickListener listener) {
            titleView.setText(tip.getTitle());
            descView.setText(tip.getDescription());
            itemView.setOnClickListener(v -> {
                listener.onItemClick(tip);
            });
        }
    }
}
