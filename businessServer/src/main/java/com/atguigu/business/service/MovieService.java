package com.atguigu.business.service;

import com.atguigu.business.model.domain.Insurance;
import com.atguigu.business.model.domain.Rating;
import com.atguigu.business.model.recom.Recommendation;
import com.atguigu.business.model.request.NewRecommendationRequest;
import com.atguigu.business.utils.Constant;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.util.JSON;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Updates.inc;

@Service
public class MovieService {

    @Autowired
    private MongoClient mongoClient;

    @Autowired
    private ObjectMapper objectMapper;

    private MongoCollection<Document> movieCollection;
    private MongoCollection<Document> averageMoviesScoreCollection;
    private MongoCollection<Document> rateCollection;

    private MongoCollection<Document> rateMoreMoviesRecently;

    public MongoCollection<Document> getMovieCollection(){
        if(null == movieCollection)
            movieCollection = mongoClient.getDatabase(Constant.MONGODB_DATABASE).getCollection(Constant.MONGODB_INSURSNCE_COLLECTION);
        return movieCollection;
    }

    private MongoCollection<Document> getAverageMoviesScoreCollection(){
        if(null == averageMoviesScoreCollection)
            averageMoviesScoreCollection = mongoClient.getDatabase(Constant.MONGODB_DATABASE).getCollection(Constant.MONGODB_AVERAGE_INSURANCES_SCORE_COLLECTION);
        return averageMoviesScoreCollection;
    }

    private MongoCollection<Document> getRateCollection(){
        if(null == rateCollection)
            rateCollection = mongoClient.getDatabase(Constant.MONGODB_DATABASE).getCollection(Constant.MONGODB_RATING_COLLECTION);
        return rateCollection;
    }

    private MongoCollection<Document> getRateMoreMoviesRecently(){
        if(null == rateMoreMoviesRecently)
            rateMoreMoviesRecently = mongoClient.getDatabase(Constant.MONGODB_DATABASE).getCollection(Constant.MONGODB_RATE_MORE_INSURANCES_RECENTLY_COLLECTION);
        return rateMoreMoviesRecently;
    }

    public List<Insurance> getRecommendeMovies(List<Recommendation> recommendations){
        List<Integer> ids = new ArrayList<>();
        for (Recommendation rec: recommendations) {
            ids.add(rec.getMid());
        }
        return getMovies(ids);
    }

    public List<Insurance> getHybirdRecommendeMovies(List<Recommendation> recommendations){
        List<Integer> ids = new ArrayList<>();
        for (Recommendation rec: recommendations) {
            ids.add(rec.getMid());
        }
        return getMovies(ids);
    }

    public List<Insurance> getMovies(List<Integer> mids){
        FindIterable<Document> documents = getMovieCollection().find(Filters.in("mid",mids));
        List<Insurance> movies = new ArrayList<>();
        for (Document document: documents) {
            movies.add(documentToMovie(document));
        }
        return movies;
    }

    //返回电影具体信息和平均评分-->返回保险信息和价格
    private Insurance documentToMovie(Document document){
        Insurance movie = null;
        try{
            movie = objectMapper.readValue(JSON.serialize(document), Insurance.class);
//            Document score = getAverageMoviesScoreCollection().find(Filters.eq("mid",movie.getMid())).first();
//            if(null == score || score.isEmpty())
//                movie.setPrice(0D);
//            else
//                //TODO:这个地方需要改，价格在Insurance表用已经有了，不需要再处理
//                movie.setPrice(score.get("price",0D));
        }catch (IOException e) {
            e.printStackTrace();
        }
        return movie;
    }

    private Rating documentToRating(Document document){
        Rating rating = null;
        try{
            rating = objectMapper.readValue(JSON.serialize(document),Rating.class);
        }catch (IOException e) {
            e.printStackTrace();
        }
        return rating;
    }

    public boolean movieExist(int mid){
        return null != findByMID(mid);
    }

    public Insurance findByMID(int mid){
        Document document = getMovieCollection().find(new Document("mid",mid)).first();
        if(document == null || document.isEmpty())
            return null;
        return documentToMovie(document);
    }

    public void AddClick(int mid){
        getRateMoreMoviesRecently().updateOne(Filters.eq("mid", mid), inc("count",1));
    }

    public void removeMovie(int mid){
        getMovieCollection().deleteOne(new Document("mid",mid));
    }

    public List<Insurance> getMyRateMovies(int uid){
        FindIterable<Document> documents = getRateCollection().find(Filters.eq("uid",uid));
        List<Integer> ids = new ArrayList<>();
        Map<Integer,Double> scores = new HashMap<>();
        for (Document document: documents) {
            Rating rating = documentToRating(document);
            ids.add(rating.getMid());
            scores.put(rating.getMid(),rating.getScore());
        }
        List<Insurance> movies = getMovies(ids);
        for (Insurance movie: movies) {
            movie.setPrice(scores.getOrDefault(movie.getMid(),movie.getPrice()));
        }

        return movies;
    }

    public List<Insurance> getNewMovies(NewRecommendationRequest request){
        FindIterable<Document> documents = getMovieCollection().find().sort(Sorts.descending("issue")).limit(request.getSum());
        List<Insurance> movies = new ArrayList<>();
        for (Document document: documents) {
            movies.add(documentToMovie(document));
        }
        return movies;
    }

}
