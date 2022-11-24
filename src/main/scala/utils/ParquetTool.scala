package utils

import config.Client
import org.apache.spark.sql.{DataFrame, SparkSession}

object ParquetTool {
  def read(sparkSession: SparkSession, path: String): DataFrame = {
    import org.apache.spark.sql.Encoders

    val schema = Encoders.product[Client].schema

    val data: DataFrame = sparkSession
      .read
      .option("header", true)
      .option("delimiter", ";")
      .schema(schema)
      .parquet(path).coalesce(1)

    data
  }

  def write(path: String, df: DataFrame): Unit = {
    df.write
      .option("header", true)
      .option("delimiter", ";")
      .mode("overwrite")
      .parquet(path)

    import scala.sys.process._
    s"hdfs dfs -rm -r data/" !

    s"hdfs dfs -mv temp data" !

  }
}
