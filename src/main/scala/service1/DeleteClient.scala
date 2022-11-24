package service1

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.types._
import utils.CsvTools

object DeleteClient {

  def deleteClient(sparkSession: SparkSession, id: Long): Unit = {

    val basePath = "hdfs://localhost:9000/user/yohan/"
    val data_path = "data"
    val dataset = CsvTools.read(sparkSession,basePath+data_path) // add base_path

    val dt_clean = dataset.filter(!col("IdentifiantClient").isin(id))

    dt_clean.show()

    CsvTools.write(basePath+"temp", dt_clean) // add base_path

  }

  def deleteClients(sparkSession: SparkSession, list: List[Long]) {


    val basePath = "hdfs://localhost:9000/user/yohan/"
    val data_path = "data"
    val dataset = CsvTools.read(sparkSession,basePath+data_path) // add base_path

    val dt_clean = dataset.filter(!col("IdentifiantClient").isin(list: _*))

    dt_clean.show()

    CsvTools.write(basePath+"temp", dt_clean) // add base_path

  }

}
