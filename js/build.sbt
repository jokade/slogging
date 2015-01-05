import ScalaJSKeys._

name := "slogging-js"

scalaJSSettings

unmanagedSourceDirectories in Compile += baseDirectory.value / "shared" / "main" / "scala"

