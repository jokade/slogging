import SonatypeKeys._

lazy val commonSettings = Seq(
  organization := "biz.enef",
  version := "0.3-SNAPSHOT",
  scalaVersion := "2.11.6",
  scalacOptions ++= Seq("-deprecation","-unchecked","-feature","-Xlint")
)

lazy val root = project.in(file(".")).
  aggregate(sloggingJVM,sloggingJS,slf4j,winston,http).
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

lazy val slf4j = project.
  dependsOn(sloggingJVM).
  settings(commonSettings:_*).
  settings(publishingSettings:_*).
  settings(
    name := "slogging-slf4j",
    libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.+"
  )

lazy val winston = project.
  dependsOn(sloggingJS).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings:_*).
  settings(publishingSettings:_*).
  settings(
    name := "slogging-winston"
  )

lazy val http = project.
  dependsOn(sloggingJS).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings:_*).
  settings(publishingSettings:_*).
  settings(
    name := "slogging-http",
    libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.8.0"
  )


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
 
