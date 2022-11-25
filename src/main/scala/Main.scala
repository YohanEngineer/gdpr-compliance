import config.Config
import org.apache.spark.sql.SparkSession
import scopt.{OParser, OParserBuilder}
import service1.DeleteClient
import service2.HashTool

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
      checkConfig(c =>
        if (c.hash > 1 && c.delete > 1 ||
          (c.hashM.nonEmpty && c.deleteM.nonEmpty) ||
          (c.hashM.nonEmpty && c.delete > 1) ||
          (c.deleteM.nonEmpty && c.hash > 1)) {
          failure("We can't hash and delete data at the same time.")
        }
        else if (c.hashM.nonEmpty && c.hash > 1) {
          failure("If you want to hash multiple clients please use --hashM option else --hash")
        }
        else if (c.deleteM.nonEmpty && c.delete > 1) {
          failure("If you want to delete multiple clients please use --deleteM option else --delete")
        }
        else success)
    )
  }

  def main(args: Array[String]): Unit = {
    OParser.parse(argParser, args, Config()) match {
      case Some(config) =>
        val hashId = config.hash
        val deleteId = config.delete
        var result = false
        if (hashId == 1 && deleteId == 1 && config.hashM.isEmpty && config.deleteM.isEmpty) {
          println(OParser.usage(argParser))
          exit(1)
        }
        val sparkSession = SparkSession.builder()
          .appName("GDPR-COMPLIANCE-APP")
          .master("local")
          .getOrCreate()
        if (config.hashM.nonEmpty) {
          val idList = config.hashM.asInstanceOf[List[Long]]
          HashTool.hashMultiple(sparkSession, idList)
        }
        if (config.deleteM.nonEmpty) {
          val idlist = config.deleteM.asInstanceOf[List[Long]]
          DeleteClient.deleteClients(sparkSession, idlist)
        }
        if (hashId > 1) {
          result = HashTool.hashClient(sparkSession, hashId)
        }
        if (deleteId > 1) {
          DeleteClient.deleteClient(sparkSession, deleteId)
        }
      case _ =>
        exit(1)
    }
  }

}

