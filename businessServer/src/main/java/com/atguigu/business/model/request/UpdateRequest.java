package com.atguigu.business.model.request;

import com.atguigu.business.model.domain.Order;
import com.atguigu.business.model.domain.User;

public class UpdateRequest {
    private int uid;
    private int mid;
    private Order newData;

    private User newUser;

    public User getNewUser() {
        return newUser;
    }

    public void setNewUser(User newUser) {
        this.newUser = newUser;
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

    public Order getNewData() {
        return newData;
    }

    public void setNewData(Order newData) {
        this.newData = newData;
    }
}
