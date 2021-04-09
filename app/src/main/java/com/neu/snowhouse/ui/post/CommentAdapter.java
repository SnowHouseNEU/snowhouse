package com.neu.snowhouse.ui.post;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.neu.snowhouse.R;
import com.neu.snowhouse.model.response.CommentResponseModel;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    ArrayList<CommentResponseModel> comments;
    private LikeClickListener listener;

    public CommentAdapter(ArrayList<CommentResponseModel> comments) {
        this.comments = comments;
    }

    public void setListener(LikeClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_comment_item, parent, false);
        return new CommentViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        holder.userName.setText(comments.get(position).getUserName());
        holder.content.setText(comments.get(position).getContent());
        holder.likeCount.setText(String.valueOf(comments.get(position).getLikeCount()));
        holder.dislikeCount.setText(String.valueOf(comments.get(position).getDislikeCount()));
        holder.createdTime.setText(comments.get(position).getCreatedTime());
        holder.thumbUp.setColorFilter(Color.parseColor("#89000000"));
        holder.thumbDown.setColorFilter(Color.parseColor("#89000000"));
        if (comments.get(position).isLikeClicked()) {
            holder.thumbUp.setColorFilter(Color.parseColor("#000099"));
        }
        if (comments.get(position).isDislikeClicked()) {
            holder.thumbDown.setColorFilter(Color.parseColor("#000099"));
        }
    }

    public void updateAdapter(ArrayList<CommentResponseModel> newComments) {
        this.comments = newComments;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        TextView content;
        ImageButton thumbUp;
        ImageButton thumbDown;
        TextView likeCount;
        TextView dislikeCount;
        TextView createdTime;

        public CommentViewHolder(@NonNull View itemView, final LikeClickListener listener) {
            super(itemView);
            userName = itemView.findViewById(R.id.item_comment_userName);
            content = itemView.findViewById(R.id.item_comment_content);
            thumbUp = itemView.findViewById(R.id.item_comment_up);
            thumbDown = itemView.findViewById(R.id.item_comment_down);
            likeCount = itemView.findViewById(R.id.item_comment_likeCount);
            dislikeCount = itemView.findViewById(R.id.item_comment_dislikeCount);
            createdTime = itemView.findViewById(R.id.item_comment_createdTime);
            thumbUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getLayoutPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onIconClick(position, "like");
                        }
                    }
                }
            });
            thumbDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getLayoutPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onIconClick(position, "dislike");
                        }
                    }
                }
            });
        }
    }
}
