package com.atguigu.business.service;

import com.atguigu.business.model.domain.Order;
import com.atguigu.business.service.OrderService;
import com.mongodb.Block;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@RunWith(SpringRunner.class)
public class OrderFindTest {

    @Autowired
    private OrderService orderService;
    @Test
    public void mongodbTest(){
        System.out.println("寻找订单开始");
        List<Order> result = new ArrayList<>();

//        List<Document> orders = getOrderCollection().find(Filters.eq("customer_id", uid)).into(new ArrayList<>());
//        Document order = orderService.getOrderCollection().find(Filters.eq("customer_id","1234")).first();
//        System.out.println("first方法"+order.toJson());
        orderService.getOrderCollection().find(Filters.eq("customer_id", 1234)).forEach((Block<? super Document>) document -> result.add(orderService.documentToOrder(document)));
        orderService.getOrderCollection().find(Filters.eq("customer_id", 1234)).forEach((Block<? super Document>) document -> System.out.println(document.toJson()));
        if (result.size() == 0){
            System.out.println("订单为空");
        }
        System.out.println(result);
//        for (Document order : orders) {
//            System.out.println(order.toJson());
//            result.add(documentToOrder(order));
//        }
//        Document order = getOrderCollection().find(Filters.eq("customer_id",uid)).first();

    }
}
