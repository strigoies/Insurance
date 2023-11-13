package com.atguigu.business.rest;

import com.atguigu.business.model.domain.Authentication;
import com.atguigu.business.model.domain.User;
import com.atguigu.business.model.request.LoginUserRequest;
import com.atguigu.business.model.request.RegisterUserRequest;
import com.atguigu.business.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    @RequestMapping(value = "/pref", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public Model addPrefGenres(@RequestParam("username") String username,@RequestParam("genres") String genres,Model model) throws UnsupportedEncodingException {
        User user = userService.findByUsername(username);
        genres= new String(genres.getBytes("ISO8859-1"),"UTF-8");
        System.out.println("UserRestApi"+genres);
        user.getPrefGenres().addAll(Arrays.asList(genres.split(",")));
        user.setFirst(false);
        model.addAttribute("success",userService.updateUser(user));
        return model;
    }

    //实名信息
    @RequestMapping(value = "/real-name",produces = "application/json",method = RequestMethod.POST)
    public  Model authentication(@RequestBody Authentication authentication,Model model){
        //将json存到mongodb
        model.addAttribute("success",userService.updateAuthentication(authentication));
        return model;
    }
}
