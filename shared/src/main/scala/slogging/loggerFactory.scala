// -   Project: slogging (https://github.com/jokade/slogging)
//      Module: shared
// Description: Provides implementations for LoggerS and LoggerFactoryS
//
// Copyright (c) 2015 Johannes Kastner <jkspam@karchedon.de>
//               Distributed under the MIT License (see included file LICENSE)
package slogging

import java.util.Date

/**
 * Common interface for LoggerS (this interface is compatible to the slf4j logging API)
 */
trait UnderlyingLogger {

  def isErrorEnabled: Boolean
  def isWarnEnabled: Boolean
  def isInfoEnabled: Boolean
  def isDebugEnabled: Boolean
  def isTraceEnabled: Boolean

  // Error

  def error(source: String, message: String) : Unit

  def error(source: String, message: String, cause: Throwable) : Unit

  def error(source: String, message: String, args: Any*) : Unit

  // Warn

  def warn(source: String, message: String): Unit

  def warn(source: String, message: String, cause: Throwable): Unit

  def warn(source: String, message: String, args: Any*): Unit

  // Info

  def info(source: String, message: String) : Unit

  def info(source: String, message: String, cause: Throwable) : Unit

  def info(source: String, message: String, args: Any*) : Unit

  // Debug

  def debug(source: String, message: String): Unit

  def debug(source: String, message: String, cause: Throwable): Unit

  def debug(source: String, message: String, args: Any*): Unit

  // Trace

  def trace(source: String, message: String): Unit

  def trace(source: String, message: String, cause: Throwable): Unit

  def trace(source: String, message: String, args: Any*): Unit
}

abstract class AbstractUnderlyingLogger extends UnderlyingLogger {
  @inline final def isErrorEnabled: Boolean = LoggerConfig.level >= LogLevel.ERROR
  @inline final def isWarnEnabled: Boolean = LoggerConfig.level >= LogLevel.WARN
  @inline final def isInfoEnabled: Boolean = LoggerConfig.level >= LogLevel.INFO
  @inline final def isDebugEnabled: Boolean = LoggerConfig.level >= LogLevel.DEBUG
  @inline final def isTraceEnabled: Boolean = LoggerConfig.level >= LogLevel.TRACE

}

abstract class LoggerTemplate extends AbstractUnderlyingLogger {

  @inline private final def log(level: MessageLevel, src: String, msg: String): Unit = log(level, src, msg, None)
  @inline private final def log(level: MessageLevel, src: String, msg: String, args: Any*): Unit = log(level, src, LoggerConfig.argsFormatter(msg, args), None)
  @inline private final def log(level: MessageLevel, src: String, msg: String, cause: Option[Throwable]): Unit = logMessage(level, src, msg, cause)

  final def error(source: String, message: String): Unit = log(MessageLevel.error, source, message)
  final def error(source: String, message: String, cause: Throwable): Unit = log(MessageLevel.error, source, message, cause)
  final def error(source: String, message: String, args: Any*): Unit = log(MessageLevel.error, source, message, args:_*)

  final def warn(source: String, message: String): Unit = log(MessageLevel.warn, source,message)
  final def warn(source: String, message: String, cause: Throwable): Unit = log(MessageLevel.warn, source, message, cause)
  final def warn(source: String, message: String, args: Any*): Unit = log(MessageLevel.warn, source, message, args:_*)

  final def info(source: String, message: String): Unit = log(MessageLevel.info, source,message)
  final def info(source: String, message: String, cause: Throwable): Unit = log(MessageLevel.info, source, message, cause)
  final def info(source: String, message: String, args: Any*): Unit = log(MessageLevel.info, source, message, args:_*)

  final def debug(source: String, message: String): Unit = log(MessageLevel.debug, source,message)
  final def debug(source: String, message: String, cause: Throwable): Unit = log(MessageLevel.debug, source, message, cause)
  final def debug(source: String, message: String, args: Any*): Unit = log(MessageLevel.debug, source, message, args:_*)

  final def trace(source: String, message: String): Unit = log(MessageLevel.trace, source,message)
  final def trace(source: String, message: String, cause: Throwable): Unit = log(MessageLevel.trace, source, message, cause)
  final def trace(source: String, message: String, args: Any*): Unit = log(MessageLevel.trace, source, message, args:_*)

  /**
   * @param level Message level
   * @param name Logger name
   * @param message Message content
   * @param cause Optional cause
   */
  def logMessage(level: MessageLevel, name: String, message: String, cause: Option[Throwable]): Unit

}

trait UnderlyingLoggerFactory {
  def getUnderlyingLogger(name: String) : UnderlyingLogger
  def apply() : UnderlyingLoggerFactory = this
}

/** Level of a given logging message */
sealed trait MessageLevel
object MessageLevel {
  case object trace extends MessageLevel
  case object debug extends MessageLevel
  case object info extends MessageLevel
  case object warn extends MessageLevel
  case object error extends MessageLevel
}


object LoggingUtils {

  // TODO: optimize?
  /**
   * Replaces `{}` in `msg` with the args.
   *
   * @param msg message to be filled with arguments
   * @param args arguments
   * @return
   */
  def argsBracketFormat(msg: String, args: Seq[Any]) : String =
    if(args.isEmpty) msg
    else {
      @annotation.tailrec
      def loop(acc: String, chars: List[Char], args: Seq[Any]): String =
        if(chars.isEmpty) acc
        else if(args.isEmpty) acc + chars.mkString
        else chars match {
          case '{' :: '}' :: xs  =>
            loop( acc + args.head, xs, args.tail )
          case a :: xs => loop( acc + a, xs, args )
          case Nil => acc
        }

      loop("",msg.toList,args)
    }

//  @inline
  def argsStringFormat(msg: String, args: Seq[Any]): String = String.format(msg,args.asInstanceOf[Seq[Object]]:_*)
}

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

object LoggerFactory {
  def getLogger(name: String) : Logger = Logger( LoggerConfig.factory.getUnderlyingLogger(name) )
}

