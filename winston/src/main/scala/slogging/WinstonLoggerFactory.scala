// -   Project: slogging (https://github.com/jokade/slogging)
// Description: Winston (Node.js) logging backend
//
// Distributed under the MIT License (see included file LICENSE)
package slogging

import scalajs.js
import js.Dynamic

object WinstonLoggerFactory extends UnderlyingLoggerFactory {
  override def getUnderlyingLogger(name: String): UnderlyingLogger = new WinstonLogger(name,_logger)

  private val _winston = Dynamic.global.require("winston")
  private lazy val _logger = WinstonLoggerJS()

  trait WinstonLoggerJS extends js.Object {
    def log(level: String, msg: String): Unit = js.native
  }
  object WinstonLoggerJS {
    def apply() : WinstonLoggerJS = _winston.asInstanceOf[WinstonLoggerJS]
  }


  class WinstonLogger(name: String, l: WinstonLoggerJS) extends UnderlyingLogger {
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

    override def trace(message: String): Unit = l.log("verbose",msg("TRACE",message))
    override def trace(message: String, cause: Throwable): Unit = l.log("verbose",msg("TRACE",message,cause))
    override def trace(message: String, args: AnyRef*): Unit = l.log("verbose",msg("TRACE",message,args))
  }
}
