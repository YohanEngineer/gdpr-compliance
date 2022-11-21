import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}
import java.time.format.DateTimeFormatter
import java.time.LocalDate

object Example {
  def main(args: Array[String]): Unit = {
    val nbOfArgs: Int = args.length
    val sparkSession = SparkSession.builder().appName("Pollution job").master("yarn").getOrCreate()
    val env = System.getenv("IP_HDFS")
    val writingPath = "hdfs://".concat(env).concat(":9000/user/silver/")
    var readingPath = ""
    nbOfArgs match {
      case 0 =>
        val currentDate = DateTimeFormatter.ofPattern("YYYY-MM-dd").format(java.time.LocalDate.now)
        readingPath = "hdfs://".concat(env).concat(":9000/user/bronze/json/").concat(currentDate).concat("/")
        readingJson(sparkSession, readingPath, writingPath)
      case 1 =>
        val date = args(0)
        readingPath = "hdfs://".concat(env).concat(":9000/user/bronze/json/").concat(date).concat("/")
        readingJson(sparkSession, readingPath, writingPath)
      case 2 =>
        val startDate = args(0).split("-")
        val endDate = args(1).split("-")
        val start = LocalDate.of(startDate(0).toInt,startDate(1).toInt,startDate(2).toInt).toEpochDay
        val end = LocalDate.of(endDate(0).toInt,endDate(1).toInt,endDate(2).toInt).toEpochDay
        val dates = (start to end).map(LocalDate.ofEpochDay(_).toString).toArray
        dates.foreach(date=>readingJson(sparkSession, "hdfs://".concat(env).concat(":9000/user/bronze/json/").concat(date).concat("/"), writingPath))


    }
  }


  def readingJson(sparkSession: SparkSession, readPath: String, writingPath: String): Unit = {

    val data: DataFrame = sparkSession
      .read
      .json(readPath).coalesce(1)

    val df = data.select("taux_co2", "date_heure")

    val english_df =
      df
        .withColumnRenamed("date_heure", "timestamp")
        .withColumnRenamed("taux_co2", "co2_rate")

    val dfToWrite = english_df.filter(english_df("co2_rate").isNotNull)

    dfToWrite.write.mode(SaveMode.Append).partitionBy("timestamp").parquet(writingPath + "pollution")

  }
}
