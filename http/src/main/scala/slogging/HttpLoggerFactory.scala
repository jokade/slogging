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
  type MessageFormatter = (String,MessageLevel,String,String,Throwable) => js.Object

  val defaultFormatter: MessageFormatter = (clientId,level,name,msg,cause) => js.Dynamic.literal(
    clientId = clientId,
    level = level match { case MessageLevel.Trace => "trace" case MessageLevel.Debug => "debug" case MessageLevel.Info => "info" case MessageLevel.Warn => "warn" case MessageLevel.Error => "error" },
    name = name,
    msg = msg,
    cause = if(cause==null) "" else cause.toString
  )

  trait LoggerTemplate extends AbstractUnderlyingLogger {

    @inline private final def log(level: MessageLevel, src: String, msg: String): Unit = log(level, src, msg, None)
    @inline private final def log(level: MessageLevel, src: String, msg: String, args: Any*): Unit = log(level, src, String.format(msg, args), None)
    @inline private final def log(level: MessageLevel, src: String, msg: String, cause: Option[Throwable]): Unit = logMessage(level, src, msg, cause)

    final def error(source: String, message: String): Unit = log(MessageLevel.Error, source, message)
    final def error(source: String, message: String, cause: Throwable): Unit = log(MessageLevel.Error, source, message, cause)
    final def error(source: String, message: String, args: Any*): Unit = log(MessageLevel.Error, source, message, args)

    final def warn(source: String, message: String): Unit = log(MessageLevel.Warn, source,message)
    final def warn(source: String, message: String, cause: Throwable): Unit = log(MessageLevel.Warn, source, message, cause)
    final def warn(source: String, message: String, args: Any*): Unit = log(MessageLevel.Warn, source, message, args)

    final def info(source: String, message: String): Unit = log(MessageLevel.Info, source,message)
    final def info(source: String, message: String, cause: Throwable): Unit = log(MessageLevel.Info, source, message, cause)
    final def info(source: String, message: String, args: Any*): Unit = log(MessageLevel.Info, source, message, args)

    final def debug(source: String, message: String): Unit = log(MessageLevel.Debug, source,message)
    final def debug(source: String, message: String, cause: Throwable): Unit = log(MessageLevel.Debug, source, message, cause)
    final def debug(source: String, message: String, args: Any*): Unit = log(MessageLevel.Debug, source, message, args)

    final def trace(source: String, message: String): Unit = log(MessageLevel.Trace, source,message)
    final def trace(source: String, message: String, cause: Throwable): Unit = log(MessageLevel.Trace, source, message, cause)
    final def trace(source: String, message: String, args: Any*): Unit = log(MessageLevel.Trace, source, message, args)

    /**
      * @param level Message level
      * @param name Logger name
      * @param message Message content
      * @param cause Optional cause
      */
    def logMessage(level: MessageLevel, name: String, message: String, cause: Option[Throwable]): Unit

  }

  /**
   * @param url URL where logging messages are sent to
   * @param clientId A string identifying the client that sent the log message
   * @param formatter function to create the message object to be sent<br/>
   */
  case class HttpLoggerConfig(url: String,
                              clientId: String,
                              formatter: MessageFormatter)

  class HttpLogger(config: HttpLoggerConfig) extends LoggerTemplate {
    import config._

    def logMessage(level: MessageLevel, name: String, message: String, cause: Option[Throwable]): Unit =
      sendMessage(url, formatter(clientId, level, name, message, cause.orNull))
  }
}


class HttpLoggerFactory(config: HttpLoggerConfig) extends UnderlyingLoggerFactory {
  private val logger = new HttpLogger(config)
  override def getUnderlyingLogger(name: String): UnderlyingLogger = logger
}
