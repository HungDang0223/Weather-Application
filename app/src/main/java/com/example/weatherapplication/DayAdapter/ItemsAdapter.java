package com.example.weatherapplication.DayAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapplication.R;

import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    private List<Item> items;

    public ItemsAdapter(List<Item> items) {
        this.items = items;
    }
    public void addItem(Item newItem) {
        items.add(newItem);
        notifyItemInserted(items.size() - 1); // Notify adapter about the new item added
    }

    @NonNull
    @Override
    public ItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsAdapter.ViewHolder holder, int position) {
        Item item = items.get(position);
        holder.hour.setText(item.getHour());
        int resourceId = holder.itemView.getContext().getResources().getIdentifier(item.getIcon(), "drawable", holder.itemView.getContext().getPackageName());
        holder.icon.setImageResource(resourceId);
        holder.temp.setText(item.getTemp());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView hour;
        public ImageView icon;
        public TextView temp;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            hour =itemView.findViewById(R.id.hour);
            icon = itemView.findViewById(R.id.icon);
            temp = itemView.findViewById(R.id.temperature);
        }
    }
}
