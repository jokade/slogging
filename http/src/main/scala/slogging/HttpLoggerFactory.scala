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

  private def sendMessage(url: String, msg: js.Object) : Unit = Ajax.post(url,JSON.stringify(msg),headers = Map("Content-Type"->"application/json"))

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
    level = level match { case MessageLevel.`trace` => "trace" case MessageLevel.`debug` => "debug" case MessageLevel.`info` => "info" case MessageLevel.`warn` => "warn" case MessageLevel.`error` => "error" },
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
