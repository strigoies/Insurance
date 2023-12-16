package com.atguigu.business.model.domain;

/**
 * 按行业的销售与出险数量
 */
public class IndustryData extends CommonInsuranceData{
    public IndustryData(String name, int sales, int claims, String year) {
        super(name, sales, claims, year);
    }
}
