ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.11.11"

lazy val root = (project in file("."))
  .settings(
    name := "gdpr-compliance"
  )

libraryDependencies += "org.apache.spark" %% "spark-core" % "2.4.8"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.4.8"
libraryDependencies += "com.github.scopt" %% "scopt" % "4.1.0"
libraryDependencies += "io.spray" %%  "spray-json" % "1.3.6"
libraryDependencies += "com.lihaoyi" %% "os-lib" % "0.8.1"
