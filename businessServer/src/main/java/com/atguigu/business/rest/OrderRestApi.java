package com.atguigu.business.rest;

import com.atguigu.business.model.domain.Order;
import com.atguigu.business.model.request.OrderRequest;
import com.atguigu.business.service.OrderService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;


@RequestMapping("/rest/orders")
@Controller
public class OrderRestApi {

    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "create-order",produces = "application/json",method = RequestMethod.GET)
    @ResponseBody
    public Model createOrder(@RequestParam("uid")int uid,@RequestParam("mid")int mid,Model model){
        //创建订单
        if (orderService.createOrder(new OrderRequest(uid,mid)))
            model.addAttribute("success", true);
        else
            model.addAttribute("success", false);
        return model;
    }
    //删除订单

    //修改订单
    @RequestMapping(value = "update-order", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public Model updateOrder(@RequestParam("uid") int uid, @RequestParam("mid") int mid, @RequestParam("newData") String newData, Model model) {
        if(orderService.upDateOrderByUidAndMid(uid,mid,newData)){
            model.addAttribute("success",true);
        }
        else {
            model.addAttribute("success",false);
        }
        return model;
    }

    @RequestMapping(value = "find-order",produces = "application/json",method = RequestMethod.GET)
    @ResponseBody
    public Model findOrder(@RequestParam("uid")int uid,Model model){
        //查询订单
        List<Order> orders = orderService.findOrder(uid);
        if (orders == null){
            model.addAttribute("success", false);
        } else {
            model.addAttribute("success",true);
        }
        model.addAttribute("order",orders);
        return model;
    }

}
