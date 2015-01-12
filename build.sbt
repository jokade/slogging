name := "slogging"

organization in ThisBuild := "biz.enef"

version in ThisBuild := "0.1"

scalaVersion in ThisBuild := "2.11.4"

scalacOptions ++= Seq("-deprecation","-feature","-Xlint")

libraryDependencies in ThisBuild ++= Seq(
  "org.scala-lang" % "scala-reflect" % "2.11.4"
)

lazy val jvm = project

lazy val js = project

lazy val root = project.in( file(".") )
                .aggregate(js,jvm)
                .settings( publish := {} ) 

publishTo in ThisBuild := Some( Resolver.sftp("repo", "karchedon.de", "/www/htdocs/w00be83c/maven.karchedon.de/") )
