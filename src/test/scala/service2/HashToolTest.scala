package service2

import config.Client
import org.apache.spark.sql.types.LongType
import org.scalatest.funsuite.AnyFunSuite
import utils.{DataSetTestUtils, SparkSessionTestWrapper}


class HashToolTest extends AnyFunSuite with SparkSessionTestWrapper with DataSetTestUtils {

  test("HashTool.hash") {

    import spark.implicits._

    val datasetToTest = Seq(
      ("2564879", "Smith", "Adams", "Allée Adrienne Lecouvreur", "2016-02-02 00:00:00.0000"),
      ("2564880", "Adams", "Smith", "Allée Claude Montal", "2017-02-02 00:00:00.0000"),
      ("2564881", "Bates", "Brown", "Allée de la Comtesse de Ségur", "2018-02-02 00:00:00.0000"),
      ("2564882", "Smith", "Lopez", "Allée Alquier-Debrousse", "2019-02-02 00:00:00.0000")
    )
      .toDF("IdentifiantClient", "Nom", "Prenom", "Adresse", "DateDeSouscription")
      .withColumn("IdentifiantClient", 'IdentifiantClient.cast(LongType))
      .as[Client]

    val name = HashTool.hash(spark, datasetToTest, 2564879).select("Nom").collectAsList().get(0).mkString
    val firstname = HashTool.hash(spark, datasetToTest, 2564879).select("Prenom").collectAsList().get(0).mkString
    val address = HashTool.hash(spark, datasetToTest, 2564879).select("Adresse").collectAsList().get(0).mkString
    assert(name.matches("""[a-fA-F\d]{64}"""))
    assert(firstname.matches("""[a-fA-F\d]{64}"""))
    assert(address.matches("""[a-fA-F\d]{64}"""))
  }

  test("HashTool.hash.schemaIsTheSame") {

    import spark.implicits._

    val datasetToTest = Seq(
      ("2564879", "Smith", "Adams", "Allée Adrienne Lecouvreur", "2016-02-02 00:00:00.0000"),
      ("2564880", "Adams", "Smith", "Allée Claude Montal", "2017-02-02 00:00:00.0000"),
      ("2564881", "Bates", "Brown", "Allée de la Comtesse de Ségur", "2018-02-02 00:00:00.0000"),
      ("2564882", "Smith", "Lopez", "Allée Alquier-Debrousse", "2019-02-02 00:00:00.0000")
    ).toDF("IdentifiantClient", "Nom", "Prenom", "Adresse", "DateDeSouscription")
      .withColumn("IdentifiantClient", 'IdentifiantClient.cast(LongType))
      .as[Client]

    val newDS = HashTool.hash(spark, datasetToTest, 2564879)

    assert(assertSchema(datasetToTest.schema, newDS.schema))

  }

  test("HashTool.hash.datasetChanged") {

    import spark.implicits._

    val datasetToTest = Seq(
      ("2564879", "Smith", "Adams", "Allée Adrienne Lecouvreur", "2016-02-02 00:00:00.0000"),
      ("2564880", "Adams", "Smith", "Allée Claude Montal", "2017-02-02 00:00:00.0000"),
      ("2564881", "Bates", "Brown", "Allée de la Comtesse de Ségur", "2018-02-02 00:00:00.0000"),
      ("2564882", "Smith", "Lopez", "Allée Alquier-Debrousse", "2019-02-02 00:00:00.0000")
    ).toDF("IdentifiantClient", "Nom", "Prenom", "Adresse", "DateDeSouscription")
      .withColumn("IdentifiantClient", 'IdentifiantClient.cast(LongType))
      .as[Client]

    val newDS = HashTool.hash(spark, datasetToTest, 2564879)

    assert(assertData(datasetToTest, newDS))

  }


}
