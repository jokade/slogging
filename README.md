slogging
========

A simple logging library for Scala and Scala.js with various backends. Slogging is compatible to the [scala-logging](https://github.com/typesafehub/scala-logging) (and slf4j) API, and uses macros to check if logging statements should be executed.

#### Contents:
* [Getting Started](#getting-started)
  * [SBT Settings](#sbt-settings)
  * [Logging and Configuration](#logging-and-configuration)
* [Backends](#backends)


Getting Started
---------------

### SBT Settings
Add one of the following lines to your `build.sbt` (depending on your target):

**Scala/JVM** with logging to stdout:
```scala
libraryDependencies += "biz.enef" %% "slogging" % "0.3-SNAPSHOT"
```
with slf4j:
```scala
libraryDependencies ++= Seq(
  "biz.enef" %% "slogging-slf4j" % "0.3-SNAPSHOT",
  "org.slf4j" % "slf4j-simple" % "1.7.+"  // or another slf4j implementation
)
```

**Scala.js** with logging to console:
```scala
libraryDependencies += "biz.enef" %%% "slogging" % "0.3-SNAPSHOT"
```
with [winston](https://www.npmjs.com/package/winston) (Node.js):
```scala
libraryDependencies += "biz.enef" %%% "slogging-winston" % "0.3-SNAPSHOT"
```

slogging 0.3-SNAPSHOT is published for Scala 2.11.x and Scala.js 0.6.2+.

### Logging and Configuration
#### Add logging statements
Mix one of two traits into your class/object to give it logging capability:
* `StrictLogging`: the `logger` is initialized when the instance is created
* `LazyLogging`: the `logger` is initialized on first access

Example:
```scala
import slogging.LazyLogging

class Foo extends LazyLogging {
  def bar() {
    // log an error message
    logger.error("...")
    // log a warning
    logger.warn("...")
    // log an info message
    logger.info("...")
    // log a debug message
    logger.debug("...")
    // log a tracing message
    logger.trace("...")
  }
}
```

#### Activate logging and set log level
The default logger is a `NullLogger` which simply ignores all logging statements. To activate logging output, assign a different logger factory to `LoggerConfig.factory`, or call the `apply()` method of the logger factory you want to use. Assign a new value to `LoggerConfig.level` to change the current log level. Example:
```scala
import slogging._

object Main extends App {
  // activate simple logging using println (supported by Scala/JVM and Scala.js) 
  LoggerConfig.factory = PrintLoggerFactory()

  // - or, use SLF4J on JVM
  // LoggerConfig.factory = SLF4JLoggerFactory()
  
  // - or, use console.log with Scala.js
  // LoggerConfig.factory = ConsoleLoggerFactory()
  
  // - or with winston / Node.js
  // LoggerConfig.factory = WinstonLoggerFactory()
  
  // set log level to DEBUG
  LoggerConfig.level = LogLevel.DEBUG
}
```
**Note**: Some backends (slf4j, winston) have their own log level management, which needs to be adjusted as well.


Backends
--------
### Scala / JVM
slogging supports the following logger backends on Scala (JVM):
#### PrintLoggerFactory
TBD

#### SLF4JLoggerFactory
TBD

### Scala.js
slogging support the following logger backends for Scala.js:
#### PrintLoggerFactory
See PrintLoggerFactory for Scala / JVM

#### ConsoleLoggerFactory
TBD

#### WinstonLoggerFactory
TBD

License
-------
This code is open source software licensed under the [MIT License](http://opensource.org/licenses/MIT)
