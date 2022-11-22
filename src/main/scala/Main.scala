import config.Config
import org.apache.spark.sql.SparkSession
import scopt.OParser
import service2.HashClient

import scala.sys.exit

object Main {

  val builder = OParser.builder[Config]
  val argParser = {
    import builder._
    OParser.sequence(
      programName("gdpr-compliance"),
      head("gdpr-compliance", "1.0.0"),
      opt[Long]('d', "delete")
        .action((s, c) => c.copy(delete = s))
        .text("delete by id"),
      opt[Long]('h', "hash")
        .action((s, c) => c.copy(hash = s))
        .text("hash by id")
    )
  }

  def main(args: Array[String]): Unit = {
    OParser.parse(argParser, args, Config()) match {
      case Some(config) =>
        val hashId = config.hash
        val deleteId = config.delete
        var result = false
        if (hashId > 1 && deleteId > 1) {
          println("We can't hash and delete your data at the same time.")
          exit(1)
        }
        if (hashId == 1 && deleteId == 1) {
          println(OParser.usage(argParser))
          exit(1)
        }
        val sparkSession = SparkSession.builder().appName("GDPR-COMPLIANCE-APP").master("local").getOrCreate()
        if (hashId > 1) {
            result = HashClient.hash(sparkSession, hashId)
        }
        if (deleteId > 1) {
          DeleteClient.deleteClient(sparkSession,deleteId)
        }
        //val idlist = List(3,4).asInstanceOf[List[Long]]
        //DeleteClient.deleteClients(sparkSession,idlist)
        if (result) {
          println("Successfully hashed data with id : " + hashId)
        } else {
          println("An error occured while trying to hash data with id : " + hashId)
        }
      case _ =>
        println(OParser.usage(argParser))
    }
  }
}

