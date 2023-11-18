package com.atguigu.recommender

import com.atguigu.recommender.DataLoader.storeDataInES
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.bson.Document
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.spark.sparkRDDFunctions
import org.elasticsearch.transport.client.PreBuiltTransportClient

import java.net.InetAddress

object EsDataLoader {
  // 定义常量
  val MOVIE_DATA_PATH = "E:\\SK-Project\\Fusion\\RecommendSystem-baoxian\\MovieRecommendSystem\\recommender\\DataLoader\\src\\main\\resources\\insurance_test.csv"

  def main(args: Array[String]): Unit = {

    val spark = SparkSession
      .builder()
      .appName("WriteToES")
      .master("local[*]")
      .config("spark.es.nodes", "192.168.50.26")
      .config("spark.es.port", "9200")
      .config("spark.es.nodes.wan.only", "true") // Needed for ES on AWS
      .config("spark.es.net.http.auth.user", "elastic")
      .config("spark.es.net.http.auth.pass", "root@123")
      .getOrCreate()

    import spark.implicits._

    // 加载数据
    val movieRDD = spark.sparkContext.textFile(MOVIE_DATA_PATH)

    val documents = movieRDD.map(line => {
      val data = line.split("\\^")
      if (data.length == 1) {
        Document.parse(s"""{"test": "${data(0)}"}""")
      } else {
        Document.parse(s"""{"mid": ${data(0).toInt}, "price": ${data(1).toDouble}, "issue": "${data(2).trim}", "name": "${data(3).trim}", "descri": "${data(4).trim}", "genres": "${data(5).trim}", "status": "${data(6).trim}", "url": "${data(7).trim}", "age": "${data(8).trim}", "period": "${data(9).trim}", "scope": "${data(10).trim}"}""")
      }
    })

    documents.saveToEs("ecommerce")

    spark.stop()
  }
}
