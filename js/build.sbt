import ScalaJSKeys._

name := "slogging-js"

scalaJSSettings

unmanagedSourceDirectories in Compile += baseDirectory.value / "shared" / "main" / "scala"

publishTo := Some(Resolver.file("file", baseDirectory.value / ".." / "maven"  ))
