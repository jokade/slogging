val sloggingVersion = "0.3-SNAPSHOT"

lazy val commonSettings = Seq(
  scalaVersion := "2.11.6",
  scalacOptions ++= Seq("-deprecation","-unchecked","-feature","-Xlint"),
  publish := {},
  publishLocal := {},
  resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
)

lazy val root = project.in(file(".")).
  aggregate(sloggingJVM,sloggingJS).
  settings(commonSettings:_*)

lazy val slogging = crossProject.in(file(".")).
  settings(commonSettings:_*).
  settings(
    name := "slogging-test",
  resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
    libraryDependencies ++= Seq(
      "biz.enef" %%% "slogging" % sloggingVersion,
      "biz.enef" %%  "slogging-slf4j" % sloggingVersion ,
      "biz.enef" %%%  "slogging-winston" % sloggingVersion ,
      "org.slf4j" %  "slf4j-simple" % "1.7.+"
    )
  ).
  jvmSettings(
  ).
  jsSettings(
    preLinkJSEnv := NodeJSEnv().value,
    postLinkJSEnv := NodeJSEnv().value
  )

lazy val sloggingJVM = slogging.jvm
lazy val sloggingJS = slogging.js


