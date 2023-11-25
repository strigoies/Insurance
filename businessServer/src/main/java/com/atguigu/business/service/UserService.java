package com.atguigu.business.service;

import com.atguigu.business.model.domain.Authentication;
import com.atguigu.business.model.domain.User;
import com.atguigu.business.model.request.LoginUserRequest;
import com.atguigu.business.model.request.RegisterUserRequest;
import com.atguigu.business.utils.Constant;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.util.JSON;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.io.IOException;

@Service
public class UserService {

    @Autowired
    private MongoClient mongoClient;
    @Autowired
    private ObjectMapper objectMapper;

    private MongoCollection<Document> userCollection;

    private MongoCollection<Document> getUserCollection(){
        if(null == userCollection)
            userCollection = mongoClient.getDatabase(Constant.MONGODB_RECOMMENDER_DATABASE).getCollection(Constant.MONGODB_USER_COLLECTION);
        return userCollection;
    }

    private MongoCollection<Document> authenticationCollection;
    private MongoCollection<Document> getAuthenticationCollection(){
        if(null == authenticationCollection)
            authenticationCollection = mongoClient.getDatabase(Constant.MONGODB_RECOMMENDER_DATABASE).getCollection(Constant.MONGODB_AUTHENTICATION_COLLECTION);
        return authenticationCollection;
    }

    public boolean registerUser(RegisterUserRequest request){
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setFirst(true);
        user.setTimestamp(System.currentTimeMillis());
        try{
            getUserCollection().insertOne(Document.parse(objectMapper.writeValueAsString(user)));
            return true;
        }catch (JsonProcessingException e){
            e.printStackTrace();
            return false;
        }
    }

    public User loginUser(LoginUserRequest request){
        User user = findByUsername(request.getUsername());
        if(null == user) {
            return null;
        }else if(!user.passwordMatch(request.getPassword())){
            return null;
        }
        return user;
    }

    private User documentToUser(Document document){
        try{
            return objectMapper.readValue(JSON.serialize(document),User.class);
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

    public boolean checkUserExist(String username){
        return null != findByUsername(username);
    }

    //用户名查用户信息
    public User findByUsername(String username){
        Document user = getUserCollection().find(new Document("username",username)).first();
        if(null == user || user.isEmpty())
            return null;
        return documentToUser(user);
    }

    public boolean updateUser(User user){
        getUserCollection().updateOne(Filters.eq("uid", user.getUid()), new Document().append("$set",new Document("first", user.isFirst())));
        getUserCollection().updateOne(Filters.eq("uid", user.getUid()), new Document().append("$set",new Document("prefGenres", user.getPrefGenres())));
        return true;
    }

    public boolean updateAuthentication(Authentication authentication) throws JsonProcessingException {
        User user = findAuthenticationByUID(authentication.getUid());
        if (null == user){
//            Document document = new Document("uid", authentication.getUid())
//                    .append("insuranceHolderName", authentication.getInsuranceHolderName())
//                    .append("insuranceHolderLicenseType", authentication.getInsuranceHolderLicenseType())
//                    .append("insuranceHolderIdNumber", authentication.getInsuranceHolderIdNumber())
//                    .append("insuranceHolderPhoneNumber", authentication.getInsuranceHolderPhoneNumber())
//                    .append("insuranceHolderIssue", authentication.getInsuranceHolderIssue())
//                    .append("insuranceHolderRemark", authentication.getInsuranceHolderRemark())
//                    .append("insuranceExceptName", authentication.getInsuranceExceptName())
//                    .append("insuranceExceptLicenseType", authentication.getInsuranceExceptLicenseType())
//                    .append("insuranceExceptIdNumber", authentication.getInsuranceExceptIdNumber())
//                    .append("insuranceExceptPhoneNumber", authentication.getInsuranceExceptPhoneNumber())
//                    .append("relationShip", authentication.getRelationShip())
//                    .append("insuranceExceptRemark", authentication.getInsuranceExceptRemark());

            // 插入BSON文档到MongoDB集合
            getAuthenticationCollection().insertOne(Document.parse(objectMapper.writeValueAsString(authentication)));
            return true;
        }else{
            return false;
        }
    }

    public User findAuthenticationByUID(int uid){
        Document user = getAuthenticationCollection().find(new Document("uid",uid)).first();
        if(null == user || user.isEmpty())
            return null;
        return documentToUser(user);
    }

    public User findByUID(int uid){
        Document user = getUserCollection().find(new Document("uid",uid)).first();
        if(null == user || user.isEmpty())
            return null;
        return documentToUser(user);
    }

    public void removeUser(String username){
        getUserCollection().deleteOne(new Document("username",username));
    }

}
