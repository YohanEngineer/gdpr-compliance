package service2

import org.scalatest.funsuite.AnyFunSuite
import utils.{DataFrameTestUtils, SparkSessionTestWrapper}


class HashToolTest extends AnyFunSuite with SparkSessionTestWrapper with DataFrameTestUtils {

  test("HashTool.hash") {

    import spark.implicits._

    val dataframeToTest = Seq(
      ("2564879", "Smith", "Adams", "Allée Adrienne Lecouvreur", "2016-02-02 00:00:00.0000"),
      ("2564880", "Adams", "Smith", "Allée Claude Montal", "2017-02-02 00:00:00.0000"),
      ("2564881", "Bates", "Brown", "Allée de la Comtesse de Ségur", "2018-02-02 00:00:00.0000"),
      ("2564882", "Smith", "Lopez", "Allée Alquier-Debrousse", "2019-02-02 00:00:00.0000")
    ).toDF("IdentifiantClient", "Nom", "Prenom", "Adresse", "DateDeSouscription")

    val name = HashTool.hash(dataframeToTest, 2564879).select("Nom").collectAsList().get(0).mkString
    val firstname = HashTool.hash(dataframeToTest, 2564879).select("Prenom").collectAsList().get(0).mkString
    val address = HashTool.hash(dataframeToTest, 2564879).select("Adresse").collectAsList().get(0).mkString
    assert(name.matches("""[a-fA-F\d]{64}"""))
    assert(firstname.matches("""[a-fA-F\d]{64}"""))
    assert(address.matches("""[a-fA-F\d]{64}"""))
  }

  test("HashTool.hash.schemaIsTheSame") {

    import spark.implicits._

    val dataframeToTest = Seq(
      ("2564879", "Smith", "Adams", "Allée Adrienne Lecouvreur", "2016-02-02 00:00:00.0000"),
      ("2564880", "Adams", "Smith", "Allée Claude Montal", "2017-02-02 00:00:00.0000"),
      ("2564881", "Bates", "Brown", "Allée de la Comtesse de Ségur", "2018-02-02 00:00:00.0000"),
      ("2564882", "Smith", "Lopez", "Allée Alquier-Debrousse", "2019-02-02 00:00:00.0000")
    ).toDF("IdentifiantClient", "Nom", "Prenom", "Adresse", "DateDeSouscription")

    val newDF = HashTool.hash(dataframeToTest, 2564879)

    assert(assertSchema(dataframeToTest.schema, newDF.schema))

  }

  test("HashTool.hash.dataframeChanged") {

    import spark.implicits._

    val dataframeToTest = Seq(
      ("2564879", "Smith", "Adams", "Allée Adrienne Lecouvreur", "2016-02-02 00:00:00.0000"),
      ("2564880", "Adams", "Smith", "Allée Claude Montal", "2017-02-02 00:00:00.0000"),
      ("2564881", "Bates", "Brown", "Allée de la Comtesse de Ségur", "2018-02-02 00:00:00.0000"),
      ("2564882", "Smith", "Lopez", "Allée Alquier-Debrousse", "2019-02-02 00:00:00.0000")
    ).toDF("IdentifiantClient", "Nom", "Prenom", "Adresse", "DateDeSouscription")

    val newDF = HashTool.hash(dataframeToTest, 2564879)

    assert(assertData(dataframeToTest, newDF))

  }


}
