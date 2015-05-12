import SonatypeKeys._

lazy val commonSettings = Seq(
  organization := "biz.enef",
  version := "0.2",
  scalaVersion := "2.11.6",
  scalacOptions ++= Seq("-deprecation","-unchecked","-feature","-Xlint")
)

lazy val root = project.in(file(".")).
  aggregate(sloggingJVM,sloggingJS).
  settings(commonSettings:_*).
  settings(sonatypeSettings: _*).
  settings(
    name := "slogging",
    publish := {},
    publishLocal := {}
  )

lazy val slogging = crossProject.in(file(".")).
  settings(commonSettings:_*).
  settings(publishingSettings:_*).
  settings(
    name := "slogging",
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % "2.11.6"
    )
  ).
  jvmSettings(
  ).
  jsSettings(
    //preLinkJSEnv := NodeJSEnv().value,
    //postLinkJSEnv := NodeJSEnv().value
  )

lazy val sloggingJVM = slogging.jvm

lazy val sloggingJS = slogging.js

lazy val publishingSettings = Seq(
  publishMavenStyle := true,
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases"  at nexus + "service/local/staging/deploy/maven2")
  },
  pomExtra := (
    <url>https://github.com/jokade/slogging</url>
    <licenses>
      <license>
        <name>MIT License</name>
        <url>http://www.opensource.org/licenses/mit-license.php</url>
      </license>
    </licenses>
    <scm>
      <url>git@github.com:jokade/slogging</url>
      <connection>scm:git:git@github.com:jokade/slogging.git</connection>
    </scm>
    <developers>
      <developer>
        <id>jokade</id>
        <name>Johannes Kastner</name>
        <email>jokade@karchedon.de</email>
      </developer>
    </developers>
  )
)
 
