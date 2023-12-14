// ClaimsRestApi.java
package com.atguigu.business.rest;

import com.atguigu.business.model.domain.ClaimSettlement;
import com.atguigu.business.model.domain.InsuranceBeInjury;
import com.atguigu.business.service.ClaimCettlementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequestMapping("/eduservice/teacher")
@Controller
public class ClaimsRestApi {

    @Autowired
    private ClaimCettlementService claimCettlementService;

    @RequestMapping(value = "/EveryAvatarInjuryAll", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public Model avatarEveryAvatarInjuryAll(Model model) {
        List<ClaimSettlement> claimSettlementList = claimCettlementService.avatarEveryAvatarInjuryAll();
        model.addAttribute("code", 200);
        model.addAttribute("message", "success");
        model.addAttribute("EveryAvatarInjuryAll", claimSettlementList);
        return model;
    }

    @RequestMapping(value = "/EveryInsuranceBeInjury", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public Model insuranceEveryInsuranceBeInjury(Model model) {
        List<InsuranceBeInjury> InsuranceBeInjuryList = claimCettlementService.insuranceEveryInsuranceBeInjury();
        model.addAttribute("code", 200);
        model.addAttribute("message", "success");
        model.addAttribute("EveryInsuranceBeInjury", InsuranceBeInjuryList);
        return model;
    }
}
