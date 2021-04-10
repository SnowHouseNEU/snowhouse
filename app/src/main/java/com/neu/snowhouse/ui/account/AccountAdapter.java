package com.neu.snowhouse.ui.account;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.neu.snowhouse.R;
import com.neu.snowhouse.model.response.LitePostResponseModel;
import com.neu.snowhouse.ui.forum.PostAdapter;

import java.util.ArrayList;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountViewHolder> {
    ArrayList<LitePostResponseModel> posts;

    public AccountAdapter(ArrayList<LitePostResponseModel> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_post_item, parent, false);
        return new AccountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder holder, int position) {
        holder.title.setText(posts.get(position).getTitle());
        holder.likeCount.setText(String.valueOf(posts.get(position).getLikeCount()));
        holder.dislikeCount.setText(String.valueOf(posts.get(position).getDislikeCount()));
        holder.createdTime.setText(String.valueOf(posts.get(position).getCreatedTime()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.itemView.setBackgroundColor(Color.parseColor("#D3D3D3"));
                Bundle bundle = new Bundle();
                bundle.putInt("postId", posts.get(position).getPostId());
                Navigation.findNavController(v).navigate(R.id.account_post, bundle);
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

    static class AccountViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView likeCount;
        TextView dislikeCount;
        TextView createdTime;
        ImageButton deletePost;

        public AccountViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_account_post_title);
            likeCount = itemView.findViewById(R.id.item_account_likeCount);
            dislikeCount = itemView.findViewById(R.id.item_account_dislikeCount);
            createdTime = itemView.findViewById(R.id.item_account_createdTime);
//            deletePost.setOnClickListener();
        }
    }
}
