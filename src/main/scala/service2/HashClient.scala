package service2

import org.apache.spark.sql.functions.{col, udf, when}
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.{DataFrame, SparkSession}
import utils.CSV


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
    val path = "data"
    var data = CSV.read(sparkSession, path)
    val searchedID = data("IdentifiantClient") === id
    var result = data.filter(searchedID)
    if (result.count() == 0) {
      return false
    }
    data = hashColumn(data,"IdentifiantClient","Nom", id)
    data = hashColumn(data,"IdentifiantClient","Prenom", id)
    data = hashColumn(data,"IdentifiantClient","Adresse", id)
    data = hashColumn(data,"IdentifiantClient","DateDeSouscription", id)
    result = data.filter(searchedID)
    result.show()
    CSV.write("temp", data)
    true
  }

  def hashColumn(data: DataFrame, identifyingColumn: String,columnName: String, id: Long): DataFrame = {
    val encryptUDF = udf(sha256Hash _)
    data.withColumn(columnName, when(col(identifyingColumn).equalTo(id), encryptUDF(col(columnName).cast(StringType)))
      .otherwise(col(columnName)
      )
    )
  }

//  def replaceFile(sc: SparkSession) = {
//    import org.apache.hadoop.fs._
//    val conf = sc.sparkContext.hadoopConfiguration
//    val fs = FileSystem.get(conf)
//    fs.rename(new Path("temp/part-0000*"), new Path("data/store.csv"))
//  }

}
