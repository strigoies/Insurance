package com.atguigu.business.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.xml.soap.SAAJResult;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EveryInjuryInsurancePlan {

    private String insurance;

    private String name;
    private int value;

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
