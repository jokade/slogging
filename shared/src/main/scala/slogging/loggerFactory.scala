// -   Project: slogging (https://github.com/jokade/slogging)
//      Module: shared
// Description: Provides implementations for LoggerS and LoggerFactoryS
//
// Copyright (c) 2015 Johannes Kastner <jkspam@karchedon.de>
//               Distributed under the MIT License (see included file LICENSE)
package slogging

import java.util.Date

/**
 * Common interface for LoggerS (this interface is compatible to the slf4j logging API)
 */
trait UnderlyingLogger {

  def isErrorEnabled: Boolean
  def isWarnEnabled: Boolean
  def isInfoEnabled: Boolean
  def isDebugEnabled: Boolean
  def isTraceEnabled: Boolean

  // Error

  def error(message: String) : Unit

  def error(message: String, cause: Throwable) : Unit

  def error(message: String, args: AnyRef*) : Unit

  // Warn

  def warn(message: String): Unit

  def warn(message: String, cause: Throwable): Unit

  def warn(message: String, args: AnyRef*): Unit

  // Info

  def info(message: String) : Unit

  def info(message: String, cause: Throwable) : Unit

  def info(message: String, args: AnyRef*) : Unit

  // Debug

  def debug(message: String): Unit

  def debug(message: String, cause: Throwable): Unit

  def debug(message: String, args: AnyRef*): Unit

  // Trace

  def trace(message: String): Unit

  def trace(message: String, cause: Throwable): Unit

  def trace(message: String, args: AnyRef*): Unit
}

abstract class AbstractUnderlyingLogger extends UnderlyingLogger {
  @inline
  final def isErrorEnabled: Boolean = LoggerConfig.level >= LogLevel.ERROR
  @inline
  final def isWarnEnabled: Boolean = LoggerConfig.level >= LogLevel.WARN
  @inline
  final def isInfoEnabled: Boolean = LoggerConfig.level >= LogLevel.INFO
  @inline
  final def isDebugEnabled: Boolean = LoggerConfig.level >= LogLevel.DEBUG
  @inline
  final def isTraceEnabled: Boolean = LoggerConfig.level >= LogLevel.TRACE
}

object NullLogger extends UnderlyingLogger {
  override val isErrorEnabled: Boolean = false
  override val isWarnEnabled: Boolean = false
  override val isInfoEnabled: Boolean = false
  override val isDebugEnabled: Boolean = false
  override val isTraceEnabled: Boolean = false

  override def error(message: String): Unit = {}
  override def error(message: String, cause: Throwable): Unit = {}
  override def error(message: String, args: AnyRef*): Unit = {}

  override def warn(message: String): Unit = {}
  override def warn(message: String, cause: Throwable): Unit = {}
  override def warn(message: String, args: AnyRef*): Unit = {}

  override def info(message: String): Unit = {}
  override def info(message: String, cause: Throwable): Unit = {}
  override def info(message: String, args: AnyRef*): Unit = {}

  override def debug(message: String): Unit = {}
  override def debug(message: String, cause: Throwable): Unit = {}
  override def debug(message: String, args: AnyRef*): Unit = {}

  override def trace(message: String): Unit = {}
  override def trace(message: String, cause: Throwable): Unit = {}
  override def trace(message: String, args: AnyRef*): Unit = {}
}

object NullLoggerFactory extends UnderlyingLoggerFactory {
  override def getUnderlyingLogger(name: String): UnderlyingLogger = NullLogger
}

object LogLevel extends Enumeration {
  val OFF = Value(0)
  val ERROR = Value(1)
  val WARN = Value(2)
  val INFO = Value(3)
  val DEBUG = Value(4)
  val TRACE = Value(5)
}

object PrintLogger {
  private var _printLoggerName: Boolean = true
  private var _printTimestamp: Boolean = false
  @inline
  def printLoggerName : Boolean = _printLoggerName
  def printLoggerName_=(f: Boolean) = this.synchronized( _printLoggerName = f )
  @inline
  def printTimestamp : Boolean = _printTimestamp
  def printTimestamp_=(f: Boolean) = this.synchronized( _printTimestamp = f )
}

final class PrintLogger(name: String) extends AbstractUnderlyingLogger {
  @inline
  private def getTimestamp() = new Date().toString
  @inline
  def prefix(level: String) =
    if(PrintLogger.printTimestamp) {
      if(PrintLogger.printLoggerName) s"[${getTimestamp()}, $level, $name]" else s"[${getTimestamp()}, $level]"
    } else {
      if(PrintLogger.printLoggerName) s"[$level, $name]" else s"[$level]"
    }
  @inline
  def msg(level: String, msg: String) = println(s"${prefix(level)} $msg")
  @inline
  def msg(level: String, msg: String, cause: Throwable) = println(s"${prefix(level)} $msg\n    $cause")
  @inline
  def msg(level: String, msg: String, args: AnyRef*) = println(s"${prefix(level)} ${String.format(msg,args)}")

  override def error(message: String): Unit = msg("ERROR",message)
  override def error(message: String, cause: Throwable): Unit = msg("ERROR",message,cause)
  override def error(message: String, args: AnyRef*): Unit = msg("ERROR",message,args)

  override def warn(message: String): Unit = msg("WARN",message)
  override def warn(message: String, cause: Throwable): Unit = msg("WARN",message,cause)
  override def warn(message: String, args: AnyRef*): Unit = msg("WARN",message,args)

  override def info(message: String): Unit = msg("INFO",message)
  override def info(message: String, cause: Throwable): Unit = msg("INFO",message,cause)
  override def info(message: String, args: AnyRef*): Unit = msg("INFO",message,args)

  override def debug(message: String): Unit = msg("DEBUG",message)
  override def debug(message: String, cause: Throwable): Unit = msg("DEBUG",message,cause)
  override def debug(message: String, args: AnyRef*): Unit = msg("DEBUG",message,args)

  override def trace(message: String): Unit = msg("TRACE",message)
  override def trace(message: String, cause: Throwable): Unit = msg("TRACE",message,cause)
  override def trace(message: String, args: AnyRef*): Unit = msg("TRACE",message,args)
}


object PrintLoggerFactory extends UnderlyingLoggerFactory {
  override def getUnderlyingLogger(name: String): UnderlyingLogger = new PrintLogger(name)
}

trait UnderlyingLoggerFactory {
  def getUnderlyingLogger(name: String) : UnderlyingLogger
  def apply() : UnderlyingLoggerFactory = this
}

object LoggerFactory {
  def getLogger(name: String) : Logger = Logger( LoggerConfig.factory.getUnderlyingLogger(name) )
}

object LoggerConfig {
  private var _factory : UnderlyingLoggerFactory = NullLoggerFactory
  private var _level : LogLevel.Value = LogLevel.INFO
  @inline
  def factory = _factory
  def factory_=(f: UnderlyingLoggerFactory) = this.synchronized{ _factory = f }
  @inline
  def level = _level
  def level_=(l: LogLevel.Value) = this.synchronized{ _level = l }
}
