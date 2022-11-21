package service2

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.{col, udf, when}
import org.apache.spark.sql.types.StringType
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
    val encryptUDF = udf(sha256Hash _)
    val path = "data"
    val data = CSV.read(sparkSession, path)
    val searchedID = data("IdentifiantClient") === id
    val result = data.filter(searchedID)
    if (result.count() == 0) {
      return false
    }
    data.withColumn("Nom", when(col("IdentifiantClient").equalTo(id), encryptUDF(col("Nom").cast(StringType)))
      .otherwise(col("Nom")
      )
    )
    data.withColumn("Prenom", when(col("IdentifiantClient").equalTo(id), encryptUDF(col("Prenom").cast(StringType)))
      .otherwise(col("Prenom")
      )
    )
    data.withColumn("Adresse", when(col("IdentifiantClient").equalTo(id), encryptUDF(col("Adresse").cast(StringType)))
      .otherwise(col("Adresse")
      )
    )
    data.withColumn("DateDeSouscription", when(col("IdentifiantClient").equalTo(id), encryptUDF(col("DateDeSouscription").cast(StringType)))
      .otherwise(col("DateDeSouscription")
      )
    )
    CSV.write("data", data)
    return true
  }


}
