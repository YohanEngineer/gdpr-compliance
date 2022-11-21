import org.apache.spark.sql.{DataFrame, SparkSession}

object Main {
  def main(args: Array[String]): Unit = {
    val sparkSession = SparkSession.builder().appName("GDPR-COMPLIANCE-APP").master("local").getOrCreate()
  }
}

