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

    private MongoCollection<Document> provinceCollection;

    public MongoCollection<Document> getProvinceCollection() {
        if (null == provinceCollection)
            provinceCollection = mongoClient.getDatabase(Constant.MONGODB_DATABASE).getCollection(Constant.MONGODB_VIRTUAL_INSURANCE_COLLECTION);
        return provinceCollection;
    }

    private MongoCollection<Document> industryCollection;

    public MongoCollection<Document> getIndustryCollection() {
        if (null == industryCollection)
            industryCollection = mongoClient.getDatabase(Constant.MONGODB_DATABASE).getCollection(Constant.MONGODB_VIRTUAL_INSURANCE_COLLECTION);
        return industryCollection;
    }

    public List<InsuranceTypeData> getInsuranceByType(String type) {
        // type 就不用了，直接返回全部吧
        List<InsuranceTypeData> result = new ArrayList<>();
        getInsuranceCollection().find().forEach((Block<? super Document>)
                document -> result.add(documentToInsurance(document)));

        if (result.size() == 0) {
            return Collections.emptyList();
        }
        return result;
    }

    public List<ProvinceData> getInsuranceByProvince(String type) {
        List<ProvinceData> result = new ArrayList<>();
        getProvinceCollection().find().forEach((Block<? super Document>)
                document -> result.add(documentToProvince(document)));

        if (result.size() == 0) {
            return Collections.emptyList();
        }
        return result;
    }

    public List<MonthlyData> getInsuranceMonthly(String type) {
        List<MonthlyData> result = new ArrayList<>();
        getInsuranceCollection().find().forEach((Block<? super Document>)
                document -> result.add(documentToMonthlyData(document)));

        if (result.size() == 0) {
            return Collections.emptyList();
        }
        return result;
    }

    public List<IndustryData> getInsuranceByIndustry(String type) {
        List<IndustryData> result = new ArrayList<>();
        getIndustryCollection().find().forEach((Block<? super Document>)
                document -> result.add(documentToIndustry(document)));

        if (result.size() == 0) {
            return Collections.emptyList();
        }
        return result;
    }

    public InsuranceTypeData documentToInsurance(Document document) {
        try {
            return objectMapper.readValue(JSON.serialize(document), InsuranceTypeData.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
            return null;
        } catch (JsonMappingException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public IndustryData documentToIndustry(Document document) {
        try {
            return objectMapper.readValue(JSON.serialize(document), IndustryData.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
            return null;
        } catch (JsonMappingException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ProvinceData documentToProvince(Document document) {
        try {
            return objectMapper.readValue(JSON.serialize(document), ProvinceData.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
            return null;
        } catch (JsonMappingException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public MonthlyData documentToMonthlyData(Document document) {
        try {
            return objectMapper.readValue(JSON.serialize(document), MonthlyData.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
            return null;
        } catch (JsonMappingException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
