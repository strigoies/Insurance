// ClaimsRestApi.java
package com.atguigu.business.rest;

import com.atguigu.business.model.domain.ClaimSettlement;
import com.atguigu.business.model.domain.EveryInjuryInsurance;
import com.atguigu.business.model.domain.InsuranceBeInjury;
import com.atguigu.business.service.ClaimCettlementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Model> avatarEveryAvatarInjuryAll(Model model) {
        List<ClaimSettlement> claimSettlementList = claimCettlementService.avatarEveryAvatarInjuryAll();
        if (claimSettlementList.isEmpty()) {
            model.addAttribute("code", 404); // 404表示资源未找到
            model.addAttribute("message", "List is empty");
            return new ResponseEntity<>(model, HttpStatus.NOT_FOUND);
        } else {
            model.addAttribute("code", 200);
            model.addAttribute("message", "success");
            model.addAttribute("EveryAvatarInjuryAll", claimSettlementList);
            return new ResponseEntity<>(model, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/EveryInsuranceBeInjury", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Model> insuranceEveryInsuranceBeInjury(Model model) {
        List<InsuranceBeInjury> InsuranceBeInjuryList = claimCettlementService.insuranceEveryInsuranceBeInjury();

        if (InsuranceBeInjuryList.isEmpty()) {
            model.addAttribute("code", 404); // 404表示资源未找到
            model.addAttribute("message", "List is empty");
            return new ResponseEntity<>(model, HttpStatus.NOT_FOUND);
        } else {
            model.addAttribute("code", 200);
            model.addAttribute("message", "success");
            model.addAttribute("EveryInsuranceBeInjury", InsuranceBeInjuryList);
            return new ResponseEntity<>(model, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/EveryInjuryInsurance", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Model> insuranceEveryInjuryInsurance(Model model) {
        List<EveryInjuryInsurance> EveryInsurance = claimCettlementService.insuranceEveryInsurance();

        if (EveryInsurance.isEmpty()) {
            model.addAttribute("code", 404); // 404表示资源未找到
            model.addAttribute("message", "List is empty");
            return new ResponseEntity<>(model, HttpStatus.NOT_FOUND);
        } else {
            model.addAttribute("code", 200);
            model.addAttribute("message", "success");
            model.addAttribute("EveryInjuryInsurance", EveryInsurance);
            return new ResponseEntity<>(model, HttpStatus.OK);
        }
    }
}
