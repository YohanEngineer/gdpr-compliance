package service2

import org.apache.spark.sql.functions.{col, udf, when}
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.{DataFrame, SparkSession}
import utils.CsvTool


object HashTool {

  def sha256Hash(text: String): String = String.format("" +
    "%064x",
    new java.math.BigInteger(
      1,
      java.security.MessageDigest
        .getInstance("SHA-256")
        .digest(text.getBytes("UTF-8")
        )
    )
  )

  def hash(data: DataFrame, id: Long): DataFrame = {
    var df = data
    df = hashColumn(df, "IdentifiantClient", "Nom", id)
    df = hashColumn(df, "IdentifiantClient", "Prenom", id)
    df = hashColumn(df, "IdentifiantClient", "Adresse", id)
    df
  }

  def hashClient(sparkSession: SparkSession, id: Long): Boolean = {
    println("Trying to hash data with id : " + id)
    val basePath = "hdfs://localhost:9000/user/yohan/"
    val data_path = "data"
    val data = CsvTool.read(sparkSession, basePath + data_path).coalesce(1)
    val searchedID = data("IdentifiantClient") === id
    val result = data.filter(searchedID)
    result.show()
    if (result.count() == 0) {
      return false
    }
    val dataHashed = hash(data, id)
    val resultHashed = dataHashed.filter(searchedID)
    resultHashed.show()
    CsvTool.write(basePath + "temp", dataHashed)

    true
  }

  def hashMultiple(sparkSession: SparkSession, listIds: List[Long]) {
    listIds.foreach(id => hashClient(sparkSession, id))
  }

  def hashColumn(data: DataFrame, identifyingColumn: String, columnName: String, id: Long): DataFrame = {
    val encryptUDF = udf(sha256Hash _)
    data.withColumn(columnName, when(col(identifyingColumn).equalTo(id), encryptUDF(col(columnName).cast(StringType)))
      .otherwise(col(columnName)
      )
    )
  }

}
