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

import java.util.List;
@RequestMapping("/eduservice/teacher")
@Controller
public class CustomerApi {

    private final CustomerService customerService;

    @Autowired
    public CustomerApi(CustomerService customerService) {
        this.customerService = customerService;
    }

    @RequestMapping(value = "/EveryUserAvatar/{insurance}", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
    @ResponseBody
    public Model avatarEveryUserAvatar(@PathVariable(value = "insurance") String insurance, Model model) {
        List<EveryUserAvatar> everyUserAvatarList = customerService.avatarEveryUserAvatar(insurance);
        model.addAttribute("code", 200);
        model.addAttribute("message", "success");
        model.addAttribute("EveryUserAvatarList", everyUserAvatarList);
        return model;
    }

    @RequestMapping(value = "/EveryUserAge/{insurance}", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
    @ResponseBody
    public Model ageEveryUserAge(@PathVariable(value = "insurance") String insurance, Model model) {
        List<EveryUserAge> everyUserAgeList = customerService.ageEveryUserAge(insurance);
        model.addAttribute("code", 200);
        model.addAttribute("message", "success");
        model.addAttribute("EveryUserAgeList", everyUserAgeList);
        return model;
    }

    @RequestMapping(value = "/EveryUserGender/{insurance}", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
    @ResponseBody
    public Model genderEveryUserGender(@PathVariable(value = "insurance") String insurance, Model model) {
        List<EveryUserGender> everyUserGenderList = customerService.genderEveryUserGender(insurance);
        model.addAttribute("code", 200);
        model.addAttribute("message", "success");
        model.addAttribute("EveryUserGenderList", everyUserGenderList);
        return model;
    }

    @RequestMapping(value = "/EveryUserInjury/{insurance}", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
    @ResponseBody
    public Model injuryEveryUserInjury(@PathVariable(value = "insurance") String insurance, Model model) {
        List<EveryUserInjury> everyUserInjuryList = customerService.injuryEveryUserInjury(insurance);
        model.addAttribute("code", 200);
        model.addAttribute("message", "success");
        model.addAttribute("EveryUserInjuryList", everyUserInjuryList);
        return model;
    }
}
