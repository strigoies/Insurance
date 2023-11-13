package com.atguigu.business.utils;

public class Constant {

    //************** FOR MONGODB ****************

    public static String MONGODB_DATABASE = "recommender";

    public static String MONGODB_USER_COLLECTION= "User";

    public static String MONGODB_MOVIE_COLLECTION = "Insurance";

    public static String MONGODB_RATING_COLLECTION = "Rating";

    public static String MONGODB_TAG_COLLECTION = "Tag";

    public static String MONGODB_AVERAGE_MOVIES_SCORE_COLLECTION = "AverageInsurances";

    //保险相似度矩阵
    public static String MONGODB_MOVIE_RECS_COLLECTION = "InsuranceRecs";

    //优质保险
    public static String MONGODB_RATE_MORE_MOVIES_COLLECTION = "RateMoreInsurances";

    //最热保险
    public static String MONGODB_RATE_MORE_MOVIES_RECENTLY_COLLECTION = "RateMoreRecentlyInsurances";

    public static String MONGODB_STREAM_RECS_COLLECTION = "StreamRecs";

    //用户推荐矩阵
    public static String MONGODB_USER_RECS_COLLECTION = "UserRecs";

    //top10列表
    public static String MONGODB_GENRES_TOP_MOVIES_COLLECTION = "GenresTopInsurances";

    //实名表
    public static String MONGODB_AUTHENTICATION_COLLECTION = "Authentication";

    //订单表
    public static String MONGODB_ORDER_COLLECTION = "Order";

    //************** FOR ELEASTICSEARCH ****************

    public static String ES_INDEX = "recommender";

    public static String ES_MOVIE_TYPE = "Movie";


    //************** FOR MOVIE RATING ******************

    public static String MOVIE_RATING_PREFIX = "MOVIE_RATING_PREFIX";

    public static int REDIS_MOVIE_RATING_QUEUE_SIZE = 40;
}