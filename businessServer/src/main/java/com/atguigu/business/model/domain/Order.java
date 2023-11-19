package com.atguigu.business.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Order {

    @JsonIgnore
    private String _id;
    private int customer_id;

    private int insurance_id;

    private double amount;

    private String payment_type;
    private String status;
    private String create_time;

    private String code;

//    public boolean isEmpty() {
//        return _id == null && customer_id == 0; // 自定义空值条件
//    }

    public int getInsurance_id() {
        return insurance_id;
    }

    public void setInsurance_id(int insurance_id) {
        this.insurance_id = insurance_id;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
