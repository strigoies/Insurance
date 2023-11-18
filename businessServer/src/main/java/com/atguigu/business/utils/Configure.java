package com.atguigu.business.utils;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.TransportUtils;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.mongodb.MongoClient;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import redis.clients.jedis.Jedis;

import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

@Configuration
public class Configure {

    private String mongoHost;
    private int mongoPort;
    private String esName;
    private String esPassword;
    private String esHost;
    private int esPort;
    private String redisHost;

    public Configure(){
        //加载配置文件
        try{
            Properties properties = new Properties();
            Resource resource = new ClassPathResource("recommend.properties");
            properties.load(new FileInputStream(resource.getFile()));
            this.mongoHost = properties.getProperty("mongo.host");
            this.mongoPort = Integer.parseInt(properties.getProperty("mongo.port"));
            this.esName = properties.getProperty("es.name");
            this.esPassword = properties.getProperty("es.password");
            this.esHost = properties.getProperty("es.host");
            this.esPort = Integer.parseInt(properties.getProperty("es.port"));
            this.redisHost = properties.getProperty("redis.host");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    @Bean(name = "mongoClient")
    public MongoClient getMongoClient(){
        MongoClient mongoClient = new MongoClient( mongoHost , mongoPort );
        return mongoClient;
    }

    @Bean(name = "transportClient")
    public ElasticsearchClient getTransportClient() {

        String host = esHost;
        int port = esPort;
        String login = esName;
        String password = esPassword;

        //tag::create-secure-client-fingerprint
//        String fingerprint = "F4:42:EC:3F:E4:DE:2E:BE:58:A6:68:3F:D8:01:0F:B2:1F:B4:7C:6C:04:EF:3E:B5:0A:FD:97:28:11:12:CA:0F";

//        SSLContext sslContext = TransportUtils
//                .sslContextFromCaFingerprint(fingerprint); // <1>

        BasicCredentialsProvider credsProv = new BasicCredentialsProvider(); // <2>
        credsProv.setCredentials(
                AuthScope.ANY, new UsernamePasswordCredentials(login, password)
        );

        RestClient restClient = RestClient
                .builder(new HttpHost(host, port, "http")) // <3>
                .setHttpClientConfigCallback(hc -> hc
//                        .setSSLContext(sslContext) // <4>
                        .setDefaultCredentialsProvider(credsProv)
//                        .setSSLHostnameVerifier((s, sslSession) -> true)
                )
                .build();

        // Create the transport and the API client
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        ElasticsearchClient esClient = new ElasticsearchClient(transport);

        return esClient;
    }

    @Bean(name = "jedis")
    public Jedis getRedisClient() {
        Jedis jedis = new Jedis(redisHost);
        return jedis;
    }
}
