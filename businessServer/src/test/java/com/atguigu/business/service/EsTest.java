package com.atguigu.business.service;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.junit.Test;

public class EsTest {


    @Test
    public void esConnectTest(){
        // URL and API key
        String serverUrl = "http://192.168.50.26:9200";
        String apiKey = "SWZoXzRncjFSMGlRajFnUl9JZG9pZw==";

        // Create the low-level client
        RestClient restClient = RestClient
                .builder(HttpHost.create(serverUrl))
                .setDefaultHeaders(new Header[]{
                        new BasicHeader("elastic", "ApiKey " + apiKey)
                })
                .build();

        // Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        // And create the API client
        ElasticsearchClient esClient = new ElasticsearchClient(transport);
    }


//    @Test
//    public void esConnectTest(){
//        private static synchronized RestHighLevelClient makeConnection() {
//
//            if(client == null) {
//                restHighLevelClient = new RestHighLevelClient(
//                        RestClient.builder( new HttpHost("localhost", "9200", "http")));
//            }
//
//            return client;
//        }
//    }
}
