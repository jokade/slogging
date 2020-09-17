import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

version in ThisBuild := "0.6.2-SNAPSHOT"

lazy val commonSettings = Seq(
  organization := "biz.enef",
//  version := "0.6.2-SNAPSHOT",
  scalaVersion := "2.13.1",
  scalacOptions ++= Seq("-deprecation","-unchecked","-feature","-Xlint")
)

lazy val commonNativeSettings = Seq(
  unmanagedSourceDirectories in Compile ++= {
    val sourceDir = (sourceDirectory in Compile).value
    if(nativeVersion.startsWith("0.3.")) Some(sourceDir / "scala-native-0.3")
    else if(nativeVersion.startsWith("0.4.")) Some(sourceDir / "scala-native-0.4")
    else None
  },
  scalaVersion := "2.11.12",
  crossScalaVersions := Seq("2.11.12"),
  nativeLinkStubs := true
)

lazy val root = project.in(file(".")).
  aggregate(
    sloggingJVM,sloggingJS,sloggingNative,
    //sloggingConfigJVM,sloggingConfigJS,sloggingConfigNative,
    slf4j,winston,http,syslog,glib).
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
  .settings(commonSettings)
  .settings(publishingSettings)
  .settings(
    name := "slogging",
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % scalaVersion.value,
      "com.lihaoyi" %%% "utest" % "0.7.4" % Test
    ),
    testFrameworks += new TestFramework("utest.runner.Framework"),
    crossScalaVersions := Seq("2.12.10", "2.13.1")
  )
  .jsSettings(
    //preLinkJSEnv := NodeJSEnv().value,
    //postLinkJSEnv := NodeJSEnv().value
  )
  .nativeSettings(commonNativeSettings)
  .enablePlugins(SbtOsgi).settings(sloggingOsgiSettings)

lazy val sloggingJVM    = slogging.jvm
lazy val sloggingJS     = slogging.js
lazy val sloggingNative = slogging.native

/*
lazy val sloggingConfig = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .crossType(CrossType.Full)
  .dependsOn(slogging)
  .settings(commonSettings ++ publishingSettings: _*)
  .settings(
    name := "slogging-config",
    libraryDependencies ++= Seq(
      "de.surfice" %%% "sconfig" % "0.0.1-SNAPSHOT" % "provided"
    )
  )


lazy val sloggingConfigJVM    = sloggingConfig.jvm
lazy val sloggingConfigJS     = sloggingConfig.js
lazy val sloggingConfigNative = sloggingConfig.native
*/

lazy val slf4j = project
  .dependsOn(sloggingJVM)
  .settings(commonSettings)
  .settings(publishingSettings)
  .settings(
    name := "slogging-slf4j",
    libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.12"
  )
  .enablePlugins(SbtOsgi).settings(sloggingOsgiSettings)

lazy val winston = project
  .dependsOn(sloggingJS)
  .enablePlugins(ScalaJSPlugin)
  .settings(
    name := "slogging-winston",
    commonSettings,
    publishingSettings
  )

lazy val http = project
  .dependsOn(sloggingJS)
  .enablePlugins(ScalaJSPlugin)
  .settings(
    name := "slogging-http",
    libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "1.0.0",
    commonSettings,
    publishingSettings
  )

lazy val syslog = project
  .dependsOn(sloggingNative)
  .enablePlugins(ScalaNativePlugin)
  .settings(
    name := "slogging-syslog",
    commonSettings,
    commonNativeSettings,
    publishingSettings
  )

lazy val glib = project
  .dependsOn(sloggingNative)
  .settings(
    name := "slogging-glib",
    nativeLinkingOptions ++= Seq("-lglib-2.0"),
    commonSettings,
    commonNativeSettings,
    publishingSettings
  )
  .enablePlugins(ScalaNativePlugin)

lazy val scriptedTests = project
  .enablePlugins(SbtPlugin)
  .settings(
    scriptedLaunchOpts := { scriptedLaunchOpts.value ++
      Seq("-Xmx1024M", "-Dlib.version=" + version.value)
    },
    scriptedBufferLog := false
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
  useGpg := false,
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
 
