package com.atguigu.business.service;

import com.atguigu.business.model.domain.Order;
import com.atguigu.business.model.domain.User;
import com.atguigu.business.model.request.OrderRequest;
import com.atguigu.business.utils.Constant;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.util.JSON;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.bson.Document;

import java.io.IOException;


@Service
public class OrderService {

    @Autowired
    private MongoClient mongoClient;

    private ObjectMapper objectMapper;

    private MongoCollection<Document> orderCollection;

    private MongoCollection<Document> getOrderCollection(){
        if (null == orderCollection)
            orderCollection = mongoClient.getDatabase(Constant.MONGODB_DATABASE).getCollection(Constant.MONGODB_ORDER_COLLECTION);
        return orderCollection;
    }

    public boolean createOrder(OrderRequest request){
        Order order = new Order();
        MovieService movieService = new MovieService();
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

    public Order findOrder(int uid){
        Document order = getOrderCollection().find(Filters.eq("uid",uid)).first();
        if (null == order || order.isEmpty()){
            return null;
        }
        return documentToOrder(order);
    }

    private Order documentToOrder(Document document){
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
