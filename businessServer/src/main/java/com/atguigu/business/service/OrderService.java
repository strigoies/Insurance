package com.atguigu.business.service;

import com.atguigu.business.model.domain.Order;
import com.atguigu.business.model.request.OrderRequest;
import com.atguigu.business.utils.Constant;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.util.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.bson.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
public class OrderService {

    @Autowired
    private MongoClient mongoClient;
    @Autowired
    private MovieService movieService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private MongoCollection<Document> orderCollection;

    public MongoCollection<Document> getOrderCollection(){
        if (null == orderCollection)
            orderCollection = mongoClient.getDatabase(Constant.MONGODB_RECOMMENDER_DATABASE).getCollection(Constant.MONGODB_ORDER_COLLECTION);
        return orderCollection;
    }

    public boolean createOrder(OrderRequest request){
        //TODO:因表结构设计改变，功能还没适配
        Order order = new Order();
        order.setCustomer_id(request.getUid());
        Document insurance = movieService.getMovieCollection().find(Filters.eq("mid",request.getMid())).first();
        order.setAmount(insurance.get("price",0D));
        order.setInsurance_id(insurance.get("mid",0));
        try{
            getOrderCollection().insertOne(Document.parse(objectMapper.writeValueAsString(order)));
            return true;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Order> findOrder(int uid){
        System.out.println("寻找订单开始");
        List<Order> result = new ArrayList<>();

        getOrderCollection().find(Filters.eq("customer_id", uid)).forEach((Block<? super Document>) document -> result.add(documentToOrder(document)));
        if (result.size() == 0){
            return Collections.emptyList();
        }
        return result;
    }

    public Order documentToOrder(Document document){
        try{
            return objectMapper.readValue(JSON.serialize(document),Order.class);
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
