package com.atguigu.business.service;

import com.atguigu.business.model.domain.*;
import com.atguigu.business.utils.Constant;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.util.JSON;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
public class VisualizationService {

    @Autowired
    private MongoClient mongoClient;

    private ObjectMapper objectMapper = new ObjectMapper();

    private MongoCollection<Document> insuranceCollection;

    public MongoCollection<Document> getInsuranceCollection() {
        if (null == insuranceCollection)
            insuranceCollection = mongoClient.getDatabase(Constant.MONGODB_DATABASE).getCollection(Constant.MONGODB_VIRTUAL_INSURANCE_COLLECTION);
        return insuranceCollection;
    }

    public MongoCollection<Document> getInsurancePlanCollection() {
        if (null == insuranceCollection)
            insuranceCollection = mongoClient.getDatabase(Constant.MONGODB_DATABASE).getCollection(Constant.MONGODB_VIRTUAL_INSURANCE_PLAN_COLLECTION);
        return insuranceCollection;
    }

    private MongoCollection<Document> provinceCollection;

    public MongoCollection<Document> getProvinceCollection() {
        if (null == provinceCollection)
            provinceCollection = mongoClient.getDatabase(Constant.MONGODB_DATABASE).getCollection(Constant.MONGODB_VIRTUAL_PROVINCE_COLLECTION);
        return provinceCollection;
    }

    private MongoCollection<Document> industryCollection;

    public MongoCollection<Document> getIndustryCollection() {
        if (null == industryCollection)
            industryCollection = mongoClient.getDatabase(Constant.MONGODB_DATABASE).getCollection(Constant.MONGODB_VIRTUAL_INDUSTRY_COLLECTION);
        return industryCollection;
    }

    private MongoCollection<Document> monthlyCollection;

    public MongoCollection<Document> getMonthlyCollection() {
        if (null == monthlyCollection)
            monthlyCollection = mongoClient.getDatabase(Constant.MONGODB_DATABASE).getCollection(Constant.MONGODB_VIRTUAL_MONTHLY_COLLECTION);
        return monthlyCollection;
    }

    public MongoCollection<Document> getMonthlyInjuryCollection() {
        if (null == monthlyCollection)
            monthlyCollection = mongoClient.getDatabase(Constant.MONGODB_DATABASE).getCollection(Constant.MONGODB_VIRTUAL_MONTHLY_INJURY_COLLECTION);
        return monthlyCollection;
    }

    public List<EveryInjuryInsurancePlan> getInsurancePlanByType(String type) {
        // type 就不用了，直接返回全部吧
        List<EveryInjuryInsurancePlan> result = new ArrayList<>();
        getInsurancePlanCollection().find().forEach((Block<? super Document>)
                document -> result.add(documentToEveryInjuryInsurancePlan(document)));

        if (result.size() == 0) {
            return Collections.emptyList();
        }
        return result;
    }

    public List<EveryInjuryInsurance> getInsuranceByType(String type) {
        // type 就不用了，直接返回全部吧
        List<EveryInjuryInsurance> result = new ArrayList<>();
        getInsuranceCollection().find().forEach((Block<? super Document>)
                document -> result.add(documentToEveryInjuryInsurance(document)));

        if (result.size() == 0) {
            return Collections.emptyList();
        }
        return result;
    }

    public List<EveryInjuryInsurance> getInsuranceByProvince(String type) {
        List<EveryInjuryInsurance> result = new ArrayList<>();
        getProvinceCollection().find().forEach((Block<? super Document>)
                document -> result.add(documentToEveryInjuryInsurance(document)));

        if (result.size() == 0) {
            return Collections.emptyList();
        }
        return result;
    }

    public List<EveryInjuryInsurance> getInsuranceMonthly(String type) {
        List<EveryInjuryInsurance> result = new ArrayList<>();
        getMonthlyCollection().find().forEach((Block<? super Document>)
                document -> result.add(documentToEveryInjuryInsurance(document)));

        if (result.size() == 0) {
            return Collections.emptyList();
        }
        return result;
    }
    public List<EveryInjuryInsurance> getInsuranceInjuryMonthly(String type) {
        List<EveryInjuryInsurance> result = new ArrayList<>();
        getMonthlyInjuryCollection().find().forEach((Block<? super Document>)
                document -> result.add(documentToEveryInjuryInsurance(document)));

        if (result.size() == 0) {
            return Collections.emptyList();
        }
        return result;
    }

    public List<EveryInjuryInsurance> getInsuranceByIndustry(String type) {
        List<EveryInjuryInsurance> result = new ArrayList<>();
        getIndustryCollection().find().forEach((Block<? super Document>)
                document -> result.add(documentToEveryInjuryInsurance(document)));

        if (result.size() == 0) {
            return Collections.emptyList();
        }
        return result;
    }

    private EveryInjuryInsurance documentToEveryInjuryInsurance(Document document) {
        EveryInjuryInsurance everyInjuryInsurance = null;
        try {
            everyInjuryInsurance = objectMapper.readValue(JSON.serialize(document), EveryInjuryInsurance.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return everyInjuryInsurance;
    }

    private EveryInjuryInsurancePlan documentToEveryInjuryInsurancePlan(Document document) {
        EveryInjuryInsurancePlan everyInjuryInsurance = null;
        try {
            everyInjuryInsurance = objectMapper.readValue(JSON.serialize(document), EveryInjuryInsurancePlan.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return everyInjuryInsurance;
    }


}
