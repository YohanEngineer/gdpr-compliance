package service2

import org.apache.spark.sql.functions.{col, udf, when}
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.{DataFrame, SparkSession}
import utils.CsvTools


object HashClient {

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

  def hash(sparkSession: SparkSession, id: Long): Boolean = {
    println("Trying to hash data with id : " + id)
    val basePath = "hdfs://localhost:9000/user/yohan/"
    val data_path = "data"
    var data = CsvTools.read(sparkSession, basePath + data_path).coalesce(1)
    val searchedID = data("IdentifiantClient") === id
    var result = data.filter(searchedID)
    if (result.count() == 0) {
      return false
    }
    data = hashColumn(data, "IdentifiantClient", "Nom", id)
    data = hashColumn(data, "IdentifiantClient", "Prenom", id)
    data = hashColumn(data, "IdentifiantClient", "Adresse", id)
    data = hashColumn(data, "IdentifiantClient", "DateDeSouscription", id)
    result = data.filter(searchedID)
    result.show()
    CsvTools.write(basePath + "temp", data)

    true
  }

  def hashMultiple(sparkSession: SparkSession, listIds: List[Long]) {
    listIds.foreach(id => hash(sparkSession, id))
  }

  def hashColumn(data: DataFrame, identifyingColumn: String, columnName: String, id: Long): DataFrame = {
    val encryptUDF = udf(sha256Hash _)
    data.withColumn(columnName, when(col(identifyingColumn).equalTo(id), encryptUDF(col(columnName).cast(StringType)))
      .otherwise(col(columnName)
      )
    )
  }

}
