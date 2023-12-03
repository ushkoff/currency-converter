ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.12"

libraryDependencies += "com.typesafe.play" %% "play-json" % "2.10.0"

lazy val root = (project in file("."))
  .settings(
    name := "currency-converter"
  )
