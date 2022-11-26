package service1

import config.Client
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.{Dataset, SparkSession}
import utils.CsvTool

object DeleteTool {

  def delete(dataset: Dataset[Client], id: Long): Dataset[Client] = {
    val dt_clean = dataset.filter(!col("IdentifiantClient").isin(id))
    dt_clean

  }

  def deleteMultiple(dataset: Dataset[Client], list: List[Long]): Dataset[Client] = {
    val dt_clean = dataset.filter(!col("IdentifiantClient").isin(list: _*))
    dt_clean

  }

  def deleteClient(sparkSession: SparkSession, id: Long): Unit = {

    val basePath = "hdfs://localhost:9000/user/yohan/"
    val data_path = "data"
    val dataset = CsvTool.readDS(sparkSession, basePath + data_path) // add base_path

    val dt_clean = delete(dataset, id)


    CsvTool.writeDS(basePath + "temp", dt_clean)

  }

  def deleteClients(sparkSession: SparkSession, list: List[Long]) {


    val basePath = "hdfs://localhost:9000/user/yohan/"
    val data_path = "data"
    val dataset = CsvTool.readDS(sparkSession, basePath + data_path) // add base_path

    val dt_clean = deleteMultiple(dataset, list)

    CsvTool.writeDS(basePath + "temp", dt_clean)

  }

}
