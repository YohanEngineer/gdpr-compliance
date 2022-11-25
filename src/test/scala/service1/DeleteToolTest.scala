package service1

import org.apache.spark.sql.functions.col
import org.scalatest.funsuite.AnyFunSuite
import utils.{DataFrameTestUtils, SparkSessionTestWrapper}

class DeleteToolTest extends AnyFunSuite with SparkSessionTestWrapper with DataFrameTestUtils {

  test("DeleteTool.delete") {

    import spark.implicits._

    val datasetForTest = Seq(
      ("2564879", "Smith", "Adams", "Allée Adrienne Lecouvreur", "2016-02-02 00:00:00.0000"),
      ("2564880", "Adams", "Smith", "Allée Claude Montal", "2017-02-02 00:00:00.0000"),
      ("2564881", "Bates", "Brown", "Allée de la Comtesse de Ségur", "2018-02-02 00:00:00.0000"),
      ("2564882", "Smith", "Lopez", "Allée Alquier-Debrousse", "2019-02-02 00:00:00.0000")
    ).toDF("IdentifiantClient", "Nom", "Prenom", "Adresse", "DateDeSouscription")

    val datasetAfterTest = datasetForTest.filter(!col("IdentifiantClient").isin(2564879))

    assert(assertData(datasetForTest,datasetAfterTest))
  }

}
