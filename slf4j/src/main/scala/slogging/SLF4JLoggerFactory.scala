// -   Project: slogging (https://github.com/jokade/slogging)
//      Module: jvm / slf4j
// Description: SLF4 logging backend
//
// Distributed under the MIT License (see included file LICENSE)
package slogging

import org.slf4j

object SLF4JLoggerFactory extends UnderlyingLoggerFactory {
  override def getUnderlyingLogger(name: String): UnderlyingLogger = new SLF4JLogger(slf4j.LoggerFactory.getLogger(name))

  class SLF4JLogger(l: slf4j.Logger) extends AbstractUnderlyingLogger {

    override def error(source: String, message: String): Unit = l.error(message)
    override def error(source: String, message: String, cause: Throwable): Unit = l.error(message,cause)
    override def error(source: String, message: String, args: Any*): Unit = l.error(message,args.asInstanceOf[Seq[AnyRef]]:_*)

    override def warn(source: String, message: String): Unit = l.warn(message)
    override def warn(source: String, message: String, cause: Throwable): Unit = l.warn(message,cause)
    override def warn(source: String, message: String, args: Any*): Unit = l.warn(message,args.asInstanceOf[Seq[AnyRef]]:_*)

    override def info(source: String, message: String): Unit = l.info(message)
    override def info(source: String, message: String, cause: Throwable): Unit = l.info(message,cause)
    override def info(source: String, message: String, args: Any*): Unit = l.info(message,args.asInstanceOf[Seq[AnyRef]]:_*)

    override def debug(source: String, message: String): Unit = l.debug(message)
    override def debug(source: String, message: String, cause: Throwable): Unit = l.debug(message,cause)
    override def debug(source: String, message: String, args: Any*): Unit = l.debug(message,args.asInstanceOf[Seq[AnyRef]]:_*)

    override def trace(source: String, message: String): Unit = l.trace(message)
    override def trace(source: String, message: String, cause: Throwable): Unit = l.trace(message,cause)
    override def trace(source: String, message: String, args: Any*): Unit = l.trace(message, args.asInstanceOf[Seq[AnyRef]]:_*)

  }
}
