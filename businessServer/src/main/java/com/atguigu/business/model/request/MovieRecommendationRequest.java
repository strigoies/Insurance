package com.atguigu.business.model.request;

public class MovieRecommendationRequest {
    private int mid;

    private int sum;

    private String descri;

    public MovieRecommendationRequest(int mid, int sum) {
        this.mid = mid;
        this.sum = sum;
    }

    public MovieRecommendationRequest(String descri, int sum) {
        this.descri = descri;
        this.sum = sum;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }


    public String getDescri() {
        return descri;
    }

    public void setDescri(String descri) {
        this.descri = descri;
    }
}
