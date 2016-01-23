// -   Project: slogging (https://github.com/jokade/slogging)
//      Module: js / http
// Description: slogging backend for HTTP remote logging
//
// Distributed under the MIT License (see included file LICENSE)
package slogging

import slogging.HttpLoggerFactory.{HttpLogger, HttpLoggerConfig}
import scala.scalajs.js
import org.scalajs.dom.ext.Ajax
import scala.scalajs.js.JSON


object HttpLoggerFactory {

  def apply(url: String, clientId: String = "", formatter: MessageFormatter = defaultFormatter) : HttpLoggerFactory =
    new HttpLoggerFactory( HttpLoggerConfig(url,clientId,formatter) )

  private def sendMessage(url: String, msg: js.Object) : Unit = Ajax.post(url,JSON.stringify(msg))

  /**
   * Parameters:
   * <ul>
   *   <li>clientId</li>
   *   <li>log level</li>
   *   <li>logger name</li>
   *   <li>message</li>
   *   <li>cause (may be null)</li>
   * </ul>
   */
  type MessageFormatter = (String,String,String,String,Throwable) => js.Object

  val defaultFormatter: MessageFormatter = (clientId,level,name,msg,cause) => js.Dynamic.literal(
    clientId = clientId,
    level = level,
    name = name,
    msg = msg,
    cause = if(cause==null) "" else cause.toString
  )

  /**
   * @param url URL where logging messages are sent to
   * @param clientId A string identifying the client that sent the log message
   * @param formatter function to create the message object to be sent<br/>
   */
  case class HttpLoggerConfig(url: String,
                              clientId: String,
                              formatter: MessageFormatter)

  class HttpLogger(config: HttpLoggerConfig) extends AbstractUnderlyingLogger {
    import config._
    @inline private final def log(level: String, src: String, msg: String): Unit = log(level,msg,null)
    @inline private final def log(level: String, src: String, msg: String, args: AnyRef*): Unit = log(level, String.format(msg, args), null)
    private final def log(level: String, src: String, msg: String, cause: Throwable): Unit = sendMessage(url, formatter(clientId,level,src,msg,cause))


    @inline final override def error(source: String, message: String): Unit = log("error", source,message)
    @inline final override def error(source: String, message: String, cause: Throwable): Unit = log("error", source,message, cause)
    @inline final override def error(source: String, message: String, args: AnyRef*): Unit = log("error", source,message, args)

    @inline final override def warn(source: String, message: String): Unit = log("warn", source,message)
    @inline final override def warn(source: String, message: String, cause: Throwable): Unit = log("warn", source,message, cause)
    @inline final override def warn(source: String, message: String, args: AnyRef*): Unit = log("warn", source,message, args)

    @inline final override def info(source: String, message: String): Unit = log("info", source,message)
    @inline final override def info(source: String, message: String, cause: Throwable): Unit = log("info", source,message, cause)
    @inline final override def info(source: String, message: String, args: AnyRef*): Unit = log("info", source,message, args)

    @inline final override def debug(source: String, message: String): Unit = log("debug", source,message)
    @inline final override def debug(source: String, message: String, cause: Throwable): Unit = log("debug", source,message, cause)
    @inline final override def debug(source: String, message: String, args: AnyRef*): Unit = log("debug", source,message, args)

    @inline final override def trace(source: String, message: String): Unit = log("trace", source,message)
    @inline final override def trace(source: String, message: String, cause: Throwable): Unit = log("trace", source,message, cause)
    @inline final override def trace(source: String, message: String, args: AnyRef*): Unit = log("trace", source,message, args)
  }
}


class HttpLoggerFactory(config: HttpLoggerConfig) extends UnderlyingLoggerFactory {
  private val logger = new HttpLogger(config)
  override def getUnderlyingLogger(name: String): UnderlyingLogger = logger
}
