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

    ArrayList<MountainData> mountainData;

    public HomeAdapter(ArrayList<MountainData> mountainData) {
        this.mountainData = mountainData;
    }

    @NonNull
    @Override
    public HomeAdapter.HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_mountain_item, parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAdapter.HomeViewHolder holder, int position) {
        holder.imageView.setImageResource(mountainData.get(position).getImage());
        holder.titleTextView.setText(mountainData.get(position).getTitle());
        holder.descriptionTextView.setText(mountainData.get(position).getDesc());
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
 }
