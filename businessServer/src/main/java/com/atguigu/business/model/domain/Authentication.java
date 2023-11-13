package com.atguigu.business.model.domain;

import javax.xml.crypto.Data;
import javax.xml.soap.SAAJResult;

public class Authentication {

    //实名认证
    private int uid;
    private String insuranceHolderName;
    private String insuranceHolderLicenseType;
    private String insuranceHolderIdNumber;
    private String insuranceHolderPhoneNumber;
    private String insuranceHolderIssue;//TODO：投保时间
    private String insuranceHolderRemark;
    private String insuranceExceptName;
    private String insuranceExceptLicenseType;
    private String insuranceExceptIdNumber;
    private String insuranceExceptPhoneNumber;
    private String relationShip;
    private String insuranceExceptRemark;

    public Authentication() {
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getInsuranceHolderName() {
        return insuranceHolderName;
    }

    public void setInsuranceHolderName(String insuranceHolderName) {
        this.insuranceHolderName = insuranceHolderName;
    }

    public String getInsuranceHolderLicenseType() {
        return insuranceHolderLicenseType;
    }

    public void setInsuranceHolderLicenseType(String insuranceHolderLicenseType) {
        this.insuranceHolderLicenseType = insuranceHolderLicenseType;
    }

    public String getInsuranceHolderIdNumber() {
        return insuranceHolderIdNumber;
    }

    public void setInsuranceHolderIdNumber(String insuranceHolderIdNumber) {
        this.insuranceHolderIdNumber = insuranceHolderIdNumber;
    }

    public String getInsuranceHolderPhoneNumber() {
        return insuranceHolderPhoneNumber;
    }

    public void setInsuranceHolderPhoneNumber(String insuranceHolderPhoneNumber) {
        this.insuranceHolderPhoneNumber = insuranceHolderPhoneNumber;
    }

    public String getInsuranceHolderIssue() {
        return insuranceHolderIssue;
    }

    public void setInsuranceHolderIssue(String insuranceHolderIssue) {
        this.insuranceHolderIssue = insuranceHolderIssue;
    }

    public String getInsuranceHolderRemark() {
        return insuranceHolderRemark;
    }

    public void setInsuranceHolderRemark(String insuranceHolderRemark) {
        this.insuranceHolderRemark = insuranceHolderRemark;
    }

    public String getInsuranceExceptName() {
        return insuranceExceptName;
    }

    public void setInsuranceExceptName(String insuranceExceptName) {
        this.insuranceExceptName = insuranceExceptName;
    }

    public String getInsuranceExceptLicenseType() {
        return insuranceExceptLicenseType;
    }

    public void setInsuranceExceptLicenseType(String insuranceExceptLicenseType) {
        this.insuranceExceptLicenseType = insuranceExceptLicenseType;
    }

    public String getInsuranceExceptIdNumber() {
        return insuranceExceptIdNumber;
    }

    public void setInsuranceExceptIdNumber(String insuranceExceptIdNumber) {
        this.insuranceExceptIdNumber = insuranceExceptIdNumber;
    }

    public String getInsuranceExceptPhoneNumber() {
        return insuranceExceptPhoneNumber;
    }

    public void setInsuranceExceptPhoneNumber(String insuranceExceptPhoneNumber) {
        this.insuranceExceptPhoneNumber = insuranceExceptPhoneNumber;
    }

    public String getRelationShip() {
        return relationShip;
    }

    public void setRelationShip(String relationShip) {
        this.relationShip = relationShip;
    }

    public String getInsuranceExceptRemark() {
        return insuranceExceptRemark;
    }

    public void setInsuranceExceptRemark(String insuranceExceptRemark) {
        this.insuranceExceptRemark = insuranceExceptRemark;
    }
}
