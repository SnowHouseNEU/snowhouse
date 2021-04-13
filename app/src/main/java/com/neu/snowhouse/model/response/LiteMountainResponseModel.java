package com.neu.snowhouse.model.response;

public class LiteMountainResponseModel {

    private int mountainId;
    private String mountainName;
    private Image image;
    private int rateCount = 0;
    private double rating = 0.0;

    public int getMountainId() {
        return mountainId;
    }

    public void setMountainId(int mountainId) {
        this.mountainId = mountainId;
    }

    public String getMountainName() {
        return mountainName;
    }

    public void setMountainName(String mountainName) {
        this.mountainName = mountainName;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public int getRateCount() {
        return rateCount;
    }

    public void setRateCount(int rateCount) {
        this.rateCount = rateCount;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
