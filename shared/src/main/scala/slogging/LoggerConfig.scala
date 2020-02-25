//     Project: slogging
//      Module: shared
// Description: Classes for logging configuration

// Distributed under the MIT License (see included file LICENSE)
package slogging

sealed abstract class LogLevel {
  def value: Int
  @inline final def >=(other: LogLevel): Boolean = this.value >= other.value
}
object LogLevel  {
  case object OFF   extends LogLevel { val value = 0 }
  case object ERROR extends LogLevel { val value = 1 }
  case object WARN  extends LogLevel { val value = 2 }
  case object INFO  extends LogLevel { val value = 3 }
  case object DEBUG extends LogLevel { val value = 4 }
  case object TRACE extends LogLevel { val value = 5 }
}

object LoggerConfig {
  type LoggingHook = Function3[LogLevel,String,String,Unit]
  type ArgsFormatter = Function2[String,Seq[Any],String]

  val defaultHook: LoggingHook = (_,_,_) => {}

  private var _factory : UnderlyingLoggerFactory = NullLoggerFactory
  private var _level : LogLevel = LogLevel.INFO
  private var _errorHook = defaultHook
  private var _argsFormatter: ArgsFormatter = LoggingUtils.argsBracketFormat

  @inline
  def argsFormatter: ArgsFormatter = _argsFormatter
  def argsFormatter_(f: ArgsFormatter): Unit = this.synchronized{ this._argsFormatter = f }

  @inline
  def factory: UnderlyingLoggerFactory = _factory
  def factory_=(f: UnderlyingLoggerFactory): Unit = this.synchronized{ _factory = f }
  @inline
  def level: LogLevel = _level
  def level_=(l: LogLevel): Unit = this.synchronized{ _level = l }
  @inline
  def onError: LoggingHook = _errorHook
  def onError_=(listener: LoggingHook): Unit = this.synchronized { _errorHook = listener }
}
