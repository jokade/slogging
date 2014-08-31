import ScalaJSKeys._

name := "logging-js"

scalaJSSettings

//scalaSource in Compile := baseDirectory.value / ".." / "src"
unmanagedSourceDirectories in Compile += baseDirectory.value / ".." / "src"
