package com.atguigu.business.model.domain;

/**
 * 按省份的销售与出险数量
 */
public class ProvinceData extends CommonInsuranceData{
    public ProvinceData(String name, int sales, int claims, String year) {
        super(name, sales, claims, year);
    }
}
