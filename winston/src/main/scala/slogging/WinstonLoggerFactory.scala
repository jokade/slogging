// -   Project: slogging (https://github.com/jokade/slogging)
//      Module: js / winston
// Description: Winston (Node.js) logging backend
//
// Distributed under the MIT License (see included file LICENSE)
package slogging

import slogging.WinstonLoggerFactory.WinstonLogger

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

object WinstonLoggerFactory {
  private lazy val _default = new WinstonLoggerFactory(DefaultLogger)

  def apply() : UnderlyingLoggerFactory = _default
  def apply(wlogger: WinstonLogger) : UnderlyingLoggerFactory = new WinstonLoggerFactory(wlogger)
  def apply(config: js.Dynamic) : UnderlyingLoggerFactory = new WinstonLoggerFactory(new CustomLogger(config))

  @js.native
  trait WinstonLogger extends js.Object {
    def log(level: String, msg: String): Unit = js.native
  }

  @js.native
  @JSImport("winston","Logger","winston.Logger")
  class CustomLogger(config: js.UndefOr[js.Dynamic] = js.undefined) extends WinstonLogger {
  }

  @js.native
  @JSImport("winston",JSImport.Namespace,"winston")
  object DefaultLogger extends WinstonLogger

}


class WinstonLoggerFactory(_logger: WinstonLogger) extends UnderlyingLoggerFactory {
  import LoggingUtils.formatMessage
  private val logger = new WinstonLoggerImpl(_logger)
  override def getUnderlyingLogger(name: String): UnderlyingLogger =  logger

  final private class WinstonLoggerImpl(l: WinstonLogger) extends AbstractUnderlyingLogger {
    @inline
    def prefix(level: String, src: String) = "" //if(ConsoleLogger.printLoggerName) s"[$level, $name]" else s"[$level]"
    @inline
    def msg(level: String, src: String, msg: String) = s"${prefix(level,src)} $msg"
    @inline
    def msg(level: String, src: String, msg: String, cause: Throwable) = s"${prefix(level,src)} $msg\n    $cause"
    @inline
    def msg(level: String, src: String, msg: String, args: Any*) = s"${prefix(level,src)} ${formatMessage(msg,args)}"


    override def error(source: String, message: String): Unit = l.log("error",msg("error",source,message))
    override def error(source: String, message: String, cause: Throwable): Unit = l.log("error",msg("ERROR",source,message,cause))
    override def error(source: String, message: String, args: Any*): Unit = l.log("error",msg("ERROR",source,message,args:_*))

    override def warn(source: String, message: String): Unit = l.log("warn",msg("WARN",source,message))
    override def warn(source: String, message: String, cause: Throwable): Unit = l.log("warn",msg("WARN",source,message,cause))
    override def warn(source: String, message: String, args: Any*): Unit = l.log("warn",msg("WARN",source,message,args:_*))

    override def info(source: String, message: String): Unit = l.log("info",msg("INFO",source,message))
    override def info(source: String, message: String, cause: Throwable): Unit = l.log("info",msg("INFO",source,message,cause))
    override def info(source: String, message: String, args: Any*): Unit = l.log("info",msg("INFO",source,message,args:_*))

    override def debug(source: String, message: String): Unit = l.log("debug",msg("DEBUG",source,message))
    override def debug(source: String, message: String, cause: Throwable): Unit = l.log("debug",msg("DEBUG",source,message,cause))
    override def debug(source: String, message: String, args: Any*): Unit = l.log("debug",msg("DEBUG",source,message,args:_*))

    override def trace(source: String, message: String): Unit = l.log("trace",msg("TRACE",source,message))
    override def trace(source: String, message: String, cause: Throwable): Unit = l.log("trace",msg("TRACE",source,message,cause))
    override def trace(source: String, message: String, args: Any*): Unit = l.log("trace",msg("TRACE",source,message,args:_*))
  }
}
