//     Project: slibs / logging
//      Author: Johannes Kastner
// Description: ScalaJS logger implementation that redirects to the browser console
package logging

import scalajs.js.Dynamic.global

object ConsoleLogger {
  var level: LogLevel.Value = LogLevel.INFO
  var printLoggerName : Boolean = true
}

class ConsoleLogger(name: String) extends UnderlyingLogger {
  val console = global.console

  def prefix(level: String) = if(ConsoleLogger.printLoggerName) s"[$level, $name]" else s"[$level]"
  def msg(level: String, msg: String) = s"${prefix(level)} $msg"
  def msg(level: String, msg: String, cause: Throwable) = s"${prefix(level)} $msg\n    $cause"
  def msg(level: String, msg: String, args: AnyRef*) = s"${prefix(level)} ${String.format(msg,args)}"

  override def isErrorEnabled: Boolean = ConsoleLogger.level >= LogLevel.ERROR
  override def isWarnEnabled: Boolean = ConsoleLogger.level >= LogLevel.WARN
  override def isInfoEnabled: Boolean = ConsoleLogger.level >= LogLevel.INFO
  override def isDebugEnabled: Boolean = ConsoleLogger.level >= LogLevel.DEBUG
  override def isTraceEnabled: Boolean = ConsoleLogger.level >= LogLevel.TRACE

  override def error(message: String): Unit = console.error(msg("ERROR",message))
  override def error(message: String, cause: Throwable): Unit = console.error(msg("ERROR",message,cause))
  override def error(message: String, args: AnyRef*): Unit = console.error(msg("ERROR",message,args))

  override def warn(message: String): Unit = console.warn(msg("WARN",message))
  override def warn(message: String, cause: Throwable): Unit = console.warn(msg("WARN",message,cause))
  override def warn(message: String, args: AnyRef*): Unit = console.warn(msg("WARN",message,args))

  override def info(message: String): Unit = console.info(msg("INFO",message))
  override def info(message: String, cause: Throwable): Unit = console.info(msg("INFO",message,cause))
  override def info(message: String, args: AnyRef*): Unit = console.info(msg("INFO",message,args))

  override def debug(message: String): Unit = console.debug(msg("DEBUG",message))
  override def debug(message: String, cause: Throwable): Unit = console.debug(msg("DEBUG",message,cause))
  override def debug(message: String, args: AnyRef*): Unit = console.debug(msg("DEBUG",message,args))

  override def trace(message: String): Unit = console.trace(msg("TRACE",message))
  override def trace(message: String, cause: Throwable): Unit = console.trace(msg("TRACE",message,cause))
  override def trace(message: String, args: AnyRef*): Unit = console.trace(msg("TRACE",message,args))
}

object ConsoleLoggerFactory extends UnderlyingLoggerFactory {
  override def getUnderlyingLogger(name: String): UnderlyingLogger = new ConsoleLogger(name)
}
