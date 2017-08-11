// -   Project: slogging (https://github.com/jokade/slogging)
//      Module: shared
// Description: A logger implementation that wraps multiple UnderlyingLoggers.
//
// Distributed under the MIT License (see included file LICENSE)
package slogging

final class MultiLogger(loggers: Iterable[UnderlyingLogger]) extends UnderlyingLogger {
  @inline def isErrorEnabled: Boolean = LoggerConfig.level >= LogLevel.ERROR
  @inline def isWarnEnabled: Boolean = LoggerConfig.level >= LogLevel.WARN
  @inline def isInfoEnabled: Boolean = LoggerConfig.level >= LogLevel.INFO
  @inline def isDebugEnabled: Boolean = LoggerConfig.level >= LogLevel.DEBUG
  @inline def isTraceEnabled: Boolean = LoggerConfig.level >= LogLevel.TRACE

  override def error(source: String, message: String): Unit = loggers.foreach(_.error(source,message))
  override def error(source: String, message: String, cause: Throwable): Unit = loggers.foreach(_.error(source,message,cause))
  override def error(source: String, message: String, args: Any*): Unit = loggers.foreach(_.error(source,message,args:_*))

  override def warn(source: String, message: String): Unit = loggers.foreach(_.warn(source,message))
  override def warn(source: String, message: String, cause: Throwable): Unit = loggers.foreach(_.warn(source,message,cause))
  override def warn(source: String, message: String, args: Any*): Unit = loggers.foreach(_.warn(source,message,args:_*))

  override def info(source: String, message: String): Unit = loggers.foreach(_.info(source,message))
  override def info(source: String, message: String, cause: Throwable): Unit = loggers.foreach(_.info(source,message,cause))
  override def info(source: String, message: String, args: Any*): Unit = loggers.foreach(_.info(source,message,args:_*))

  override def debug(source: String, message: String): Unit = loggers.foreach(_.debug(source,message))
  override def debug(source: String, message: String, cause: Throwable): Unit = loggers.foreach(_.debug(source,message,cause))
  override def debug(source: String, message: String, args: Any*): Unit = loggers.foreach(_.debug(source,message,args:_*))

  override def trace(source: String, message: String): Unit = loggers.foreach(_.trace(source,message))
  override def trace(source: String, message: String, cause: Throwable): Unit = loggers.foreach(_.trace(source,message,cause))
  override def trace(source: String, message: String, args: Any*): Unit = loggers.foreach(_.trace(source,message,args:_*))

}

object MultiLogger {
  def apply(loggers: UnderlyingLogger*): MultiLogger = new MultiLogger(loggers)
}
