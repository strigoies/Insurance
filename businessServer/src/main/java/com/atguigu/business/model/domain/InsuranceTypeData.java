package com.atguigu.business.model.domain;
/**
 * 按保险种类与方案的销售与出险数量
 */
public class InsuranceTypeData extends CommonInsuranceData{
    public String plan;

    public InsuranceTypeData(String name, int sales, int claims, String year) {
        super(name, sales, claims, year);
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }
}
