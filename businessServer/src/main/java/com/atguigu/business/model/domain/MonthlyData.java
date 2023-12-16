package com.atguigu.business.model.domain;

/**
 * 按月份的销售与出险数量
 */
public class MonthlyData extends CommonInsuranceData{
    public MonthlyData(String name, int sales, int claims, String year) {
        super(name, sales, claims, year);
    }
}
