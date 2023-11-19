package com.atguigu.business.model.recom;

/**
 * 推荐项目的包装
 */
public class Recommendation {

    // 电影ID
    private int mid;

    // 保险点击量
    private double count;

    public Recommendation(int mid, double count) {
        this.mid = mid;
        this.count = count;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }
}
