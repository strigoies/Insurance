package com.atguigu.business.rest;

import com.atguigu.business.model.domain.Authentication;
import com.atguigu.business.model.domain.Order;
import com.atguigu.business.model.domain.User;
import com.atguigu.business.model.request.LoginUserRequest;
import com.atguigu.business.model.request.RegisterUserRequest;
import com.atguigu.business.model.request.UpdateRequest;
import com.atguigu.business.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.jws.soap.SOAPBinding;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@RequestMapping("/rest/users")
@Controller
public class UserRestApi {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", produces = "application/json", method = RequestMethod.GET )
    @ResponseBody
    public Model login(@RequestParam("username") String username, @RequestParam("password") String password, Model model) {
        User user  =userService.loginUser(new LoginUserRequest(username,password));
        model.addAttribute("success",user != null);
        model.addAttribute("user",user);
        System.out.println(username);
        return model;
    }

    @RequestMapping(value = "/register", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public Model addUser(@RequestParam("username") String username,@RequestParam("password") String password,Model model) {
        if(userService.checkUserExist(username)){
            model.addAttribute("success",false);
            model.addAttribute("message"," 用户名已经被注册！");
            return model;
        }
        model.addAttribute("success",userService.registerUser(new RegisterUserRequest(username,password)));
        return model;
    }

    //冷启动问题
    //用户喜好存储
    @RequestMapping(value = "/store-prefer", produces = "application/json", method = RequestMethod.POST)
    @ResponseBody
    public Model addPrefGenres(@RequestBody User userOld,Model model) throws UnsupportedEncodingException {
        User userNew = userService.findByUID(userOld.getUid());
        userNew.setPrefGenres(userOld.getPrefGenres());
        userNew.setFirst(false);
        model.addAttribute("success",userService.updateUser(userNew));
        return model;
    }

    //实名信息
    @RequestMapping(value = "/real-name",produces = "application/json",method = RequestMethod.POST)
    public  Model authentication(@RequestBody Authentication authentication,Model model) throws JsonProcessingException {
        //将json存到mongodb
        model.addAttribute("success",userService.updateAuthentication(authentication));
        return model;
    }

    //用户查找
    @RequestMapping(value = "find-user", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public Model findOrder(@RequestParam("offset") int offset,@RequestParam("limit") int limit, Model model) {
        //查询订单
        List<User> users = userService.findUsers(offset,limit);
        if (users == null) {
            model.addAttribute("false", false);
        } else {
            model.addAttribute("success", true);
        }
        model.addAttribute("order", users);
        return model;
    }
    //用户删除
    @RequestMapping(value = "delete-user", produces = "application/json", method = RequestMethod.DELETE)
    @ResponseBody
    public Model deleteOrder(@RequestParam("uid") int uid,  Model model) {
        if (userService.deleteUserByUid(uid)) {
            model.addAttribute("success", true);
        } else {
            model.addAttribute("false", false);
        }
        return model;
    }
    //用户修改
    @RequestMapping(value = "update-user", produces = "application/json", method = RequestMethod.POST)
    @ResponseBody
    public Model updateOrder(@RequestBody UpdateRequest request, Model model) throws IOException {
        int uid = request.getUid();
        if (uid == 0){
            return   model.addAttribute("Invalid uid;", true);
        }
        User newUser = request.getNewUser();
        if (userService.upDateUserByUid(uid, newUser)) {
            return model.addAttribute("User updated successfully", true);
        } else {
            return model.addAttribute("User not found", false);
        }
    }
}
