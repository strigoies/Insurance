// ClaimCettlementService.java
package com.atguigu.business.service;

import com.atguigu.business.model.domain.ClaimSettlement;
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

    public MongoCollection<Document> getMovieCollection() {
        if (null == movieCollection)
            movieCollection = mongoClient.getDatabase(Constant.MONGODB_DATABASE).getCollection(Constant.MONGODB_ClaimSettlement_COLLECTION);
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

    public List<ClaimSettlement> avatarEveryAvatarInjuryAll() {
        FindIterable<Document> documents = getMovieCollection().find();
        List<ClaimSettlement> claimSettlementList = new ArrayList<>();
        for (Document document : documents) {
            claimSettlementList.add(documentToClaimSettlement(document));
        }
        return claimSettlementList;
    }


}
