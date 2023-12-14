package com.atguigu.business.model.domain;

public class CommonInsuranceData {
    private String name;
    private int sales;
    private int claims;
    private String year;

    // 省略构造函数、getter和setter方法

    public CommonInsuranceData(String name, int sales, int claims, String year) {
        this.name = name;
        this.sales = sales;
        this.claims = claims;
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public int getClaims() {
        return claims;
    }

    public void setClaims(int claims) {
        this.claims = claims;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}