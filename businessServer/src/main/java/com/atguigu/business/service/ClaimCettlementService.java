// ClaimCettlementService.java
package com.atguigu.business.service;

import com.atguigu.business.model.domain.ClaimSettlement;
import com.atguigu.business.model.domain.EveryInjuryInsurance;
import com.atguigu.business.model.domain.InsuranceBeInjury;
import com.atguigu.business.utils.Constant;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.util.JSON;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClaimCettlementService {

    @Autowired
    private MongoClient mongoClient;

    @Autowired
    private ObjectMapper objectMapper;

    private MongoCollection<Document> movieCollection;

    public MongoCollection<Document> getClaimsCollection() {
        if (null == movieCollection)
            movieCollection = mongoClient.getDatabase(Constant.MONGODB_DATABASE).getCollection(Constant.MONGODB_ClaimSettlement_COLLECTION);
        return movieCollection;
    }

    public MongoCollection<Document> getAvatarInjuryCollection() {
        if (null == movieCollection)
            movieCollection = mongoClient.getDatabase(Constant.MONGODB_DATABASE).getCollection(Constant.MONGODB_AvatarInjury_COLLECTION);
        return movieCollection;
    }
    public MongoCollection<Document> getEveryInsuranceCollection() {
        if (null == movieCollection)
            movieCollection = mongoClient.getDatabase(Constant.MONGODB_DATABASE).getCollection(Constant.MONGODB_EveryInjuryInsurance_COLLECTION);
        return movieCollection;
    }

    private ClaimSettlement documentToClaimSettlement(Document document) {
        ClaimSettlement claimSettlement = null;
        try {
            claimSettlement = objectMapper.readValue(JSON.serialize(document), ClaimSettlement.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return claimSettlement;
    }
    private InsuranceBeInjury documentToInsuranceBeInjury(Document document) {
        InsuranceBeInjury insuranceBeInjury = null;
        try {
            insuranceBeInjury = objectMapper.readValue(JSON.serialize(document), InsuranceBeInjury.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return insuranceBeInjury;
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
    public List<ClaimSettlement> avatarEveryAvatarInjuryAll() {
        FindIterable<Document> documents = getClaimsCollection().find();
        List<ClaimSettlement> claimSettlementList = new ArrayList<>();
        for (Document document : documents) {
            claimSettlementList.add(documentToClaimSettlement(document));
        }
        return claimSettlementList;
    }

    public List<InsuranceBeInjury> insuranceEveryInsuranceBeInjury() {
        FindIterable<Document> documents = getAvatarInjuryCollection().find();
        List<InsuranceBeInjury> claimSettlementList = new ArrayList<>();
        for (Document document : documents) {
            claimSettlementList.add(documentToInsuranceBeInjury(document));
        }
        return claimSettlementList;
    }
    public List<EveryInjuryInsurance> insuranceEveryInsurance() {
        FindIterable<Document> documents = getEveryInsuranceCollection().find();
        List<EveryInjuryInsurance> everyInjuryInsuranceList = new ArrayList<>();
        for (Document document : documents) {
            everyInjuryInsuranceList.add(documentToEveryInjuryInsurance(document));
        }
        return everyInjuryInsuranceList;
    }


}
