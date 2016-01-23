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

  def error(source: String, message: String) : Unit

  def error(source: String, message: String, cause: Throwable) : Unit

  def error(source: String, message: String, args: AnyRef*) : Unit

  // Warn

  def warn(source: String, message: String): Unit

  def warn(source: String, message: String, cause: Throwable): Unit

  def warn(source: String, message: String, args: AnyRef*): Unit

  // Info

  def info(source: String, message: String) : Unit

  def info(source: String, message: String, cause: Throwable) : Unit

  def info(source: String, message: String, args: AnyRef*) : Unit

  // Debug

  def debug(source: String, message: String): Unit

  def debug(source: String, message: String, cause: Throwable): Unit

  def debug(source: String, message: String, args: AnyRef*): Unit

  // Trace

  def trace(source: String, message: String): Unit

  def trace(source: String, message: String, cause: Throwable): Unit

  def trace(source: String, message: String, args: AnyRef*): Unit
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

  override def error(source: String, message: String): Unit = {}
  override def error(source: String, message: String, cause: Throwable): Unit = {}
  override def error(source: String, message: String, args: AnyRef*): Unit = {}

  override def warn(source: String, message: String): Unit = {}
  override def warn(source: String, message: String, cause: Throwable): Unit = {}
  override def warn(source: String, message: String, args: AnyRef*): Unit = {}

  override def info(source: String, message: String): Unit = {}
  override def info(source: String, message: String, cause: Throwable): Unit = {}
  override def info(source: String, message: String, args: AnyRef*): Unit = {}

  override def debug(source: String, message: String): Unit = {}
  override def debug(source: String, message: String, cause: Throwable): Unit = {}
  override def debug(source: String, message: String, args: AnyRef*): Unit = {}

  override def trace(source: String, message: String): Unit = {}
  override def trace(source: String, message: String, cause: Throwable): Unit = {}
  override def trace(source: String, message: String, args: AnyRef*): Unit = {}
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

object PrintLogger extends AbstractUnderlyingLogger {
  private var _printLoggerName: Boolean = true
  private var _printTimestamp: Boolean = false
  @inline
  def printLoggerName : Boolean = _printLoggerName
  def printLoggerName_=(f: Boolean) = this.synchronized( _printLoggerName = f )
  @inline
  def printTimestamp : Boolean = _printTimestamp
  def printTimestamp_=(f: Boolean) = this.synchronized( _printTimestamp = f )

  @inline
  private def getTimestamp() = new Date().toString
  @inline
  def prefix(level: String, src: String) =
    if(PrintLogger.printTimestamp) {
      if(PrintLogger.printLoggerName) s"[${getTimestamp()}, $level, $src]" else s"[${getTimestamp()}, $level]"
    } else {
      if(PrintLogger.printLoggerName) s"[$level, $src]" else s"[$level]"
    }
  @inline
  def msg(level: String, src: String, msg: String) = println(s"${prefix(level,src)} $msg")
  @inline
  def msg(level: String, src: String, msg: String, cause: Throwable) = println(s"${prefix(level,src)} $msg\n    $cause")
  @inline
  def msg(level: String, src: String, msg: String, args: AnyRef*) = println(s"${prefix(level,src)} ${String.format(msg,args)}")

  override def error(source: String, message: String): Unit = msg("ERROR",source,message)
  override def error(source: String, message: String, cause: Throwable): Unit = msg("ERROR",source,message,cause)
  override def error(source: String, message: String, args: AnyRef*): Unit = msg("ERROR",source,message,args)

  override def warn(source: String, message: String): Unit = msg("WARN",source,message)
  override def warn(source: String, message: String, cause: Throwable): Unit = msg("WARN",source,message,cause)
  override def warn(source: String, message: String, args: AnyRef*): Unit = msg("WARN",source,message,args)

  override def info(source: String, message: String): Unit = msg("INFO",source,message)
  override def info(source: String, message: String, cause: Throwable): Unit = msg("INFO",source,message,cause)
  override def info(source: String, message: String, args: AnyRef*): Unit = msg("INFO",source,message,args)

  override def debug(source: String, message: String): Unit = msg("DEBUG",source,message)
  override def debug(source: String, message: String, cause: Throwable): Unit = msg("DEBUG",source,message,cause)
  override def debug(source: String, message: String, args: AnyRef*): Unit = msg("DEBUG",source,message,args)

  override def trace(source: String, message: String): Unit = msg("TRACE",source,message)
  override def trace(source: String, message: String, cause: Throwable): Unit = msg("TRACE",source,message,cause)
  override def trace(source: String, message: String, args: AnyRef*): Unit = msg("TRACE",source,message,args)
}


object PrintLoggerFactory extends UnderlyingLoggerFactory {
  override def getUnderlyingLogger(name: String): UnderlyingLogger = PrintLogger
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
