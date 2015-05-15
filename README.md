slogging
========

A simple logging library for Scala and Scala.js with various backends. Slogging is compatible to the [scala-logging](https://github.com/typesafehub/scala-logging) (and slf4j) API, and uses macros to check if logging statements should be executed.

**News:** Version 0.3 has been released ([release notes](https://github.com/jokade/slogging/wiki/Release-Notes))!

#### Contents:
* [Getting Started](#getting-started)
  * [SBT Settings](#sbt-settings)
  * [Logging and Configuration](#logging-and-configuration)
* [Backends](#backends)
  * [Scala / JVM](#scala--jvm)
    * [PrintLogger](#printloggerfactory)
    * [SLF4J](#slf4jloggerfactory)
  * [Scala.js](#scalajs)
    * [PrintLogger](#printloggerfactory-1)
    * [ConsoleLogger](#consoleloggerfactory)
    * [Winston (Node.js)](#winstonloggerfactory)
    * [Remote HTTP](#httploggerfactory)

Getting Started
---------------

### SBT Settings
Add one of the following lines to your `build.sbt` (depending on your target):

**Scala/JVM** with logging to stdout:
```scala
libraryDependencies += "biz.enef" %% "slogging" % "0.3"
```
with slf4j:
```scala
libraryDependencies ++= Seq(
  "biz.enef" %% "slogging-slf4j" % "0.3",
  "org.slf4j" % "slf4j-simple" % "1.7.+"  // or another slf4j implementation
)
```

**Scala.js** with logging to console:
```scala
libraryDependencies += "biz.enef" %%% "slogging" % "0.3"
```
with [winston](https://www.npmjs.com/package/winston) (Node.js):
```scala
libraryDependencies += "biz.enef" %%% "slogging-winston" % "0.3"
```
with remote logging via HTTP POST:
```scala
libraryDependencies += "biz.enef" %%% "slogging-http" % "0.3"
```

slogging 0.3 is published for Scala 2.11.x and Scala.js 0.6.2+.

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

#### Disable logging at compile time
It is possible to omit logging statements from the generated code altogether. To this end add the following flag to your `build.sbt`:
```scala
scalacOptions += "-Xmacro-settings:slogging.disable"
```

Backends
--------
### Scala / JVM
slogging supports the following logger backends on Scala (JVM):
#### PrintLoggerFactory
A simple backend that prints all log messages to stdout.

**Usage:**
```scala
// build.sbt
libraryDependencies += "biz.enef" %% "slogging" % "VERSION"
```
```scala
import slogging._

// activate PrintLogger; no additional configuration required
LoggerConfig.factory = PrintLoggerFactory()
```

#### SLF4JLoggerFactory
This backend is just a wrapper around [slf4j](http://www.slf4j.org).

**Usage:**
```scala
// build.sbt
libraryDependencies ++= Seq(
  "biz.enef" %% "slogging-slf4j" % "VERSION",
  "org.slf4j" % "slf4j-simple" % "1.7.+"  // or another slf4j implementation
)
```
```scala
import slogging._

// activate SLF4J backend
LoggerConfig.factory = SLF4JFactory()
```

### Scala.js
slogging support the following logger backends for Scala.js:
#### PrintLoggerFactory
A simple backend that prints all log messages to the console unsing Scala's `println()`.

**Usage:**
```scala
// build.sbt
libraryDependencies += "biz.enef" %%% "slogging" % "VERSION"
```
```scala
import slogging._

// activate PrintLogger; no additional configuration required
LoggerConfig.factory = PrintLoggerFactory()
```

#### ConsoleLoggerFactory
Similar to PrintLoggerFactory, but uses `console.log` instead of `println()`.

**Usage:**
```scala
// build.sbt
libraryDependencies += "biz.enef" %%% "slogging" % "VERSION"
```
```scala
import slogging._

// activate ConsoleLogger; no additional configuration required
LoggerConfig.factory = ConsoleLoggerFactory()
```

#### WinstonLoggerFactory
A wrapper around the [winston](https://www.npmjs.com/package/winston) logging library for Node.js.

**Usage:**
```scala
// build.sbt
libraryDependencies += "biz.enef" %%% "slogging-winston" % "VERSION"
```
```scala
import slogging._

// use default winston logger
LoggerConfig.factory = WinstonLoggerFactory()

// use a custom winston logger
import WinstonLoggerFactory.{WinstonLogger, winston}

LoggerConfig.factory = WinstonLoggerFactory(WinstonLogger(literal(
  levels = js.Dictionary(
    "trace" -> 0,
    "debug" -> 1,
    "info"  -> 2,
    "warn"  -> 3,
    "error" -> 4
  ),
  transports = js.Array(
    js.Dynamic.newInstance(winston.transports.Console)(literal(
      level = "debug"
    ))
  )
)))
```

#### HttpLoggerFactory
This backend sends log messages to a HTTP server via Ajax POST requests. Note that the same origin policy usually requires that the server to which the messages are sent must be the same server from which the javascript source was loaded.

**Usage:**
```scala
import slogging._

// function that assembles the JSON object to be sent
// (only required if you want to override the default formatter)
val fmt: HttpLoggerFactory.MessageFormatter = (clientId,level,name,msg,cause) => js.Dynamic.literal(
  id = clientId,
  loggerName = name,
  logMessage = msg
)

// configure & activate remote HTTP logging 
LoggerConfig.factory = 
  HttpLoggerFactory("/logging", // target URL for log messages
                    "client1",  // ID of the client that sends the messages (optional)
                    fmt         // message formatting function (optional)
                   ) 
```

License
-------
This code is open source software licensed under the [MIT License](http://opensource.org/licenses/MIT)
