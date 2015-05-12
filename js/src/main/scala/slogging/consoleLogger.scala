// -   Project: slogging (https://github.com/jokade/slogging)
//      Module: JS
// Description: ScalaJS logger implementation that redirects to the browser console
//
// Copyright (c) 2015 Johannes Kastner <jkspam@karchedon.de>
//               Distributed under the MIT License (see included file LICENSE)
package slogging

import scalajs.js.Dynamic.global

object ConsoleLogger {
  /**
   * Logging level for all ConsoleLogger instances
   */
  var level: LogLevel.Value = LogLevel.INFO

  /**
   * If true, all logger outputs are prefixed by the logger name
   */
  var printLoggerName : Boolean = true
}

/**
 * ScalaJS logger implementation that redirects to the browser console.
 *
 * @param name logger name
 */
class ConsoleLogger(name: String) extends UnderlyingLogger {
  val console = global.console

  def prefix(level: String) = if(ConsoleLogger.printLoggerName) s"[$level, $name]" else s"[$level]"
  def msg(level: String, msg: String) = s"${prefix(level)} $msg"
  def msg(level: String, msg: String, cause: Throwable) = s"${prefix(level)} $msg\n    $cause"
  def msg(level: String, msg: String, args: AnyRef*) = s"${prefix(level)} ${String.format(msg,args)}"


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
