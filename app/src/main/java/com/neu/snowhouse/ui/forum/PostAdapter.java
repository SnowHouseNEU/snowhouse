package com.neu.snowhouse.ui.forum;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.neu.snowhouse.R;
import com.neu.snowhouse.model.response.LitePostResponseModel;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    ArrayList<LitePostResponseModel> posts;

    public PostAdapter(ArrayList<LitePostResponseModel> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forum_post_item, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        holder.title.setText(posts.get(position).getTitle());
        holder.likeCount.setText(String.valueOf(posts.get(position).getLikeCount()));
        holder.dislikeCount.setText(String.valueOf(posts.get(position).getDislikeCount()));
        holder.userName.setText(posts.get(position).getUserName());
        holder.createdTime.setText(posts.get(position).getCreatedTime());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.itemView.setBackgroundColor(Color.parseColor("#D3D3D3"));
                Bundle bundle = new Bundle();
                bundle.putInt("postId", posts.get(position).getPostId());
                Navigation.findNavController(v).navigate(R.id.forum_post, bundle);
            }
        });
    }

    public void updateAdapter(ArrayList<LitePostResponseModel> newPosts) {
        this.posts = newPosts;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView likeCount;
        TextView dislikeCount;
        TextView userName;
        TextView createdTime;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_post_title);
            likeCount = itemView.findViewById(R.id.item_post_likeCount);
            dislikeCount = itemView.findViewById(R.id.item_post_dislikeCount);
            userName = itemView.findViewById(R.id.item_post_userName);
            createdTime = itemView.findViewById(R.id.item_post_createdTime);
        }
    }
}
