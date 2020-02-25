//     Project: slogging
//      Module: glib
// Description: Logger backend for glib

// Distributed under the MIT License (see included file LICENSE)
package slogging

import slogging.GLibLoggerFactory.GLibLogger

import scalanative.unsafe._


object GLibLoggerFactory extends UnderlyingLoggerFactory {
  override def getUnderlyingLogger(name: String): UnderlyingLogger = GLibLogger

  object GLibLogger extends LoggerTemplate {
    override def logMessage(level: MessageLevel, name: String, message: String, cause: Option[Throwable]): Unit = Zone { implicit z =>
      val msg = toCString( formatter.formatMessage(level,name,message,cause) )
      api.g_log(null,glevel(level),msg)
    }

    private def glevel(level: MessageLevel): GLogLevelFlags = level match {
      case MessageLevel.error => GLogLevelFlags.LEVEL_CRITICAL
      case MessageLevel.warn  => GLogLevelFlags.LEVEL_CRITICAL
      case MessageLevel.info  => GLogLevelFlags.LEVEL_INFO
      case MessageLevel.debug => GLogLevelFlags.LEVEL_DEBUG
      case MessageLevel.trace => GLogLevelFlags.LEVEL_DEBUG
    }
  }

  @extern
  object api {
    def g_log(log_domain: CString, log_level: GLogLevelFlags, msg: CString): Unit = extern
  }

  type GLogLevelFlags = Int
  object GLogLevelFlags {
    val RECURSION       = 1 << 0
    val FATAL           = 1 << 1
    val LEVEL_ERROR     = 1 << 2
    val LEVEL_CRITICAL  = 1 << 3
    val LEVEL_WARNING   = 1 << 4
    val LEVEL_MESSAGE   = 1 << 5
    val LEVEL_INFO      = 1 << 6
    val LEVEL_DEBUG     = 1 << 7

    val LEVEL_MASK      = ~(RECURSION | FATAL)
  }


  private var _formatter: MessageFormatter = MessageFormatter.default
  @inline
  final def formatter: MessageFormatter = _formatter
  final def formatter_=(f: MessageFormatter): Unit = this.synchronized{ _formatter = f }
}
