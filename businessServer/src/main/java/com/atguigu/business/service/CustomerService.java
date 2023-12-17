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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // 查询并统计每个保险类型的和
    public List<EveryUserAvatar> avatarEveryUserAvatar(String insurance) {
        if ("全部".equals(insurance)) {
            List<EveryUserAvatar> allEveryUserAvatars = mapDocumentsToEveryUserAvatarList(getEveryUserAvatarCollection().find());
            return aggregateSumForAllInsurance(allEveryUserAvatars);
        } else {
            FindIterable<Document> documents = getEveryUserAvatarCollection().find(Filters.in("name", insurance));
            return mapDocumentsToEveryUserAvatarList(documents);
        }
    }

    // 统计每个保险类型的和
    private List<EveryUserAvatar> aggregateSumForAllInsurance(List<EveryUserAvatar> allEveryUserAvatars) {
        // 使用Map来聚合每个保险类型的和
        Map<String, EveryUserAvatar> insuranceSumMap = new HashMap<>();

        for (EveryUserAvatar everyUserAvatar : allEveryUserAvatars) {
            String insurance = everyUserAvatar.getInsurance();

            if (insuranceSumMap.containsKey(insurance)) {
                // 如果Map中已经包含该保险类型，累加相应字段的值
                EveryUserAvatar sumAvatar = insuranceSumMap.get(insurance);
                sumAvatar.setValue(sumAvatar.getValue() + everyUserAvatar.getValue());
            } else {
                // 如果Map中不包含该保险类型，将其添加到Map中
                insuranceSumMap.put(insurance, everyUserAvatar);
            }
        }

        // 将Map中的值转换为List并返回
        return new ArrayList<>(insuranceSumMap.values());
    }

    // 查询并统计每个保险类型的和
    public List<EveryUserAge> ageEveryUserAge(String insurance) {
        if ("全部".equals(insurance)) {
            List<EveryUserAge> allEveryUserAges = mapDocumentsToEveryUserAgeList(getEveryUserAgeCollection().find());
            return aggregateSumAgeForAllInsurance(allEveryUserAges);
        } else {
            FindIterable<Document> documents = getEveryUserAgeCollection().find(Filters.in("insurance", insurance));
            return mapDocumentsToEveryUserAgeList(documents);
        }
    }

    // 统计每个保险类型的和
    private List<EveryUserAge> aggregateSumAgeForAllInsurance(List<EveryUserAge> allEveryUserAges) {
        // 使用Map来聚合每个保险类型的和
        Map<String, EveryUserAge> insuranceSumMap = new HashMap<>();

        for (EveryUserAge everyUserAge : allEveryUserAges) {
            String insurance = everyUserAge.getName();

            if (insuranceSumMap.containsKey(insurance)) {
                // 如果Map中已经包含该保险类型，累加相应字段的值
                EveryUserAge sumAge = insuranceSumMap.get(insurance);
                sumAge.setValue(sumAge.getValue() + everyUserAge.getValue());
            } else {
                // 如果Map中不包含该保险类型，将其添加到Map中
                insuranceSumMap.put(insurance, everyUserAge);
            }
        }

        // 将Map中的值转换为List并返回
        return new ArrayList<>(insuranceSumMap.values());
    }

    // 查询保险类型的数据
    public List<EveryUserGender> genderEveryUserGender(String insurance) {
        if ("全部".equals(insurance)) {
            List<EveryUserGender> allEveryUserGenders = mapDocumentsToEveryUserGenderList(getEveryUserGenderCollection().find());
            return aggregateSumGenderForAllInsurance(allEveryUserGenders);
        } else {
            FindIterable<Document> documents = getEveryUserGenderCollection().find(Filters.in("insurance", insurance));
            return mapDocumentsToEveryUserGenderList(documents);
        }
    }

    // 统计每个保险类型的和
    private List<EveryUserGender> aggregateSumGenderForAllInsurance(List<EveryUserGender> allEveryUserGenders) {
        // 使用Map来聚合每个保险类型的和
        Map<String, EveryUserGender> insuranceSumMap = new HashMap<>();

        for (EveryUserGender everyUserGender : allEveryUserGenders) {
            String insurance = everyUserGender.getName();

            if (insuranceSumMap.containsKey(insurance)) {
                // 如果Map中已经包含该保险类型，累加相应字段的值
                EveryUserGender sumGender = insuranceSumMap.get(insurance);
                sumGender.setValue(sumGender.getValue() + everyUserGender.getValue());
            } else {
                // 如果Map中不包含该保险类型，将其添加到Map中
                insuranceSumMap.put(insurance, everyUserGender);
            }
        }

        // 将Map中的值转换为List并返回
        return new ArrayList<>(insuranceSumMap.values());
    }

    // 查询保险类型的数据

    private List<EveryUserAvatar> mapDocumentsToEveryUserAvatarList(FindIterable<Document> documents) {
        List<EveryUserAvatar> everyUserAvatarsList = new ArrayList<>();
        for (Document document : documents) {
            try {
                everyUserAvatarsList.add(documentToEveryUserAvatar(document));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return everyUserAvatarsList;
    }

    private List<EveryUserAge> mapDocumentsToEveryUserAgeList(FindIterable<Document> documents) {
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

    private List<EveryUserGender> mapDocumentsToEveryUserGenderList(FindIterable<Document> documents) {
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

    private List<EveryUserInjury> mapDocumentsToEveryUserInjuryList(FindIterable<Document> documents) {
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

    public List<EveryUserInjury> injuryEveryUserInjury(String insurance) {
        if ("全部".equals(insurance)) {
            List<EveryUserInjury> allEveryUserInjuries = mapDocumentsToEveryUserInjuryList(getEveryUserInjuryCollection().find());
            return aggregateSumInjuryForAllInsurance(allEveryUserInjuries);
        } else {
            FindIterable<Document> documents = getEveryUserInjuryCollection().find(Filters.in("insurance", insurance));
            return mapDocumentsToEveryUserInjuryList(documents);
        }
    }

    // 统计每个保险类型的和
    private List<EveryUserInjury> aggregateSumInjuryForAllInsurance(List<EveryUserInjury> allEveryUserInjuries) {
        // 使用Map来聚合每个保险类型的和
        Map<String, EveryUserInjury> insuranceSumMap = new HashMap<>();

        for (EveryUserInjury everyUserInjury : allEveryUserInjuries) {
            String insurance = everyUserInjury.getName();

            if (insuranceSumMap.containsKey(insurance)) {
                // 如果Map中已经包含该保险类型，累加相应字段的值
                EveryUserInjury sumInjury = insuranceSumMap.get(insurance);
                sumInjury.setValue(sumInjury.getValue() + everyUserInjury.getValue());
            } else {
                // 如果Map中不包含该保险类型，将其添加到Map中
                insuranceSumMap.put(insurance, everyUserInjury);
            }
        }

        // 将Map中的值转换为List并返回
        return new ArrayList<>(insuranceSumMap.values());
    }

}
