package service1

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.types.{DateType, LongType, StringType, StructField, StructType}

object DeleteClient {

  def deleteClient(sparkSession: SparkSession, id: Long): Unit = {

    val schema = StructType(
      Seq(
        StructField("IdentifiantClient", LongType),
        StructField("Nom", StringType),
        StructField("Prénom", StringType),
        StructField("Adresse", StringType),
        StructField("DateDeSouscription", DateType)
      )
    ) // Just for testing

    val dataset = sparkSession.read.schema(schema).option("header",true).csv("data") // just for testing
    //val dataset = sparkSession.read.csv("data").as[Client] FROM HDFS

    val dt_clean = dataset.filter(!col("IdentifiantClient").isin(id))

    dt_clean.show()

    dt_clean.write.format("csv").save("data/spark_output") // WRITE THIS IN HDFS


  }

}
