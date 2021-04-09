package com.neu.snowhouse.ui.inbox;

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

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.InboxViewHolder> {
    ArrayList<LitePostResponseModel> posts;

    public InboxAdapter(ArrayList<LitePostResponseModel> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public InboxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inbox_post_item, parent, false);
        return new InboxViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InboxViewHolder holder, int position) {
        String title = posts.get(position).getTitle();
        int unReadComments = posts.get(position).getUnReadComments();
        String message = "Post: " + title + " has " + unReadComments + " new comments";
        holder.title.setText(message);
        holder.likeCount.setText(String.valueOf(posts.get(position).getLikeCount()));
        holder.dislikeCount.setText(String.valueOf(posts.get(position).getDislikeCount()));
        holder.createdTime.setText(posts.get(position).getCreatedTime());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.itemView.setBackgroundColor(Color.parseColor("#D3D3D3"));
                Bundle bundle = new Bundle();
                bundle.putInt("postId", posts.get(position).getPostId());
                Navigation.findNavController(v).navigate(R.id.inbox_post, bundle);
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

    static class InboxViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView likeCount;
        TextView dislikeCount;
        TextView createdTime;

        public InboxViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_inbox_title);
            likeCount = itemView.findViewById(R.id.item_inbox_likeCount);
            dislikeCount = itemView.findViewById(R.id.item_inbox_dislikeCount);
            createdTime = itemView.findViewById(R.id.item_inbox_createdTime);
        }
    }
}
