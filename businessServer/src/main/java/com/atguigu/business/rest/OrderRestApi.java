package com.atguigu.business.rest;

import com.atguigu.business.model.domain.Order;
import com.atguigu.business.model.request.OrderRequest;
import com.atguigu.business.model.request.UpdateRequest;
import com.atguigu.business.service.OrderService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@RequestMapping("/rest/orders")
@Controller
public class OrderRestApi {

    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "create-order", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public Model createOrder(@RequestParam("uid") int uid, @RequestParam("mid") int mid, Model model) {
        //创建订单
        if (orderService.createOrder(new OrderRequest(uid, mid))) model.addAttribute("success", true);
        else model.addAttribute("success", false);
        return model;
    }

    // 删除订单
    @RequestMapping(value = "delete-order", produces = "application/json", method = RequestMethod.DELETE)
    @ResponseBody
    public Model deleteOrder(@RequestParam("uid") int uid, @RequestParam("mid") int mid,  Model model) {
        if (orderService.deleteOrderByUid(uid, mid)) {
            model.addAttribute("success", true);
        } else {
            model.addAttribute("success", false);
        }
        return model;
    }

    // 更新订单
    @RequestMapping(value = "update-order", produces = "application/json", method = RequestMethod.POST)
    @ResponseBody
    public Model updateOrder(@RequestBody UpdateRequest request, Model model) {
        int uid = request.getUid();
        int mid = request.getMid();
        Order newData = request.getNewData();
        if (orderService.upDateOrderByUidAndMid(uid, mid, newData)) {
            model.addAttribute("success", true);
        } else {
            model.addAttribute("success", false);
        }
        return model;
    }



    @RequestMapping(value = "find-order", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public Model findOrder(@RequestParam("uid") int uid, Model model) {
        //查询订单
        List<Order> orders = orderService.findOrder(uid);
        if (orders == null) {
            model.addAttribute("success", false);
        } else {
            model.addAttribute("success", true);
        }
        model.addAttribute("order", orders);
        return model;
    }

    @RequestMapping(value = "order-percentum", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public Model orderPercentum(Model model) {
        //获取订单数量
        Map<String, String> midCounts = orderService.getInsuranceNamePercentages();
        if (midCounts != null) {
            model.addAttribute("success", true);
            // 打印每个mid的数量
//            for (Map.Entry<String, String> entry : midCounts.entrySet()) {
//                System.out.println("Mid: " + entry.getKey() + ", Count: " + entry.getValue());
//            }
        } else {
            model.addAttribute("success", false);
        }
        model.addAttribute("percentum", midCounts);
        return model;
    }
}
