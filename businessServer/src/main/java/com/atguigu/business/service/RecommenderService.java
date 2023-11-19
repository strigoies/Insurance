package com.atguigu.business.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.atguigu.business.model.domain.Insurance;
import com.atguigu.business.model.recom.Recommendation;
import com.atguigu.business.model.request.*;
import com.atguigu.business.utils.Constant;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class RecommenderService {

    // 混合推荐中CF的比例
    private static Double CF_RATING_FACTOR = 0.3;
    private static Double CB_RATING_FACTOR = 0.3;
    private static Double SR_RATING_FACTOR = 0.4;

    @Autowired
    private MongoClient mongoClient;
    @Autowired
    private ElasticsearchClient esClient;

    // 协同过滤推荐【电影相似性】
    private List<Recommendation> findMovieCFRecs(int mid, int maxItems) {
        MongoCollection<Document> movieRecsCollection = mongoClient.getDatabase(Constant.MONGODB_DATABASE).getCollection(Constant.MONGODB_INSURANCE_RECS_COLLECTION);
        Document movieRecs = movieRecsCollection.find(new Document("mid", mid)).first();
        return parseRecs(movieRecs, maxItems);
    }

    // 协同过滤推荐【用户电影矩阵】
    private List<Recommendation> findUserCFRecs(int uid, int maxItems) {
        MongoCollection<Document> movieRecsCollection = mongoClient.getDatabase(Constant.MONGODB_DATABASE).getCollection(Constant.MONGODB_USER_RECS_COLLECTION);
        Document userRecs = movieRecsCollection.find(new Document("uid", uid)).first();
        return parseRecs(userRecs, maxItems);
    }

    // 基于内容的推荐算法
    private List<Recommendation> findContentBasedMoreLikeThisRecommendations(String desc, int maxItems) {
//        MoreLikeThisQueryBuilder query = QueryBuilders.moreLikeThisQuery(
//                new MoreLikeThisQueryBuilder.Item[]{new MoreLikeThisQueryBuilder.Item(Constant.ES_INDEX, Constant.ES_MOVIE_TYPE, String.valueOf(mid))});
        MoreLikeThisQuery moreLikeThisQuery = MoreLikeThisQuery.of(m ->
                m.fields("descri")
                        .like(new Like.Builder().text(desc).build())
                        .minTermFreq(1)
        );
        SearchRequest searchRequest = SearchRequest.of(s -> s.index(Constant.ES_INDEX)
                .size(maxItems)
                .query(Query.of(q -> q.moreLikeThis(moreLikeThisQuery))));
        try{
            SearchResponse<Insurance> response = esClient.search(searchRequest, Insurance.class);
            return parseESResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<Recommendation>();
    }

    // 实时推荐
    private List<Recommendation> findStreamRecs(int uid,int maxItems){
        MongoCollection<Document> streamRecsCollection = mongoClient.getDatabase(Constant.MONGODB_DATABASE).getCollection(Constant.MONGODB_STREAM_RECS_COLLECTION);
        Document streamRecs = streamRecsCollection.find(new Document("uid", uid)).first();
        return parseRecs(streamRecs, maxItems);
    }

    //用于解析Document
    private List<Recommendation>    parseRecs(Document document, int maxItems) {
        List<Recommendation> recommendations = new ArrayList<>();
        if (null == document || document.isEmpty())
            return recommendations;
        ArrayList<Integer> recs = document.get("recs", ArrayList.class);
        for (int mid : recs) {
            int count = getMovieCount(mid);
            recommendations.add(new Recommendation(mid, count));
        }
        Collections.sort(recommendations, new Comparator<Recommendation>() {
            @Override
            public int compare(Recommendation o1, Recommendation o2) {
                return o1.getCount() > o2.getCount() ? -1 : 1;
            }
        });
        return recommendations.subList(0, Math.min(maxItems, recommendations.size()));
    }

    //通过mid获取点击量
    public int getMovieCount(int mids){
        Document document = mongoClient.getDatabase(Constant.MONGODB_DATABASE).getCollection(Constant.MONGODB_INSURSNCE_COLLECTION)
                .find(Filters.in("mid",mids)).first();
        if (document != null) {
            return document.getInteger("count", -1);
        }
        return -1;
    }

    // 全文检索
    private List<Recommendation> findContentBasedSearchRecommendations(String text, int maxItems) {
//        MultiMatchQueryBuilder query = QueryBuilders.multiMatchQuery(text, "name", "descri");
        MultiMatchQuery query = MultiMatchQuery.of(m ->
                m.fields("name", "descri")
                        .query(text)
        );
        SearchRequest searchRequest = SearchRequest.of(s -> s.size(maxItems)
                .query(Query.of(q -> q.multiMatch(query))));
        try{
            SearchResponse<Insurance> response = esClient.search(searchRequest, Insurance.class);
            return parseESResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        return parseESResponse(esClient.prepareSearch().setIndices(Constant.ES_INDEX).setTypes(Constant.ES_MOVIE_TYPE).setQuery(query).setSize(maxItems).execute().actionGet());
        return new ArrayList<Recommendation>();
    }

    private List<Recommendation> parseESResponse(SearchResponse response) {
        List<Recommendation> recommendations = new ArrayList<>();
//        for (SearchHit hit : response.getHits()) {
//            recommendations.add(new Recommendation((int) hit.getSourceAsMap().get("mid"), (double) hit.getScore()));
//        }

        for (Object hit : response.hits().hits()) {
            Hit h = (Hit) hit;
            Insurance insurance = (Insurance) h.source();
            if (insurance == null) continue;
            recommendations.add(new Recommendation(insurance.getMid(), insurance.getCount()));
        }
        return recommendations;
    }

    // 混合推荐算法
    private List<Recommendation> findHybridRecommendations(int productId, int maxItems) {
        List<Recommendation> hybridRecommendations = new ArrayList<>();


        //电影相似性矩阵
        List<Recommendation> cfRecs = findMovieCFRecs(productId, maxItems);
        for (Recommendation recommendation : cfRecs) {
            hybridRecommendations.add(new Recommendation(recommendation.getMid(), recommendation.getCount() * CF_RATING_FACTOR));
        }

        //基于内容推荐TODO
//        List<Recommendation> cbRecs = findContentBasedMoreLikeThisRecommendations(productId, maxItems);
//        for (Recommendation recommendation : cbRecs) {
//            hybridRecommendations.add(new Recommendation(recommendation.getMid(), recommendation.getCount() * CB_RATING_FACTOR));
//        }

        //实时推荐
        List<Recommendation> streamRecs = findStreamRecs(productId,maxItems);
        for (Recommendation recommendation : streamRecs) {
            hybridRecommendations.add(new Recommendation(recommendation.getMid(), recommendation.getCount() * SR_RATING_FACTOR));
        }

        Collections.sort(hybridRecommendations, new Comparator<Recommendation>() {
            @Override
            public int compare(Recommendation o1, Recommendation o2) {
                return o1.getCount() > o2.getCount() ? -1 : 1;
            }
        });
        return hybridRecommendations.subList(0, maxItems > hybridRecommendations.size() ? hybridRecommendations.size() : maxItems);
    }



    public List<Recommendation> getCollaborativeFilteringRecommendations(UserRecommendationRequest request) {

        return findUserCFRecs(request.getUid(), request.getSum());
    }

    public List<Recommendation> getContentBasedMoreLikeThisRecommendations(MovieRecommendationRequest request) {
        return findContentBasedMoreLikeThisRecommendations(request.getDescri(), request.getSum());
    }

    public List<Recommendation> getContentBasedSearchRecommendations(SearchRecommendationRequest request) {
        return findContentBasedSearchRecommendations(request.getText(), request.getSum());
    }

    public List<Recommendation> getHybridRecommendations(MovieHybridRecommendationRequest request) {
        return  findHybridRecommendations(request.getMid(), request.getSum());
    }


    public List<Recommendation> getHotRecommendations(HotRecommendationRequest request) {
        // 获取热门电影的条目
        MongoCollection<Document> rateMoreMoviesRecentlyCollection = mongoClient.getDatabase(Constant.MONGODB_DATABASE).getCollection(Constant.MONGODB_RATE_MORE_INSURANCES_RECENTLY_COLLECTION);
        FindIterable<Document> documents = rateMoreMoviesRecentlyCollection.find().limit(request.getSum());

        List<Recommendation> recommendations = new ArrayList<>();
        for (Document document : documents) {
            recommendations.add(new Recommendation(document.getInteger("mid"), 0D));
        }
        return recommendations;
    }

    public List<Recommendation> getRateMoreRecommendations(RateMoreRecommendationRequest request) {

        // 获取评分最多电影的条目
        MongoCollection<Document> rateMoreMoviesCollection = mongoClient.getDatabase(Constant.MONGODB_DATABASE).getCollection(Constant.MONGODB_RATE_MORE_INSURANCES_COLLECTION);
        FindIterable<Document> documents = rateMoreMoviesCollection.find().sort(Sorts.descending("count")).limit(request.getSum());

        List<Recommendation> recommendations = new ArrayList<>();
        for (Document document : documents) {
            recommendations.add(new Recommendation(document.getInteger("mid"), 0D));
        }
        return recommendations;
    }

    public List<Recommendation> getContentBasedGenresRecommendations(SearchRecommendationRequest request) {

        // 保险分类
        FuzzyQuery query = FuzzyQuery.of(m ->
                m.field("genres.keyword")
                        .value(request.getText())
                        .fuzziness("2"));
        SearchRequest searchRequest = SearchRequest.of(s -> s.index(Constant.ES_INDEX)
                .size(request.getSum())
                .query(Query.of(q -> q.fuzzy(query))));
        try{
            SearchResponse<Insurance> response = esClient.search(searchRequest, Insurance.class);
            return parseESResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<Recommendation>();
//        FuzzyQueryBuilder query = QueryBuilders.fuzzyQuery("genres", request.getText());
//        return parseESResponse(esClient.prepareSearch().setIndices(Constant.ES_INDEX).setTypes(Constant.ES_MOVIE_TYPE).setQuery(query).setSize(request.getSum()).execute().actionGet());

        /*FuzzyQueryBuilder query = QueryBuilders.fuzzyQuery("genres","意外医疗保险责任");
        //WildcardQueryBuilder query=QueryBuilders.wildcardQuery("genres", request.getText());
        System.out.println("--------request.getText"+query);
        System.out.println(esClient.prepareSearch().setIndices(Constant.ES_INDEX).setTypes(Constant.ES_MOVIE_TYPE).setQuery(QueryBuilders.queryStringQuery(request.getText())).get());
        System.out.println(esClient.prepareSearch().setIndices(Constant.ES_INDEX).setTypes(Constant.ES_MOVIE_TYPE).setQuery(QueryBuilders.fuzzyQuery("genres.keyword","医疗")).get());
        return parseESResponse(esClient.prepareSearch().setIndices(Constant.ES_INDEX).setTypes(Constant.ES_MOVIE_TYPE).setQuery(QueryBuilders.queryStringQuery(request.getText())).setSize(request.getSum()).execute().actionGet());*/
    }

    public List<Recommendation> getTopGenresRecommendations(TopGenresRecommendationRequest request){

        // Top10查询
        Document genresTopMovies = mongoClient.getDatabase(Constant.MONGODB_DATABASE).getCollection(Constant.MONGODB_GENRES_TOP_MOVIES_COLLECTION)
                .find(Filters.eq("genres",request.getGenres())).first();
        return parseRecs(genresTopMovies,request.getSum());
    }

}