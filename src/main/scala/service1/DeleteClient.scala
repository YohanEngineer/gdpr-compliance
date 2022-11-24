package service1

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.col
import utils.CsvTool

object DeleteClient {

  def deleteClient(sparkSession: SparkSession, id: Long): Unit = {

    val basePath = "hdfs://localhost:9000/user/yohan/"
    val data_path = "data"
    val dataset = CsvTool.read(sparkSession, basePath + data_path) // add base_path

    val dt_clean = dataset.filter(!col("IdentifiantClient").isin(id))

    dt_clean.show()

    CsvTool.write(basePath + "temp", dt_clean) // add base_path

  }

  def deleteClients(sparkSession: SparkSession, list: List[Long]) {


    val basePath = "hdfs://localhost:9000/user/yohan/"
    val data_path = "data"
    val dataset = CsvTool.read(sparkSession, basePath + data_path) // add base_path

    val dt_clean = dataset.filter(!col("IdentifiantClient").isin(list: _*))

    dt_clean.show()

    CsvTool.write(basePath + "temp", dt_clean) // add base_path

  }

}
