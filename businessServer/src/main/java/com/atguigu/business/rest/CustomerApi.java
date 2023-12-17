package com.atguigu.business.rest;

import com.atguigu.business.model.domain.EveryUserAge;
import com.atguigu.business.model.domain.EveryUserAvatar;
import com.atguigu.business.model.domain.EveryUserGender;
import com.atguigu.business.model.domain.EveryUserInjury;
import com.atguigu.business.model.request.AgeEveryUserAge;
import com.atguigu.business.model.request.AvatarEveryUserAvatar;
import com.atguigu.business.model.request.GenderEveryUserGender;
import com.atguigu.business.model.request.InjuryEveryUserInjury;
import com.atguigu.business.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

@RequestMapping("/eduservice/teacher")
@Controller
public class CustomerApi {

    private final CustomerService customerService;

    @Autowired
    public CustomerApi(CustomerService customerService) {
        this.customerService = customerService;
    }

    @RequestMapping(value = "/EveryUserAvatar", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Model avatarEveryUserAvatar(@RequestBody AvatarEveryUserAvatar avatarEveryUserAvatar, Model model) {
        String insurance = avatarEveryUserAvatar.getInsurance();
        List<EveryUserAvatar> everyUserAvatarList = customerService.avatarEveryUserAvatar(insurance);

        if (everyUserAvatarList.isEmpty()) {
            model.addAttribute("code", 404);
            model.addAttribute("message", "List is empty");
        } else {
            model.addAttribute("code", 200);
            model.addAttribute("message", "success");
            model.addAttribute("EveryUserAvatarList", everyUserAvatarList);
        }
        return model;
    }

    @RequestMapping(value = "/EveryUserAge", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Model ageEveryUserAge(@RequestBody AgeEveryUserAge ageEveryUserAge, Model model) {
        String insurance = ageEveryUserAge.getInsurance();
        List<EveryUserAge> everyUserAgeList = customerService.ageEveryUserAge(insurance);

        if (everyUserAgeList.isEmpty()) {
            model.addAttribute("code", 404);
            model.addAttribute("message", "List is empty");
        } else {
            model.addAttribute("code", 200);
            model.addAttribute("message", "success");
            model.addAttribute("EveryUserAgeList", everyUserAgeList);
        }
        return model;
    }

    @RequestMapping(value = "/EveryUserGender", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Model genderEveryUserGender(@RequestBody GenderEveryUserGender genderEveryUserGender, Model model) {

        String insurance = genderEveryUserGender.getInsurance();
        List<EveryUserGender> everyUserGenderList = customerService.genderEveryUserGender(insurance);

        if (everyUserGenderList.isEmpty()) {
            model.addAttribute("code", 404);
            model.addAttribute("message", "List is empty");
        } else {
            model.addAttribute("code", 200);
            model.addAttribute("message", "success");
            model.addAttribute("EveryUserGenderList", everyUserGenderList);
        }

        return model;
    }

    @RequestMapping(value = "/EveryUserInjury", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Model injuryEveryUserInjury(@RequestBody InjuryEveryUserInjury injuryEveryUserInjury, Model model) {
        String insurance = injuryEveryUserInjury.getInsurance();
        List<EveryUserInjury> everyUserInjuryList = customerService.injuryEveryUserInjury(insurance);

        if (everyUserInjuryList.isEmpty()) {
            model.addAttribute("code", 404);
            model.addAttribute("message", "List is empty");
        } else {
            model.addAttribute("code", 200);
            model.addAttribute("message", "success");
            model.addAttribute("EveryUserInjuryList", everyUserInjuryList);
        }

        return model;
    }
}
