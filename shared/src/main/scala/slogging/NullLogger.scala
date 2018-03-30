//     Project: slogging
//      Module: shared
// Description: No-Op logger implementation

// Distributed under the MIT License (see included file LICENSE)
package slogging

object NullLogger extends UnderlyingLogger {
  override val isErrorEnabled: Boolean = false
  override val isWarnEnabled: Boolean = false
  override val isInfoEnabled: Boolean = false
  override val isDebugEnabled: Boolean = false
  override val isTraceEnabled: Boolean = false

  override def error(source: String, message: String): Unit = {}
  override def error(source: String, message: String, cause: Throwable): Unit = {}
  override def error(source: String, message: String, args: Any*): Unit = {}

  override def warn(source: String, message: String): Unit = {}
  override def warn(source: String, message: String, cause: Throwable): Unit = {}
  override def warn(source: String, message: String, args: Any*): Unit = {}

  override def info(source: String, message: String): Unit = {}
  override def info(source: String, message: String, cause: Throwable): Unit = {}
  override def info(source: String, message: String, args: Any*): Unit = {}

  override def debug(source: String, message: String): Unit = {}
  override def debug(source: String, message: String, cause: Throwable): Unit = {}
  override def debug(source: String, message: String, args: Any*): Unit = {}

  override def trace(source: String, message: String): Unit = {}
  override def trace(source: String, message: String, cause: Throwable): Unit = {}
  override def trace(source: String, message: String, args: Any*): Unit = {}
}

object NullLoggerFactory extends UnderlyingLoggerFactory {
  override def getUnderlyingLogger(name: String): UnderlyingLogger = NullLogger

}

