package com.atguigu.business.rest;

import com.atguigu.business.model.domain.*;
import com.atguigu.business.service.VisualizationService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/eduservice/teacher")
@Controller
public class OverviewApi {

    @Autowired
    private VisualizationService visualService;

    public static final String SALES = "sales";
    public static final String CLAIMS = "claims";

    /**
     * 1保险种类销售数量
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/EveryInsurance", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public Model insuranceEveryInsurance(Model model) {
        try {
            List<Document> data = visualService.getInsuranceByType(SALES);
            model.addAttribute("code", 200);
            model.addAttribute("message", "保险种类销售数据获取成功！");
            model.addAttribute("EveryInsurance", data);
        } catch (Exception e) {
            model.addAttribute("code", 500);
            model.addAttribute("message", "失败：" + e.getMessage());
        }
        return model;
    }

    /**
     * 2各省分布
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/EveryProvinceInjury", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public Model provinceEveryInjury(Model model) {
        try {
            List<ProvinceData> data = visualService.getInsuranceByProvince(SALES);
            ;
            model.addAttribute("code", 200);
            model.addAttribute("message", "各省分布数据获取成功！");
            model.addAttribute("EveryProvinceInjury", data);
        } catch (Exception e) {
            model.addAttribute("code", 500);
            model.addAttribute("message", "失败：" + e.getMessage());
        }
        return model;
    }

    /**
     * 3各月销售保险数量
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/EveryMonthBuyInsurance", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public Model monthEveryMonthBuyInsurance(Model model) {
        try {
            List<MonthlyData> data = visualService.getInsuranceMonthly(SALES);
            model.addAttribute("code", 200);
            model.addAttribute("message", "各月销售保险数据获取成功！");
            model.addAttribute("EveryMonthBuyInsurance", data);
        } catch (Exception e) {
            model.addAttribute("code", 500);
            model.addAttribute("message", "失败：" + e.getMessage());
        }
        return model;
    }

    /**
     * 4各月出险数量
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/EveryMonthInjury", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public Model monthEveryMonthInjury(Model model) {
        try {
            List<MonthlyData> data = visualService.getInsuranceInjuryMonthly(CLAIMS);
            model.addAttribute("code", 200);
            model.addAttribute("message", "各月出险数据获取成功！");
            model.addAttribute("EveryMonthInjury", data);
        } catch (Exception e) {
            model.addAttribute("code", 500);
            model.addAttribute("message", "失败：" + e.getMessage());
        }
        return model;
    }

    /**
     * 5各行业出险数量
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/EveryAvatarInjury", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public Model avatarEveryAvatarInjury(Model model) {
        try {
            List<IndustryData> data = visualService.getInsuranceByIndustry(CLAIMS);
            model.addAttribute("code", 200);
            model.addAttribute("message", "各行业出险数量数据获取成功！");
            model.addAttribute("EveryAvatarInjury", data);
        } catch (Exception e) {
            model.addAttribute("code", 500);
            model.addAttribute("message", "失败：" + e.getMessage());
        }
        return model;
    }

    /**
     * 6各行业购买保险数量
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/EveryAvatarBuyInsurance", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public Model avatarEveryAvatarBuyInsurance(Model model) {
        try {
            List<IndustryData> data = visualService.getInsuranceByIndustry(SALES);
            model.addAttribute("code", 200);
            model.addAttribute("message", "各行业购买保险数据获取成功！");
            model.addAttribute("EveryAvatarBuyInsurance", data);
        } catch (Exception e) {
            model.addAttribute("code", 500);
            model.addAttribute("message", "失败：" + e.getMessage());
        }
        return model;
    }

    /**
     * 7保险种类个方案销售占比
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/EveryInsurancePlan", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public Model insuranceEveryInsurancePlan(Model model) {
        try {
            List<InsuranceTypeData> data = visualService.getInsurancePlanByType(SALES);
            model.addAttribute("code", 200);
            model.addAttribute("message", "保险种类个方案销售占比数据获取成功！");
            model.addAttribute("EveryInsurancePlan", data);
        } catch (Exception e) {
            model.addAttribute("code", 500);
            model.addAttribute("message", "失败：" + e.getMessage());
        }
        return model;
    }
}
