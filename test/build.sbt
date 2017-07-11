val sloggingVersion = "0.5.3-SNAPSHOT"

lazy val commonSettings = Seq(
  scalaVersion := "2.12.0",
  scalacOptions ++= Seq("-deprecation","-unchecked","-feature","-Xlint"),
  publish := {},
  publishLocal := {},
  resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
)

lazy val root = project.in(file(".")).
  aggregate(testsJVM,testsJS).
  settings(commonSettings:_*)

lazy val tests = crossProject.in(file(".")).
  settings(commonSettings:_*).
  settings(
    name := "slogging-test",
    //resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
    libraryDependencies ++= Seq(
      "biz.enef" %%% "slogging" % sloggingVersion
    )
    //scalacOptions += "-Xmacro-settings:slogging.disable"
  ).
  jvmSettings(
    libraryDependencies ++= Seq(
      "biz.enef" %%  "slogging-slf4j" % sloggingVersion,
      "org.slf4j" %  "slf4j-simple" % "1.7.+"
    )
  ).
  jsSettings(
    preLinkJSEnv := NodeJSEnv().value,
    postLinkJSEnv := NodeJSEnv().value,
    libraryDependencies ++= Seq(
      "biz.enef" %%%  "slogging-winston" % sloggingVersion,
      "biz.enef" %%%  "slogging-http" % sloggingVersion
    )
  )

lazy val testsJVM = tests.jvm
lazy val testsJS = tests.js


