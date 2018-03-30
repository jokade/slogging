// (5) shadow sbt-scalajs' crossProject and CrossType until Scala.js 1.0.0 is released
import sbtcrossproject.{crossProject, CrossType}

lazy val commonSettings = Seq(
  organization := "biz.enef",
  version := "0.6.2-SNAPSHOT",
  scalaVersion := "2.11.12",
  scalacOptions ++= Seq("-deprecation","-unchecked","-feature","-Xlint")
  //crossScalaVersions := Seq("2.11.11", "2.12.2")
)

lazy val root = project.in(file(".")).
  aggregate(sloggingJVM,sloggingJS,sloggingNative,slf4j,winston,http,syslog,glib).
  settings(commonSettings:_*).
  //settings(sonatypeSettings: _*).
  settings(
    name := "slogging",
    publish := {},
    publishLocal := {},
    resolvers += Resolver.sonatypeRepo("releases")
  )

lazy val sloggingOsgiSettings = osgiSettings ++ Seq(
  OsgiKeys.exportPackage := Seq("slogging.*;version=${Bundle-Version}")
)

lazy val slogging = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .crossType(CrossType.Full)
  .in(file("."))
  .settings(commonSettings:_*)
  .settings(publishingSettings:_*)
  .settings(
    name := "slogging",
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % scalaVersion.value,
      "com.lihaoyi" %%% "utest" % "0.6.3" % "test"
    ),
    testFrameworks += new TestFramework("utest.runner.Framework")
  )
  .jvmSettings(
    crossScalaVersions := Seq("2.11.12", "2.12.5")
  )
  .jsSettings(
    crossScalaVersions := Seq("2.11.12", "2.12.5")
    //preLinkJSEnv := NodeJSEnv().value,
    //postLinkJSEnv := NodeJSEnv().value
  )
  .enablePlugins(SbtOsgi).settings(sloggingOsgiSettings:_*)

lazy val sloggingJVM    = slogging.jvm
lazy val sloggingJS     = slogging.js
lazy val sloggingNative = slogging.native

lazy val slf4j = project.
  dependsOn(sloggingJVM).
  settings(commonSettings:_*).
  settings(publishingSettings:_*).
  settings(
    name := "slogging-slf4j",
    libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.12"
  ).
  enablePlugins(SbtOsgi).settings(sloggingOsgiSettings:_*)

lazy val winston = project.
  dependsOn(sloggingJS).
  enablePlugins(ScalaJSPlugin).
  settings(
    commonSettings ++
    publishingSettings :_*).
  settings(
    name := "slogging-winston"
  )

lazy val http = project.
  dependsOn(sloggingJS).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings ++
    publishingSettings:_*).
  settings(
    name := "slogging-http",
    libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.1"
  )

lazy val syslog = project
  .dependsOn(sloggingNative)
  .enablePlugins(ScalaNativePlugin)
  .settings(
    commonSettings ++
    publishingSettings :_*)
  .settings(
    name := "slogging-syslog"
  )

lazy val glib = project
  .dependsOn(sloggingNative)
  .enablePlugins(ScalaNativePlugin)
  .settings(commonSettings ++ publishingSettings :_*)
  .settings(
    name := "slogging-glib",
    nativeLinkingOptions ++= Seq("-lglib-2.0")
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
 
