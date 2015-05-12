slogging
========

A simple logging library for Scala and Scala.js. Slogging is compatible to [scala-logging](https://github.com/typesafehub/scala-logging) (and hence slf4j) and uses macros to check if logging statements should be executed.

Getting Started
---------------

### sbt settings
Add one of the following lines to your `build.sbt` (depending on your target):

**Scala/JVM** with logging to stdout:
```scala
libraryDependencies += "biz.enef" %% "slogging" % "0.2"
```

**Scala.js** with logging to console:
```scala
libraryDependencies += "biz.enef" %%% "slogging" % "0.2"
```

slogging 0.2 is published for Scala 2.11.x and Scala.js 0.6.x.

### Add logging statements
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

### Activate logging and set log level
The default logger is a `NullLogger` which simply ignores all logging statements. To activate logging output, assign a different logger factory to `LoggerFactory.current`, or call the `apply()` method of the logger factory you want to use. Assign a new value to `LogLevel.current` to change the current log level. Example:
```scala
import slogging.{PrintLoggerFactory, LogLevel}

object Main extends App {
  // activate simple logging using println
  PrintLoggerFactory()
  
  // set log level to DEBUG
  LogLevel.current = LogLevel.DEBUG
}
```

License
-------
This code is open source software licensed under the [MIT License](http://opensource.org/licenses/MIT)
