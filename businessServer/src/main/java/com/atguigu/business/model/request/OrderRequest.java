package com.atguigu.business.model.request;

public class OrderRequest {
    private int uid;
    private int mid;

    public OrderRequest(int uid, int mid) {
        this.uid = uid;
        this.mid = mid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }
}
