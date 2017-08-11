//     Project: slogging
//      Module: shared
// Description: Classes for logging configuration

// Distributed under the MIT License (see included file LICENSE)
package slogging

object LogLevel extends Enumeration {
  val OFF = Value(0)
  val ERROR = Value(1)
  val WARN = Value(2)
  val INFO = Value(3)
  val DEBUG = Value(4)
  val TRACE = Value(5)
}

object LoggerConfig {
  type LoggingHook = Function3[LogLevel.Value,String,String,Unit]

  val defaultHook: LoggingHook = (_,_,_) => {}

  private var _factory : UnderlyingLoggerFactory = NullLoggerFactory
  private var _level : LogLevel.Value = LogLevel.INFO
  private var _errorHook = defaultHook

  @inline
  def factory: UnderlyingLoggerFactory = _factory
  def factory_=(f: UnderlyingLoggerFactory): Unit = this.synchronized{ _factory = f }
  @inline
  def level: LogLevel.Value = _level
  def level_=(l: LogLevel.Value): Unit = this.synchronized{ _level = l }
  @inline
  def onError: LoggingHook = _errorHook
  def onError_=(listener: LoggingHook): Unit = this.synchronized( _errorHook = listener )
}
