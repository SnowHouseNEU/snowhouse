package com.neu.snowhouse.model;

public class PostResponseModel {
    private int postId;
    private String userName;
    private String createdTime;
    private String title;
    private String content;
    private Image image;
    private String tag1;
    private String tag2;
    private String tag3;
    private int likeCount = 0;
    private int dislikeCount = 0;
    private boolean likeClicked = false;
    private boolean dislikeClicked = false;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getTag1() {
        return tag1;
    }

    public void setTag1(String tag1) {
        this.tag1 = tag1;
    }

    public String getTag2() {
        return tag2;
    }

    public void setTag2(String tag2) {
        this.tag2 = tag2;
    }

    public String getTag3() {
        return tag3;
    }

    public void setTag3(String tag3) {
        this.tag3 = tag3;
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
