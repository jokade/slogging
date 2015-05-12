// -   Project: slogging (https://github.com/jokade/slogging)
// Description: SLF4 logging backend
//
// Distributed under the MIT License (see included file LICENSE)
package slogging

import org.slf4j

object SLF4JLoggerFactory extends UnderlyingLoggerFactory {
  override def getUnderlyingLogger(name: String): UnderlyingLogger = new SLF4JLogger(slf4j.LoggerFactory.getLogger(name))

  class SLF4JLogger(l: slf4j.Logger) extends UnderlyingLogger {

    override def error(message: String): Unit = l.error(message)
    override def error(message: String, cause: Throwable): Unit = l.error(message,cause)
    override def error(message: String, args: AnyRef*): Unit = l.error(message,args:_*)

    override def warn(message: String): Unit = l.warn(message)
    override def warn(message: String, cause: Throwable): Unit = l.warn(message,cause)
    override def warn(message: String, args: AnyRef*): Unit = l.warn(message,args:_*)

    override def info(message: String): Unit = l.info(message)
    override def info(message: String, cause: Throwable): Unit = l.info(message,cause)
    override def info(message: String, args: AnyRef*): Unit = l.info(message,args:_*)

    override def debug(message: String): Unit = l.debug(message)
    override def debug(message: String, cause: Throwable): Unit = l.debug(message,cause)
    override def debug(message: String, args: AnyRef*): Unit = l.debug(message,args:_*)

    override def trace(message: String): Unit = l.trace(message)
    override def trace(message: String, cause: Throwable): Unit = l.trace(message,cause)
    override def trace(message: String, args: AnyRef*): Unit = l.trace(message, args:_*)

  }
}
