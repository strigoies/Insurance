package com.atguigu.business.rest;

import com.atguigu.business.model.domain.EveryUserAge;
import com.atguigu.business.model.domain.EveryUserAvatar;
import com.atguigu.business.model.domain.EveryUserGender;
import com.atguigu.business.model.domain.EveryUserInjury;
import com.atguigu.business.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;

@RequestMapping("/eduservice/teacher")
@Controller
public class CustomerApi {

    private final CustomerService customerService;

    @Autowired
    public CustomerApi(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping(value = "/EveryUserAvatar/{insurance}", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public Model avatarEveryUserAvatar(@PathVariable(value = "insurance") String insurance, Model model) {
//        try {
//            insurance = new String(insurance.getBytes("iso-8859-1"), "utf-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace(); // 处理异常
//        }
        System.out.println(insurance);
        List<EveryUserAvatar> everyUserAvatarList = customerService.avatarEveryUserAvatar(insurance);

        if (everyUserAvatarList.isEmpty()) {
            model.addAttribute("code", 404); // 404表示资源未找到
            model.addAttribute("message", "List is empty");
        } else {
            model.addAttribute("code", 200);
            model.addAttribute("message", "success");
            model.addAttribute("EveryUserAvatarList", everyUserAvatarList);
        }

        return model;
    }

    @GetMapping(value = "/EveryUserAge/{insurance}", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public Model ageEveryUserAge(@PathVariable(value = "insurance") String insurance, Model model) {
        try {
            insurance = new String(insurance.getBytes("iso-8859-1"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace(); // 处理异常
        }
        System.out.println(insurance);

        List<EveryUserAge> everyUserAgeList = customerService.ageEveryUserAge(insurance);

        if (everyUserAgeList.isEmpty()) {
            model.addAttribute("code", 404); // 404表示资源未找到
            model.addAttribute("message", "List is empty");
        } else {
            model.addAttribute("code", 200);
            model.addAttribute("message", "success");
            model.addAttribute("EveryUserAgeList", everyUserAgeList);
        }

        return model;
    }

    @GetMapping(value = "/EveryUserGender/{insurance}", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public Model genderEveryUserGender(@PathVariable(value = "insurance") String insurance, Model model) {
        try {
            insurance = new String(insurance.getBytes("iso-8859-1"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace(); // 处理异常
        }

        List<EveryUserGender> everyUserGenderList = customerService.genderEveryUserGender(insurance);

        if (everyUserGenderList.isEmpty()) {
            model.addAttribute("code", 404); // 404表示资源未找到
            model.addAttribute("message", "List is empty");
        } else {
            model.addAttribute("code", 200);
            model.addAttribute("message", "success");
            model.addAttribute("EveryUserGenderList", everyUserGenderList);
        }

        return model;
    }

    @GetMapping(value = "/EveryUserInjury/{insurance}", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public Model injuryEveryUserInjury(@PathVariable(value = "insurance") String insurance, Model model) {
        try {
            insurance = new String(insurance.getBytes("iso-8859-1"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace(); // 处理异常
        }

        List<EveryUserInjury> everyUserInjuryList = customerService.injuryEveryUserInjury(insurance);

        if (everyUserInjuryList.isEmpty()) {
            model.addAttribute("code", 404); // 404表示资源未找到
            model.addAttribute("message", "List is empty");
        } else {
            model.addAttribute("code", 200);
            model.addAttribute("message", "success");
            model.addAttribute("EveryUserInjuryList", everyUserInjuryList);
        }

        return model;
    }
}
