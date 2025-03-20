name := """ratesapp"""
organization := "assignment"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.16"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.1" % Test
libraryDependencies += "com.typesafe.play" %% "play-jdbc" % "2.9.0" // Play JDBC module
libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.33"    // MySQL JDBC Driver
// Adds additional packages into Twirl
//TwirlKeys.templateImports += "assignment.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "assignment.binders._"
