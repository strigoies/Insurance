package com.atguigu.business.service;

import com.atguigu.business.model.domain.EveryUserAge;
import com.atguigu.business.model.domain.EveryUserAvatar;
import com.atguigu.business.model.domain.EveryUserGender;
import com.atguigu.business.model.domain.EveryUserInjury;
import com.atguigu.business.utils.Constant;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.util.JSON;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private MongoClient mongoClient;

    @Autowired
    private ObjectMapper objectMapper;

    private volatile MongoCollection<Document> EveryUserAvatarCollection;
    private volatile MongoCollection<Document> EveryUserGenderCollection;
    private volatile MongoCollection<Document> EveryUserAgeCollection;
    private volatile MongoCollection<Document> EveryUserInjuryCollection;

    public MongoCollection<Document> getEveryUserAvatarCollection() {
        if (EveryUserAvatarCollection == null) {
            synchronized (this) {
                if (EveryUserAvatarCollection == null) {
                    EveryUserAvatarCollection = mongoClient.getDatabase(Constant.MONGODB_DATABASE).getCollection(Constant.MONGODB_EveryUserAvatar_COLLECTION);
                }
            }
        }
        return EveryUserAvatarCollection;
    }

    public MongoCollection<Document> getEveryUserGenderCollection() {
        if (EveryUserGenderCollection == null) {
            synchronized (this) {
                if (EveryUserGenderCollection == null) {
                    EveryUserGenderCollection = mongoClient.getDatabase(Constant.MONGODB_DATABASE).getCollection(Constant.MONGODB_EveryUserGender_COLLECTION);
                }
            }
        }
        return EveryUserGenderCollection;
    }

    public MongoCollection<Document> getEveryUserAgeCollection() {
        if (EveryUserAgeCollection == null) {
            synchronized (this) {
                if (EveryUserAgeCollection == null) {
                    EveryUserAgeCollection = mongoClient.getDatabase(Constant.MONGODB_DATABASE).getCollection(Constant.MONGODB_EveryUserAge_COLLECTION);
                }
            }
        }
        return EveryUserAgeCollection;
    }

    public MongoCollection<Document> getEveryUserInjuryCollection() {
        if (EveryUserInjuryCollection == null) {
            synchronized (this) {
                if (EveryUserInjuryCollection == null) {
                    EveryUserInjuryCollection = mongoClient.getDatabase(Constant.MONGODB_DATABASE).getCollection(Constant.MONGODB_EveryUserInjury_COLLECTION);
                }
            }
        }
        return EveryUserInjuryCollection;
    }


    private EveryUserAvatar documentToEveryUserAvatar(Document document) throws IOException {
        return objectMapper.readValue(JSON.serialize(document), EveryUserAvatar.class);
    }

    private EveryUserGender documentToEveryUserGender(Document document) throws IOException {
        return objectMapper.readValue(JSON.serialize(document), EveryUserGender.class);
    }

    private EveryUserAge documentToEveryUserAge(Document document) throws IOException {
        return objectMapper.readValue(JSON.serialize(document), EveryUserAge.class);
    }

    private EveryUserInjury documentToEveryUserInjury(Document document) throws IOException {
        return objectMapper.readValue(JSON.serialize(document), EveryUserInjury.class);
    }

    public List<EveryUserAvatar> avatarEveryUserAvatar(String insurance) {
        FindIterable<Document> documents = getEveryUserAvatarCollection().find(Filters.in("insurance", insurance));
        List<EveryUserAvatar> everyUserAvatarsList = new ArrayList<>();
        for (Document document : documents) {
            try {
                everyUserAvatarsList.add(documentToEveryUserAvatar(document));
            } catch (IOException e) {
                // 处理异常，可以抛出自定义异常或记录日志
                e.printStackTrace();
            }
        }
        return everyUserAvatarsList;
    }

    public List<EveryUserAge> ageEveryUserAge(String insurance) {
        FindIterable<Document> documents = getEveryUserAgeCollection().find(Filters.in("insurance", insurance));
        List<EveryUserAge> everyUserAgesList = new ArrayList<>();
        for (Document document : documents) {
            try {
                everyUserAgesList.add(documentToEveryUserAge(document));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return everyUserAgesList;
    }

    public List<EveryUserGender> genderEveryUserGender(String insurance) {
        FindIterable<Document> documents = getEveryUserGenderCollection().find(Filters.in("insurance", insurance));
        List<EveryUserGender> everyUserGendersList = new ArrayList<>();
        for (Document document : documents) {
            try {
                everyUserGendersList.add(documentToEveryUserGender(document));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return everyUserGendersList;
    }

    public List<EveryUserInjury> injuryEveryUserInjury(String insurance) {
        FindIterable<Document> documents = getEveryUserInjuryCollection().find(Filters.in("insurance", insurance));
        List<EveryUserInjury> everyUserInjuriesList = new ArrayList<>();
        for (Document document : documents) {
            try {
                everyUserInjuriesList.add(documentToEveryUserInjury(document));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return everyUserInjuriesList;
    }
}
