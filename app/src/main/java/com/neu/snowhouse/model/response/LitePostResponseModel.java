package com.neu.snowhouse.model.response;

public class LitePostResponseModel {
    private int postId;
    private String userName;
    private String createdTime;
    private String title;
    private int likeCount = 0;
    private int dislikeCount = 0;
    private int unReadComments = 0;

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public int getUnReadComments() {
        return unReadComments;
    }

    public void setUnReadComments(int unReadComments) {
        this.unReadComments = unReadComments;
    }
}
