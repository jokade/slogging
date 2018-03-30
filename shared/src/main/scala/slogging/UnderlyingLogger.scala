//     Project: Default (Template) Project
//      Module:
// Description:

// Distributed under the MIT License (see included file LICENSE)
package slogging

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

  def error(source: String, message: String, args: Any*) : Unit

  // Warn

  def warn(source: String, message: String): Unit

  def warn(source: String, message: String, cause: Throwable): Unit

  def warn(source: String, message: String, args: Any*): Unit

  // Info

  def info(source: String, message: String) : Unit

  def info(source: String, message: String, cause: Throwable) : Unit

  def info(source: String, message: String, args: Any*) : Unit

  // Debug

  def debug(source: String, message: String): Unit

  def debug(source: String, message: String, cause: Throwable): Unit

  def debug(source: String, message: String, args: Any*): Unit

  // Trace

  def trace(source: String, message: String): Unit

  def trace(source: String, message: String, cause: Throwable): Unit

  def trace(source: String, message: String, args: Any*): Unit
}


abstract class AbstractUnderlyingLogger extends UnderlyingLogger {
  @inline final def isErrorEnabled: Boolean = LoggerConfig.level >= LogLevel.ERROR
  @inline final def isWarnEnabled: Boolean = LoggerConfig.level >= LogLevel.WARN
  @inline final def isInfoEnabled: Boolean = LoggerConfig.level >= LogLevel.INFO
  @inline final def isDebugEnabled: Boolean = LoggerConfig.level >= LogLevel.DEBUG
  @inline final def isTraceEnabled: Boolean = LoggerConfig.level >= LogLevel.TRACE

}
