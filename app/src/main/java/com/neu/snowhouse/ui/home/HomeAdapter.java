package com.neu.snowhouse.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.neu.snowhouse.R;
import com.neu.snowhouse.databinding.HomeMountainItemBinding;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder>{

    private ArrayList<MountainData> mountainData;
    private ItemClickListener clickListener;

    public HomeAdapter(ArrayList<MountainData> mountainData, ItemClickListener clickListener) {
        this.mountainData = mountainData;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public HomeAdapter.HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_mountain_item, parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAdapter.HomeViewHolder holder, int position) {
        MountainData data = mountainData.get(position);
        holder.imageView.setImageResource(mountainData.get(position).getImage());
        holder.titleTextView.setText(mountainData.get(position).getTitle());
        holder.descriptionTextView.setText(mountainData.get(position).getDesc());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(mountainData.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mountainData.size();
    }

    static class HomeViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;
        TextView descriptionTextView;

        public HomeViewHolder(View itemView) {
            super(itemView);
            HomeMountainItemBinding binding = HomeMountainItemBinding.bind(itemView);
            imageView = binding.mountainImageView;
            titleTextView = binding.mountainTitle;
            descriptionTextView = binding.mountainDescription;
        }
    }

    public interface ItemClickListener {
        public void onItemClick(MountainData data);
    }
 }
