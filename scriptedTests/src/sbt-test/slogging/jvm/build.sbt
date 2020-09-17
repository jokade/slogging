import scala.sys.process._

val expectedOutput =
"""[error, Main$] error[error, Main$] error[warn, Main$] warn[error, Main$] error[warn, Main$] warn[info, Main$] info[error, Main$] error[warn, Main$] warn[info, Main$] info[debug, Main$] debug[error, Main$] error[warn, Main$] warn[info, Main$] info[debug, Main$] debug[trace, Main$] trace"""

lazy val root = (project in file("."))
  .settings(
    scalaVersion := "2.13.1",
    sys.props.get("lib.version") match {
      case Some(x) => libraryDependencies += "biz.enef" %% "slogging" % x
      case _ => sys.error("""The system property 'lib.version' is not defined.""")
    },
    assemblyJarName in assembly := "test.jar",
    TaskKey[Unit]("check") := {
      val stdout = new StringBuilder
      val stderr = new StringBuilder
      val process = Process("java", Seq("-jar", (crossTarget.value / "test.jar").toString))
      val out = (process !! ProcessLogger(stdout append _, stderr append _))
      if (stderr.toString() != expectedOutput) sys.error("expected output: |"+expectedOutput +"|\nactual:                  |" + stderr +"|")
    }
  )
