package com.atguigu.recommender


import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.casbah.{MongoClient, MongoClientURI}
import org.apache.spark.{SparkConf, sql}
import org.apache.spark.sql.{DataFrame, SparkSession}


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
  val INSURANCE_DATA_PATH = "E:\\SK-Project\\Fusion\\RecommendSystem-baoxian\\MovieRecommendSystem\\recommender\\DataLoader\\src\\main\\resources\\insurance_test.csv"
  val MONGODB_INSURANCE_COLLECTION = "Insurance"
  val ES_INSURANCE_INDEX = "Insurance"

  def main(args: Array[String]): Unit = {


    val config = Map(
      "spark.cores" -> "local[*]",
      "mongo.uri" -> "mongodb://192.168.50.26:27017/Insurance",
      "mongo.db" -> "test",
      "es.httpHosts" -> "192.168.50.26:9200",
      "es.transportHosts" -> "192.168.50.26:9300",
      "es.index" -> "recommender",
      "es.cluster.name" -> "elasticsearch"
    )

    // 创建一个sparkConf
    val sparkConf = new SparkConf().setMaster(config("spark.cores")).setAppName("DataLoader")

    // 创建一个SparkSession
    val spark = SparkSession.builder().config(sparkConf).getOrCreate()

    import spark.implicits._

    // 加载数据
    val insuranceRDD = spark.sparkContext.textFile(INSURANCE_DATA_PATH)

    val insuranceDF = insuranceRDD.map(
      item => {
        val attr = item.split("\\^")
        Insurance(attr(0).toInt, attr(1).trim, attr(2).trim, attr(3).trim, attr(4).trim, attr(5).toDouble, attr(6).trim, attr(7).trim, attr(8).trim, attr(9).trim,attr(10).trim)
      }
    ).toDF()

    println(insuranceDF)

    implicit val mongoConfig = MongoConfig(config("mongo.uri"), config("mongo.db"))

    // 将数据保存到MongoDB
    storeDataInMongoDB(insuranceDF)
    spark.close()
  }

  def storeDataInMongoDB(insuranceDF: DataFrame)(implicit mongoConfig: MongoConfig): Unit = {
    val mongoClient = MongoClient(MongoClientURI(mongoConfig.uri))

    //如果已有相应数据库，先删除
    mongoClient(mongoConfig.db)(MONGODB_INSURANCE_COLLECTION).dropCollection()

    //将DF数据写入对应mongodb表中
    insuranceDF.write
      .option("uri",mongoConfig.uri)
      .option("database",MONGODB_INSURANCE_COLLECTION)
      .option("collection",MONGODB_INSURANCE_COLLECTION)
      .mode("overwrite")
      .format("com.mongodb.spark,sql")
      .save()

    //建索引
    mongoClient(mongoConfig.db)(MONGODB_INSURANCE_COLLECTION).createIndex(MongoDBObject("mid" -> 1))

    mongoClient.close()
  }
}
