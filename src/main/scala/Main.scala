import config.Config
import org.apache.spark.sql.SparkSession
import scopt.{OParser, OParserBuilder}
import service1.DeleteClient
import service2.HashClient
import utils.CsvTools

import scala.sys.exit

object Main {

  val builder: OParserBuilder[Config] = OParser.builder[Config]
  val argParser: OParser[Unit, Config] = {
    import builder._
    OParser.sequence(
      programName("gdpr-compliance"),
      head("gdpr-compliance", "1.0.0"),
      opt[Long]('d', "delete")
        .optional()
        .action((s, c) => c.copy(delete = s))
        .text("delete by id")
        .validate(s =>
          if (s >= 2564879) success
          else failure("Option --delete must have a valid id"))
      ,
      opt[Seq[Long]]("deleteM")
        .optional()
        .abbr("dm")
        .valueName("<id1>,<id2>...")
        .action((x, c) => c.copy(deleteM = x))
        .text("delete multiple clients by ids"),
      opt[Long]('h', "hash")
        .optional()
        .action((s, c) => c.copy(hash = s))
        .text("hash by id")
        .validate(s =>
          if (s >= 2564879) success
          else failure("Option --hash must have a valid id"))
      ,
      opt[Seq[Long]]("hashM")
        .optional()
        .abbr("hm")
        .valueName("<id1>,<id2>...")
        .action((x, c) => c.copy(hashM = x))
        .text("hash multiple clients by ids"),
      opt[Boolean]('i', "init")
        .hidden()
        .optional()
        .action((s, c) => c.copy(init = s))
        .text("init database")
      ,
      checkConfig(c =>
        if (c.hash > 1 && c.delete > 1) {
          failure("We can't hash and delete your data at the same time.")
        }
        else success)
    )
  }


  def initDb(sparkSession: SparkSession): Unit = {
    // https://kontext.tech/article/1067/spark-dynamic-and-static-partition-overwrite
    val path = "hdfs://localhost:9000/user/yohan/secret/"
    val data = CsvTools.read(sparkSession, path)
    val writingPath = "hdfs://localhost:9000/user/yohan/data/"
    CsvTools.write(writingPath, data)
  }

  def main(args: Array[String]): Unit = {
    OParser.parse(argParser, args, Config()) match {
      case Some(config) =>
        val hashId = config.hash
        val deleteId = config.delete
        val isInitTrue = config.init
        var result = false
        if (hashId == 1 && deleteId == 1) {
          println(OParser.usage(argParser))
          exit(1)
        }
        val sparkSession = SparkSession.builder().appName("GDPR-COMPLIANCE-APP").master("local").getOrCreate()
        if (isInitTrue) {
          initDb(sparkSession)
        }
        if (hashId > 1) {
          result = HashClient.hash(sparkSession, hashId)
        }
        if (deleteId > 1) {
          DeleteClient.deleteClient(sparkSession, deleteId)
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
        exit(1)
    }
  }

}

