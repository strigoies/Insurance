package com.atguigu.business.rest;

import com.atguigu.business.model.domain.EsSearch;
import com.atguigu.business.model.domain.SortSearch;
import com.atguigu.business.model.domain.Tag;
import com.atguigu.business.model.recom.Recommendation;
import com.atguigu.business.model.request.*;
import com.atguigu.business.service.*;
import com.atguigu.business.model.domain.User;
import com.atguigu.business.utils.Constant;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Random;


@RequestMapping("/rest/insurance")
@Controller
public class MovieRestApi {

    private static Logger logger = Logger.getLogger(MovieRestApi.class.getName());

    @Autowired
    private RecommenderService recommenderService;
    @Autowired
    private MovieService movieService;
    @Autowired
    private UserService userService;
    @Autowired
    private RatingService ratingService;


    /**
     * 该接口暂时不用
     * 获取推荐的电影【实时推荐6 + 内容推荐4】
     * @return
     */
    // TODO: 混合推荐结果中，基于内容的推荐，基于MID，而非UID
    @RequestMapping(value = "/guess", produces = "application/json", method = RequestMethod.GET )
    @ResponseBody
    public Model getGuessMovies(@RequestParam("username")String username,@RequestParam("num")int num, Model model) {
        User user = userService.findByUsername(username);
        List<Recommendation> recommendations = recommenderService.getHybridRecommendations(new MovieHybridRecommendationRequest(user.getUid(),num));
        if(recommendations.isEmpty()){
            if (!user.getPrefGenres().isEmpty()) {
                String randomGenres = user.getPrefGenres().get(new Random().nextInt(user.getPrefGenres().size()));
                recommendations = recommenderService.getTopGenresRecommendations(new TopGenresRecommendationRequest(randomGenres.split(" ")[0],num));
            }
        }
        model.addAttribute("success",true);
        model.addAttribute("insurances",movieService.getHybirdRecommendeMovies(recommendations));
        return model;
    }

    /**
     * 该接口用到UserRecs表和GenresTopInsurances表
     * 逻辑：如果有用户推荐矩阵，则返回结果；如果没有，则返回TOP10表中内容
     */
    @RequestMapping(value = "/wish", produces = "application/json", method = RequestMethod.GET )
    @ResponseBody
    public Model getWishMovies(@RequestParam("username")String username,@RequestParam("num")int num, Model model) throws UnsupportedEncodingException {
        User user = userService.findByUsername(username);
        if (user != null) {
            //获取用户协同过滤结果
            List<Recommendation> recommendations = recommenderService.getCollaborativeFilteringRecommendations(new UserRecommendationRequest(user.getUid(), num));
            //没有协同过滤结果的情况下，获取用户偏好
            if (recommendations.isEmpty()) {
                if (!user.getPrefGenres().isEmpty()) {
                    //query= new String(user.getPrefGenres().getBytes("ISO8859-1"),"UTF-8");
                    System.out.println("保险条款" + user.getPrefGenres());
                    String randomGenres = user.getPrefGenres().get(new Random().nextInt(user.getPrefGenres().size()));
                    recommendations = recommenderService.getTopGenresRecommendations(new TopGenresRecommendationRequest(randomGenres.split(" ")[0], num));
                    System.out.println("/wish离线推荐" + recommendations.size());
                }
            }
            model.addAttribute("success", true);
            model.addAttribute("insurances", movieService.getRecommendeMovies(recommendations));
        } else{
            model.addAttribute("success", false);
            model.addAttribute("reason", "用户不存在");
        }
        return model;
    }

    /**
     * 所用表RateMoreRecentlyInsurances
     * 逻辑：查热门表
     */
    @RequestMapping(value = "/hot", produces = "application/json", method = RequestMethod.GET )
    @ResponseBody
    public Model getHotMovies(@RequestParam("num")int num, Model model) {
        List<Recommendation> recommendations = recommenderService.getHotRecommendations(new HotRecommendationRequest(num));
        model.addAttribute("success",true);
        model.addAttribute("insurances",movieService.getRecommendeMovies(recommendations));
        return model;
    }

    /**
     * 所用表：Insurance
     * 逻辑：用SQL查最新保险
     */
    @RequestMapping(value = "/new", produces = "application/json", method = RequestMethod.GET )
    @ResponseBody
    public Model getNewMovies(@RequestParam("num")int num, Model model) {
        model.addAttribute("success",true);
        model.addAttribute("insurances",movieService.getNewMovies(new NewRecommendationRequest(num)));
        return model;
    }

    /**
     * 所用表：ES表
     * 逻辑：使用ES的相关查询方法
     * 获取电影详细页面相似的电影集合
     */
    @RequestMapping(value = "/same", produces = "application/json", method = RequestMethod.GET )
    @ResponseBody
    public Model getSameMovie(@RequestBody EsSearch esSearch, Model model) throws UnsupportedEncodingException {
        String descri = esSearch.getQuery();
        int num = esSearch.getNum();
        List<Recommendation> recommendations = recommenderService.getContentBasedMoreLikeThisRecommendations(new MovieRecommendationRequest(descri,num));
        model.addAttribute("success",false);
        model.addAttribute("insurances",movieService.getRecommendeMovies(recommendations));
        return model;
    }

    /**
     * 所用表：Insurance
     * 逻辑：获取单个电影的信息
     */
    @RequestMapping(value = "/info/{id}", produces = "application/json", method = RequestMethod.GET )
    @ResponseBody
    public Model getMovieInfo(@PathVariable("id")int id, Model model) {
        model.addAttribute("success",true);
        model.addAttribute("insurance",movieService.findByMID(id));
        movieService.AddClick(id);
        return model;
    }

    /**
     * 所用表：ES
     * 逻辑：模糊查询保险
     */
    @RequestMapping(value = "/search", produces = "application/json", method = RequestMethod.GET )
    @ResponseBody
    public Model getSearchMovies(@RequestBody EsSearch esSearch, Model model) throws UnsupportedEncodingException {
        String query = esSearch.getQuery();
        int num = esSearch.getNum();
//        query= new String(query.getBytes("ISO8859-1"),"UTF-8");
        List<Recommendation> recommendations = recommenderService.getContentBasedSearchRecommendations(new SearchRecommendationRequest(query,num));
        model.addAttribute("success",true);
        model.addAttribute("insurances",movieService.getRecommendeMovies(recommendations));
        return model;
    }

    /**
     * 所用表：ES
     * 逻辑：查询类别保险
     */
    @RequestMapping(value = "/genres", produces = "application/json", method = RequestMethod.GET )
    @ResponseBody
    public Model getGenresMovies(@RequestBody SortSearch sortSearch, Model model) throws UnsupportedEncodingException {
        String category = sortSearch.getSort();
        int num = sortSearch.getNum();
//        category= new String(category.getBytes("ISO8859-1"),"UTF-8");
        List<Recommendation> recommendations = recommenderService.getContentBasedGenresRecommendations(new SearchRecommendationRequest(category,num));
        model.addAttribute("success",true);
        model.addAttribute("insurances",movieService.getRecommendeMovies(recommendations));
        return model;
    }
}
