name := "slogging-jvm"

scalaSource in Compile := baseDirectory.value / "shared" / "main" / "scala"

publishTo := Some(Resolver.file("file", baseDirectory.value / ".." / "maven"  ))
