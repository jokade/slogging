//     Project: slogging
//      Module: 
// Description: 
package slogging

import scala.scalanative.unsafe._

object SyslogLoggerFactory extends UnderlyingLoggerFactory {
  override def getUnderlyingLogger(name: String) = {
    SyslogLogger
  }

  object SyslogLogger extends LoggerTemplate {
    override def logMessage(level: MessageLevel, name: String, message: String, cause: Option[Throwable]): Unit = Zone{ implicit z =>
      api.vsyslog(priority(level,syslogFacility),c"%s",toCVarArgList(toCString(message)))
    }
  }

  /* syslog levels */
  val LOG_EMERG   = 0
  val LOG_ALERT   = 1
  val LOG_CRIT    = 2
  val LOG_ERR     = 3
  val LOG_WARNING = 4
  val LOG_NOTICE  = 5
  val LOG_INFO    = 6
  val LOG_DEBUG   = 7

  /* syslog facilities */
  val LOG_KERN       = 0<<3
  val LOG_USER       = 1<<3
  val LOG_MAIL       = 2<<3
  val LOG_DAEMON     = 3<<3
  val LOG_AUTH       = 4<<3
  val LOG_SYSLOG     = 5<<3
  val LOG_LPR        = 6<<3
  val LOG_NEWS       = 7<<3
  val LOG_UUCP       = 8<<3
  val LOG_CRON       = 9<<3
  val LOG_AUTHPRIV   = 10<<3
  val LOG_FTP        = 11<<3
  val LOG_NETINFO    = 12<<3
  val LOG_REMOTEAUTH = 13<<3
  val LOG_INSTALL    = 14<<3
  val LOG_RAS        = 15<<3
  val LOG_LOCAL0     = 16<<3
  val LOG_LOCAL1     = 17<<3
  val LOG_LOCAL2     = 18<<3
  val LOG_LOCAL3     = 19<<3
  val LOG_LOCAL4     = 20<<3
  val LOG_LOCAL5     = 21<<3
  val LOG_LOCAL6     = 22<<3
  val LOG_LOCAL7     = 23<<3

  var levelMap: Map[MessageLevel,Int] = Map(
    MessageLevel.error -> LOG_ERR,
    MessageLevel.warn  -> LOG_WARNING,
    MessageLevel.info  -> LOG_INFO,
    MessageLevel.debug -> LOG_DEBUG,
    MessageLevel.trace -> LOG_DEBUG
  )

  var syslogFacility: Int = LOG_USER

  def LOG_UPTO(pri: Int): CInt = (1 << (pri+1)) - 1

  def priority(level: MessageLevel, facility: Int): CInt = levelMap(level) + facility


  @extern
  object api {
    def vsyslog(priority: CInt, format: CString, msg: CVarArgList): Unit = extern
    def setlogmask(maskprio: CInt): CInt = extern
  }
}
