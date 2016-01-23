// -   Project: slogging (https://github.com/jokade/slogging)
//      Module: js / winston
// Description: Winston (Node.js) logging backend
//
// Distributed under the MIT License (see included file LICENSE)
package slogging

import slogging.WinstonLoggerFactory.WinstonLogger

import scalajs.js
import js.Dynamic

object WinstonLoggerFactory {
  val winston = Dynamic.global.require("winston")
  private lazy val _default = new WinstonLoggerFactory(WinstonLogger())

  def apply() : UnderlyingLoggerFactory = _default
  def apply(wlogger: WinstonLogger) : UnderlyingLoggerFactory = new WinstonLoggerFactory(wlogger)

  @js.native
  trait WinstonLogger extends js.Object {
    def log(level: String, msg: String): Unit = js.native
  }
  object WinstonLogger {
    def apply() : WinstonLogger = winston.asInstanceOf[WinstonLogger]
    def apply(config: js.Dynamic) : WinstonLogger = js.Dynamic.newInstance(winston.Logger)(config).asInstanceOf[WinstonLogger]
  }

}

class WinstonLoggerFactory(_logger: WinstonLogger) extends UnderlyingLoggerFactory {
  override def getUnderlyingLogger(name: String): UnderlyingLogger = new WinstonLoggerImpl(name,_logger)

  private class WinstonLoggerImpl(name: String, l: WinstonLogger) extends AbstractUnderlyingLogger {
    def prefix(level: String) = "" //if(ConsoleLogger.printLoggerName) s"[$level, $name]" else s"[$level]"
    def msg(level: String, msg: String) = s"${prefix(level)} $msg"
    def msg(level: String, msg: String, cause: Throwable) = s"${prefix(level)} $msg\n    $cause"
    def msg(level: String, msg: String, args: AnyRef*) = s"${prefix(level)} ${String.format(msg,args)}"


    override def error(message: String): Unit = l.log("error",msg("error",message))
    override def error(message: String, cause: Throwable): Unit = l.log("error",msg("ERROR",message,cause))
    override def error(message: String, args: AnyRef*): Unit = l.log("error",msg("ERROR",message,args))

    override def warn(message: String): Unit = l.log("warn",msg("WARN",message))
    override def warn(message: String, cause: Throwable): Unit = l.log("warn",msg("WARN",message,cause))
    override def warn(message: String, args: AnyRef*): Unit = l.log("warn",msg("WARN",message,args))

    override def info(message: String): Unit = l.log("info",msg("INFO",message))
    override def info(message: String, cause: Throwable): Unit = l.log("info",msg("INFO",message,cause))
    override def info(message: String, args: AnyRef*): Unit = l.log("info",msg("INFO",message,args))

    override def debug(message: String): Unit = l.log("debug",msg("DEBUG",message))
    override def debug(message: String, cause: Throwable): Unit = l.log("debug",msg("DEBUG",message,cause))
    override def debug(message: String, args: AnyRef*): Unit = l.log("debug",msg("DEBUG",message,args))

    override def trace(message: String): Unit = l.log("trace",msg("TRACE",message))
    override def trace(message: String, cause: Throwable): Unit = l.log("trace",msg("TRACE",message,cause))
    override def trace(message: String, args: AnyRef*): Unit = l.log("trace",msg("TRACE",message,args))
  }
}
