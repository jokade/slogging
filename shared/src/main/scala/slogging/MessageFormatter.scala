//     Project: Default (Template) Project
//      Module:
// Description:

// Distributed under the MIT License (see included file LICENSE)
package slogging

import java.util.Date

trait MessageFormatter {
  def formatMessage(level: MessageLevel, name: String, msg: String, cause: Option[Throwable]): String
}

object MessageFormatter {
  abstract class PrefixFormatter extends MessageFormatter {
    @inline final override def formatMessage(level: MessageLevel, name: String, msg: String, cause: Option[Throwable]): String =
      if(cause.isDefined)
        formatPrefix(level,name) + msg + "\n" + cause.get.toString
      else
        formatPrefix(level,name) + msg

    def formatPrefix(level: MessageLevel, name: String): String
  }

  final class DefaultPrefixFormatter(printLevel: Boolean,
                                    printName: Boolean,
                                    printTimestamp: Boolean) extends PrefixFormatter {
    @inline
    private def getTimestamp = new Date().toString

    override def formatPrefix(level: MessageLevel, name: String): String =
      if(printTimestamp) {
        if(printName) s"[$getTimestamp, $level, $name] " else s"[$getTimestamp, $level] "
      } else {
        if(printName) s"[$level, $name] " else s"[$level] "
      }
  }

  lazy val default: MessageFormatter = new DefaultPrefixFormatter(true,true,false)
}