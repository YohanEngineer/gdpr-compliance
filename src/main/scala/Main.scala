import org.apache.spark.sql.{DataFrame, SparkSession}
import service1.DeleteClient

object Main {
  def main(args: Array[String]): Unit = {
    val sparkSession = SparkSession.builder().appName("GDPR-COMPLIANCE-APP").master("local").getOrCreate()
//    val id = args(0).asInstanceOf[Long]
    val id_test = 1
    DeleteClient.deleteClient(sparkSession,id_test)
  }
}

