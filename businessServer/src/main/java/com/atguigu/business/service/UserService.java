package com.atguigu.business.service;

import com.atguigu.business.model.domain.Authentication;
import com.atguigu.business.model.domain.Order;
import com.atguigu.business.model.domain.User;
import com.atguigu.business.model.request.LoginUserRequest;
import com.atguigu.business.model.request.RegisterUserRequest;
import com.atguigu.business.utils.Constant;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.util.JSON;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        getUserCollection().updateOne(Filters.eq("uid", user.getUid()), new Document("$set",new Document("first", user.isFirst())));
        getUserCollection().updateOne(Filters.eq("uid", user.getUid()), new Document("$set",new Document("prefGenres", user.getPrefGenres())));
        return true;
    }

    public boolean updateAuthentication(Authentication authentication) throws JsonProcessingException {
        User user = findAuthenticationByUID(authentication.getUid());
        if (null == user){
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

    public List<User> findUsers(int offset,int limit){
        List<User> result = new ArrayList<>();
        getUserCollection().find().limit(limit).skip(offset).forEach((Block<? super Document>) document -> result.add(documentToUser(document)));
        if (result.size() == 0) {
            return Collections.emptyList();
        }
        return result;
    }

    public Boolean deleteUserByUid(int uid){
        // 根据customer_id和insurance_id字段查找订单文档
        if (getUserCollection().find(Filters.and(Filters.eq("uid", uid))).first() != null){
            DeleteResult deleteResult = getUserCollection().deleteOne(Filters.and(Filters.eq("uid", uid)));
            if (deleteResult.getDeletedCount() > 0){
                System.out.println(deleteResult);
                return true;
            }
        }
        return false;
    }

    public Boolean upDateUserByUid(int uid,  User user) throws IOException {
        // 根据customer_id和insurance_id字段查找订单文档
        Document document = getUserCollection().find(Filters.and(Filters.eq("uid", uid))).first();
        if (document != null) {
            // 将文档转换为Order对象
            User existingOrder = documentToUser(document);
            // 将newData中的数据更新到existingOrder中
            User updatedOrder = updateUserData(existingOrder, user);
            // 将更新后的Order对象转换为Document
            Document updatedDocument = Document.parse(objectMapper.writeValueAsString(updatedOrder));

            System.out.println(updatedDocument.toJson());
            UpdateResult updateResult = getUserCollection().replaceOne(Filters.and(Filters.eq("uid", uid)), updatedDocument);
            return updateResult.getModifiedCount() > 0;
        } else {
            return false;
        }
    }

    private User updateUserData(User existingUser, User newUserData) throws IOException {
        // 将newData中的数据更新到existingOrder中

        // 检查每个字段是否存在并且不为null，如果是，则更新existingOrder
        if (newUserData.getUsername() != null) {
            existingUser.setUsername(newUserData.getUsername());
        }

        if (newUserData.getPrefGenres() != null) {
            existingUser.setPrefGenres(newUserData.getPrefGenres());
        }

        return existingUser;
    }
    public void removeUser(String username){
        getUserCollection().deleteOne(new Document("username",username));
    }

}
