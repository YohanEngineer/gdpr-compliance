package utils

import config.Client
import org.apache.spark.sql.{DataFrame, SparkSession}

object CsvTools {
  def read(sparkSession: SparkSession, path: String): DataFrame = {
//    import spray.json._
//    import DefaultJsonProtocol._
//
//    val jsonString = os.read(os.pwd/"src"/"main"/"resources"/"schema.json")
//
//    val jsonAst = jsonString.parseJson
//
//    val json = jsonAst.prettyPrint
//
//    print(json)
//
//    val myObject = jsonAst.toJson.convertTo[Client]

    import org.apache.spark.sql.Encoders

    val schema = Encoders.product[Client].schema

    val data: DataFrame = sparkSession
      .read
      .option("header", true)
      .option("delimiter", ";")
      .schema(schema)
      .csv(path).coalesce(1)

    data
  }

  def write(path: String, df: DataFrame): Unit = {
    df.write
      .option("header", true)
      .option("delimiter", ";")
      .mode("overwrite")
      .csv(path)

    import scala.sys.process._
    s"hdfs dfs -rm -r data/" !

    s"hdfs dfs -mv temp data" !

  }
}
