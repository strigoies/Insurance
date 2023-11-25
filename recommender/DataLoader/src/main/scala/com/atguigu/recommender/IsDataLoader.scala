package com.atguigu.recommender

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.bson.Document
import org.elasticsearch.spark.sparkRDDFunctions


/**
  * Insurance 数据集
  *
  * 260                                         保险ID，mid
  * Star Wars: Episode IV - A New Hope (1977)   保险公司名称，name
  * Princess Leia is captured and held hostage  产品名称，descri
  * September 21, 2004                          发行时间，issue
  * Action|Adventure|Sci-Fi                     所属类别，genres
  *                                             价格，price
  *                                             详情链接，url
  *                                             销售状态，status
  *                                             承保年龄，age
  *                                             周期，period
  *                                             承保范围，sczope
  */
case class Insurance(mid:Int,name:String,descri:String,issue:String,genres:String,price:Double,url:String,status:String,age:String,period:String,scope:String)
/**
  *
  * @param uri MongoDB连接
  * @param db  MongoDB数据库
  */
//case class MongoConfig(uri:String, db:String)

/**
  *
  * @param httpHosts       http主机列表，逗号分隔
  * @param transportHosts  transport主机列表
  * @param index            需要操作的索引
  * @param clustername      集群名称，默认elasticsearch
  */
//case class ESConfig(httpHosts:String, transportHosts:String, index:String, clustername:String)
object IsDataLoader {

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

    // 加载数据
    val movieRDD = spark.sparkContext.textFile(MOVIE_DATA_PATH)

    import spark.implicits._

    val documents = movieRDD.map(line => {
      val str = scala.util.Random.nextInt(5000).toString
      val data = line.split("\\^")
      if (data.length == 1) {
        Document.parse(s"""{"test": "${data(0)}"}""")
      } else {
        Document.parse(s"""{"mid": ${data(0).toInt}, "price": ${data(1).toDouble}, "issue": "${data(2).trim}", "name": "${data(3).trim}", "descri": "${data(4).trim}", "genres": "${data(5).trim}", "status": "${data(6).trim}", "url": "${data(7).trim}", "age": "${data(8).trim}", "period": "${data(9).trim}", "scope": "${data(10).trim}","count":"${str.toInt}"}""")
      }
    })

    documents.saveToEs("insurance")

    spark.stop()
  }
}
