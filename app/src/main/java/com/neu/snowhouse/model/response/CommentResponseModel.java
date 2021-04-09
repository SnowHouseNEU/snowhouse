package com.neu.snowhouse.model.response;

public class CommentResponseModel {

    private int commentId;
    private String userName;
    private String createdTime;
    private int postId;
    private String content;
    private int likeCount = 0;
    private int dislikeCount = 0;
    private boolean likeClicked = false;
    private boolean dislikeClicked = false;

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getDislikeCount() {
        return dislikeCount;
    }

    public void setDislikeCount(int dislikeCount) {
        this.dislikeCount = dislikeCount;
    }

    public boolean isLikeClicked() {
        return likeClicked;
    }

    public void setLikeClicked(boolean likeClicked) {
        this.likeClicked = likeClicked;
    }

    public boolean isDislikeClicked() {
        return dislikeClicked;
    }

    public void setDislikeClicked(boolean dislikeClicked) {
        this.dislikeClicked = dislikeClicked;
    }
}
