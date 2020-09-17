// -   Project: slogging (https://github.com/jokade/slogging)
//      Module: JS
// Description: ScalaJS logger implementation that redirects to the browser console
//
// Copyright (c) 2015 Johannes Kastner <jkspam@karchedon.de>
//               Distributed under the MIT License (see included file LICENSE)
package slogging

import scalajs.js.Dynamic.global


/**
 * ScalaJS logger implementation that redirects to the browser console.
 *
 */
object ConsoleLogger extends AbstractUnderlyingLogger {
  import LoggingUtils.argsBracketFormat
  private var _printLoggerName = true
  /**
   * If true, all logger outputs are prefixed by the logger name
   */
  @inline
  def printLoggerName : Boolean = _printLoggerName
  def printLoggerName_=(f: Boolean): Unit = this.synchronized { _printLoggerName = f }

  private val console = global.console

  @inline
  final def prefix(level: String, src: String) = if(ConsoleLogger.printLoggerName) s"[$level, $src]" else s"[$level]"
  @inline
  final def msg(level: String, src: String, msg: String) = s"${prefix(level,src)} $msg"
  @inline
  final def msg(level: String, src: String, msg: String, cause: Throwable) = s"${prefix(level,src)} $msg\n    $cause"
  @inline
  final def msg(level: String, src: String, msg: String, args: Any*) = s"${prefix(level,src)} ${argsBracketFormat(msg,args)}"


  override def error(source: String, message: String): Unit = console.error(msg("ERROR",source,message))
  override def error(source: String, message: String, cause: Throwable): Unit = console.error(msg("ERROR",source,message,cause))
  override def error(source: String, message: String, args: Any*): Unit = console.error(msg("ERROR",source,message,args:_*))

  override def warn(source: String, message: String): Unit = console.warn(msg("WARN",source,message))
  override def warn(source: String, message: String, cause: Throwable): Unit = console.warn(msg("WARN",source,message,cause))
  override def warn(source: String, message: String, args: Any*): Unit = console.warn(msg("WARN",source,message,args:_*))

  override def info(source: String, message: String): Unit = console.info(msg("INFO",source,message))
  override def info(source: String, message: String, cause: Throwable): Unit = console.info(msg("INFO",source,message,cause))
  override def info(source: String, message: String, args: Any*): Unit = console.info(msg("INFO",source,message,args:_*))

  override def debug(source: String, message: String): Unit = console.debug(msg("DEBUG",source,message))
  override def debug(source: String, message: String, cause: Throwable): Unit = console.debug(msg("DEBUG",source,message,cause))
  override def debug(source: String, message: String, args: Any*): Unit = console.debug(msg("DEBUG",source,message,args:_*))

  override def trace(source: String, message: String): Unit = console.trace(msg("TRACE",source,message))
  override def trace(source: String, message: String, cause: Throwable): Unit = console.trace(msg("TRACE",source,message,cause))
  override def trace(source: String, message: String, args: Any*): Unit = console.trace(msg("TRACE",source,message,args:_*))

}

object ConsoleLoggerFactory extends UnderlyingLoggerFactory {
  override def getUnderlyingLogger(name: String): UnderlyingLogger = ConsoleLogger
}
