package service2

import config.Client
import org.apache.spark.sql.functions.{col, udf, when}
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.{Dataset, SparkSession}
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

  def hash(sparkSession: SparkSession, data: Dataset[Client], id: Long): Dataset[Client] = {
    var ds = data
    ds = hashColumn(sparkSession, ds, "IdentifiantClient", "Nom", id)
    ds = hashColumn(sparkSession, ds, "IdentifiantClient", "Prenom", id)
    ds = hashColumn(sparkSession, ds, "IdentifiantClient", "Adresse", id)
    ds
  }

  def hashClient(sparkSession: SparkSession, id: Long): Boolean = {
    println("Trying to hash data with id : " + id)
    val basePath = "hdfs://localhost:9000/user/yohan/"
    val data_path = "data"
    val data = CsvTool.readDS(sparkSession, basePath + data_path).coalesce(1)
    val searchedID = data("IdentifiantClient") === id
    val result = data.filter(searchedID)
    result.show()
    if (result.count() == 0) {
      return false
    }
    val dataHashed = hash(sparkSession, data, id)
    val resultHashed = dataHashed.filter(searchedID)
    resultHashed.show()
    CsvTool.writeDS(basePath + "temp", dataHashed)

    true
  }

  def hashMultiple(sparkSession: SparkSession, listIds: List[Long]) {
    listIds.foreach(id => hashClient(sparkSession, id))
  }

  def hashColumn(sparkSession: SparkSession, ds: Dataset[Client], identifyingColumn: String, columnName: String, id: Long): Dataset[Client] = {
    val encryptUDF = udf(sha256Hash _)
    import sparkSession.implicits._
    ds.withColumn(columnName, when(col(identifyingColumn).equalTo(id), encryptUDF(col(columnName).cast(StringType)))
      .otherwise(col(columnName)
      )
    ).as[Client]
  }

}
