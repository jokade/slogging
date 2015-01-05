name := "slogging"

organization in ThisBuild := "de.karchedon"

version in ThisBuild := "0.1-SNAPSHOT"

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

publishTo in ThisBuild := Some( Resolver.ssh("repo", "karchedon.de", "/www/htdocs/w00be83c/maven.karchedon.de") as("ssh-w00be83c") )
