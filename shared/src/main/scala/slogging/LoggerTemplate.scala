//     Project: Default (Template) Project
//      Module:
// Description:

// Distributed under the MIT License (see included file LICENSE)
package slogging

abstract class LoggerTemplate extends AbstractUnderlyingLogger {

  @inline private final def log(level: MessageLevel, src: String, msg: String): Unit = log(level, src, msg, None)
  @inline private final def log(level: MessageLevel, src: String, msg: String, args: Any*): Unit = log(level, src, LoggerConfig.argsFormatter(msg, args), None)
  @inline private final def log(level: MessageLevel, src: String, msg: String, cause: Option[Throwable]): Unit = logMessage(level, src, msg, cause)

  final def error(source: String, message: String): Unit = log(MessageLevel.error, source, message)
  final def error(source: String, message: String, cause: Throwable): Unit = log(MessageLevel.error, source, message, Option(cause))
  final def error(source: String, message: String, args: Any*): Unit = log(MessageLevel.error, source, message, args:_*)

  final def warn(source: String, message: String): Unit = log(MessageLevel.warn, source,message)
  final def warn(source: String, message: String, cause: Throwable): Unit = log(MessageLevel.warn, source, message, Option(cause))
  final def warn(source: String, message: String, args: Any*): Unit = log(MessageLevel.warn, source, message, args:_*)

  final def info(source: String, message: String): Unit = log(MessageLevel.info, source,message)
  final def info(source: String, message: String, cause: Throwable): Unit = log(MessageLevel.info, source, message, Option(cause))
  final def info(source: String, message: String, args: Any*): Unit = log(MessageLevel.info, source, message, args:_*)

  final def debug(source: String, message: String): Unit = log(MessageLevel.debug, source,message)
  final def debug(source: String, message: String, cause: Throwable): Unit = log(MessageLevel.debug, source, message, Option(cause))
  final def debug(source: String, message: String, args: Any*): Unit = log(MessageLevel.debug, source, message, args:_*)

  final def trace(source: String, message: String): Unit = log(MessageLevel.trace, source,message)
  final def trace(source: String, message: String, cause: Throwable): Unit = log(MessageLevel.trace, source, message, Option(cause))
  final def trace(source: String, message: String, args: Any*): Unit = log(MessageLevel.trace, source, message, args:_*)

  /**
   * @param level Message level
   * @param name Logger name
   * @param message Message content
   * @param cause Optional cause
   */
  def logMessage(level: MessageLevel, name: String, message: String, cause: Option[Throwable]): Unit

}
