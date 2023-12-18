package com.atguigu.business.service;

import com.atguigu.business.model.domain.Order;
import com.atguigu.business.model.domain.User;
import com.atguigu.business.model.request.OrderRequest;
import com.atguigu.business.utils.Constant;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.util.JSON;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.bson.Document;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;


@Service
public class OrderService {

    @Autowired
    private MongoClient mongoClient;
    @Autowired
    private MovieService movieService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private MongoCollection<Document> orderCollection;
    private MongoCollection<Document> InsuranceCollection;

    public MongoCollection<Document> getOrderCollection() {
        if (null == orderCollection)
            orderCollection = mongoClient.getDatabase(Constant.MONGODB_RECOMMENDER_DATABASE).getCollection(Constant.MONGODB_ORDER_COLLECTION);
        return orderCollection;
    }

    public MongoCollection<Document> getInsuranceCollection() {
        if (null == InsuranceCollection)
            InsuranceCollection = mongoClient.getDatabase(Constant.MONGODB_DATABASE).getCollection(Constant.MONGODB_INSURSNCE_COLLECTION);
        return InsuranceCollection;
    }

    public boolean createOrder(OrderRequest request) {
        //TODO:因表结构设计改变，功能还没适配
        Order order = new Order();
        order.setCustomer_id(request.getUid());
        Document insurance = movieService.getMovieCollection().find(Filters.eq("mid", request.getMid())).first();
        order.setAmount(insurance.get("price", 0D));
        order.setInsurance_id(insurance.get("mid", 0));
        try {
            getOrderCollection().insertOne(Document.parse(objectMapper.writeValueAsString(order)));
            return true;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Order> findOrder(int uid) {
        List<Order> result = new ArrayList<>();
        getOrderCollection().find(Filters.eq("customer_id", uid)).forEach((Block<? super Document>) document -> result.add(documentToOrder(document)));
        if (result.size() == 0) {
            return Collections.emptyList();
        }
        return result;
    }

    public Order documentToOrder(Document document) {
        try {
            return objectMapper.readValue(JSON.serialize(document), Order.class);
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


    public Boolean upDateOrderByUidAndMid(int uid, int mid, Order newData) {
        // 根据customer_id和insurance_id字段查找订单文档
        Document document = getOrderCollection().find(Filters.and(Filters.eq("customer_id", uid), Filters.eq("insurance_id", mid))).first();
        if (document != null) {
            try {
                // 将文档转换为Order对象
                Order existingOrder = documentToOrder(document);
                // 将newData中的数据更新到existingOrder中
                Order updatedOrder = updateOrderData(existingOrder, newData);
                // 将更新后的Order对象转换为Document
                Document updatedDocument = Document.parse(objectMapper.writeValueAsString(updatedOrder));
                // 使用原来的customer_id和insurance_id更新订单文档
                UpdateResult updateResult = getOrderCollection().replaceOne(Filters.and(Filters.eq("customer_id", uid), Filters.eq("insurance_id", mid)), updatedDocument);
                // 检查是否成功更新
                return updateResult.getModifiedCount() > 0;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    public Boolean deleteOrderByUid(int uid,int mid){
        // 根据customer_id和insurance_id字段查找订单文档
        if (getOrderCollection().find(Filters.and(Filters.eq("customer_id", uid), Filters.eq("insurance_id", mid))).first() != null){
            DeleteResult deleteResult = getOrderCollection().deleteOne(Filters.and(Filters.eq("customer_id", uid), Filters.eq("insurance_id", mid)));
            if (deleteResult.getDeletedCount() > 0){
                System.out.println(deleteResult);
                return true;
            }
        }
        return false;
    }

    private Order updateOrderData(Order existingOrder, Order newOrderData) throws IOException {
        // 将newData中的数据更新到existingOrder中

        // 检查每个字段是否存在并且不为null，如果是，则更新existingOrder
        if (newOrderData.getAmount() != 0) {
            existingOrder.setAmount(newOrderData.getAmount());
        }

        if (newOrderData.getPayment_type() != null) {
            existingOrder.setPayment_type(newOrderData.getPayment_type());
        }

        if (newOrderData.getStatus() != null) {
            existingOrder.setStatus(newOrderData.getStatus());
        }

        if (newOrderData.getCreate_time() != null) {
            existingOrder.setCreate_time(newOrderData.getCreate_time());
        }

        if (newOrderData.getCode() != null) {
            existingOrder.setCode(newOrderData.getCode());
        }

        return existingOrder;
    }

    //获取购买所占百分比
    public Map<String, String> getInsuranceNamePercentages() {
        Map<String, Integer> insuranceCounts = new HashMap<>();
        Map<String, String> insurancePercentages = new HashMap<>();

        // 执行聚合操作
        List<Document> results = getOrderCollection().aggregate(Arrays.asList(Aggregates.group("$insurance_id", Accumulators.sum("count", 1)))).into(new ArrayList<>());
        long totalCount = getOrderCollection().count();

        for (Document result : results) {
            int insuranceId = result.getInteger("_id");

            // 在保险集合中查找匹配的记录
            Document insuranceDocument = getInsuranceCollection().find(Filters.eq("mid", insuranceId)).first();

            if (insuranceDocument != null) {
                String insuranceName = insuranceDocument.getString("descri");
                int count = result.getInteger("count");

                //TODO:因为数据库中的保险名字有重复，为测试使用，后期删掉
                // 检查insuranceName是否已经存在于Map中
                if (insuranceCounts.containsKey(insuranceName)) {
                    // 如果存在，给insuranceName后面添加一个随机数字
                    insuranceName = insuranceName + "_" + new Random().nextInt(1000); // 根据需要调整范围
                }

                insuranceCounts.put(insuranceName, count);
            }
        }


        // 使用 DecimalFormat 保留两位有效数字
        DecimalFormat df = new DecimalFormat("#.##");

        // 计算百分比并保留两位有效数字
        for (Map.Entry<String, Integer> entry : insuranceCounts.entrySet()) {
            String insuranceName = entry.getKey();
//            int id=entry.getKey();
            int count = entry.getValue();
//            System.out.println("Insurance Name: " + insuranceName + ", Count: " + count);
            double percentage = (count / (double) totalCount) * 100;
            String formattedPercentage = df.format(percentage);
            insurancePercentages.put(insuranceName, formattedPercentage);
        }
        return insurancePercentages;
    }

    // 分页查订单
    public List<Order> findUsers(int offset, int limit){
        List<Order> result = new ArrayList<>();
        getOrderCollection().find().limit(limit).skip(offset).forEach((Block<? super Document>) document -> result.add(documentToOrder(document)));
        if (result.size() == 0) {
            return Collections.emptyList();
        }
        return result;
    }

}
