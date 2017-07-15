
lazy val commonSettings = Seq(
  organization := "biz.enef",
  version := "0.5.4-SNAPSHOT",
  scalaVersion := "2.11.11",
  scalacOptions ++= Seq("-deprecation","-unchecked","-feature","-Xlint"),
  crossScalaVersions := Seq("2.11.11", "2.12.2")
)

lazy val root = project.in(file(".")).
  aggregate(sloggingJVM,sloggingJS,slf4j,winston,http).
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

lazy val slogging = crossProject.in(file(".")).
  settings(commonSettings:_*).
  settings(publishingSettings:_*).
  settings(
    name := "slogging",
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % scalaVersion.value,
      "com.lihaoyi" %%% "utest" % "0.4.4" % "test"
    ),
    testFrameworks += new TestFramework("utest.runner.Framework")
  ).
  jvmSettings(
  ).
  jsSettings(
    //preLinkJSEnv := NodeJSEnv().value,
    //postLinkJSEnv := NodeJSEnv().value
  ).
  enablePlugins(SbtOsgi).settings(sloggingOsgiSettings:_*)

lazy val sloggingJVM = slogging.jvm
lazy val sloggingJS = slogging.js

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
 
