package com.atguigu.business.model.request;

import com.atguigu.business.model.domain.Order;

public class UpdateRequest {
    private int uid;
    private int mid;
    private Order newData;

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

    public Order getNewData() {
        return newData;
    }

    public void setNewData(Order newData) {
        this.newData = newData;
    }
}
