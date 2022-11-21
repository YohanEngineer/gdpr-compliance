package utils

import config.Client
import org.apache.spark.sql.{DataFrame, Dataset, SaveMode, SparkSession}

object CSV {
  def read(sparkSession: SparkSession, path: String): DataFrame = {
    import org.apache.spark.sql.Encoders

    val schema = Encoders.product[Client].schema

    val data : DataFrame = sparkSession
      .read
      .option("header", true)
      .option("delimiter", ";")
      .schema(schema)
      .csv(path).coalesce(1)

    return data
  }

  def write(path: String, df : DataFrame): Unit = {
    df.write.mode(SaveMode.Overwrite).csv(path)
  }
}
