//      Project: slogging
//       Author: jokade <jkspam@karchedon.de>
//  Description: SBT build definition for the slogging library
name := "slogging"

organization := "biz.enef"

version in ThisBuild := "1.0"

scalaVersion in ThisBuild := "2.11.1"

scalacOptions ++= Seq("-deprecation","-feature","-Xlint")

libraryDependencies in ThisBuild ++= Seq(
  "org.scala-lang" % "scala-reflect" % "2.11.1"
)

lazy val js = project

lazy val root = project.in( file(".") ).aggregate(js)
