// -   Project: slogging (https://github.com/jokade/slogging)
//      Module: shared
// Description: Common interface and macro wrapper for LoggerS
//
// Copyright (c) 2015 Johannes Kastner <jkspam@karchedon.de>
//               Distributed under the MIT License (see included file LICENSE)
package slogging

import scala.language.experimental.macros
import scala.reflect.macros.blackbox.Context

object Logger {
  def apply(l: UnderlyingLogger) : Logger = new Logger(l)
}

/**
 * Implementation for a performant Logger based on macros and an underlying [[Logger]].
 * Adaptation of the scala-logging Logger implementation.
 *
 * @param underlying
 */
final class Logger private (val underlying: UnderlyingLogger) extends AnyVal {

  // Error

  def error(message: String): Unit = macro LoggerMacro.errorMessage

  def error(message: String, cause: Throwable): Unit = macro LoggerMacro.errorMessageCause

  def error(message: String, args: Any*): Unit = macro LoggerMacro.errorMessageArgs

  // Warn

  def warn(message: String): Unit = macro LoggerMacro.warnMessage

  def warn(message: String, cause: Throwable): Unit = macro LoggerMacro.warnMessageCause

  def warn(message: String, args: Any*): Unit = macro LoggerMacro.warnMessageArgs

  // Info

  def info(message: String): Unit = macro LoggerMacro.infoMessage

  def info(message: String, cause: Throwable): Unit = macro LoggerMacro.infoMessageCause

  def info(message: String, args: Any*): Unit = macro LoggerMacro.infoMessageArgs

  // Debug

  def debug(message: String): Unit = macro LoggerMacro.debugMessage

  def debug(message: String, cause: Throwable): Unit = macro LoggerMacro.debugMessageCause

  def debug(message: String, args: Any*): Unit = macro LoggerMacro.debugMessageArgs

  // Trace

  def trace(message: String): Unit = macro LoggerMacro.traceMessage

  def trace(message: String, cause: Throwable): Unit = macro LoggerMacro.traceMessageCause

  def trace(message: String, args: Any*): Unit = macro LoggerMacro.traceMessageArgs
}


/**
 * Copied from scala-logging LoggerMacro
 */
private class LoggerMacro(val c: Context) {
  import c.universe._

  type LoggerContext = Context { type PrefixType = Logger }

  val disabled = c.settings.exists( _ == "slogging.disable" )

  // Error

  def errorMessage(message: c.Expr[String]) : c.Tree = {
    val underlying = q"${c.prefix}.underlying"
    if(disabled) q"()" else
    q"""if ($underlying.isErrorEnabled) {
          $underlying.error(loggerName,$message)
          slogging.LoggerConfig.onError(slogging.LogLevel.ERROR,loggerName,$message)
        }"""
  }

  def errorMessageCause(message: c.Expr[String], cause: c.Expr[Throwable]) : c.Tree = {
    val underlying = q"${c.prefix}.underlying"
    if(disabled) q"()" else
    q"""if ($underlying.isErrorEnabled) {
          $underlying.error(loggerName,$message, $cause)
          slogging.LoggerConfig.onError(slogging.LogLevel.ERROR,loggerName,$message)
        }"""
  }

  def errorMessageArgs(message: c.Expr[String], args: c.Expr[Any]*) : c.Tree = {
    val underlying = q"${c.prefix}.underlying"
    if(disabled) q"()"
    else if (args.length == 2)
      q"""if ($underlying.isErrorEnabled) {
            $underlying.error(loggerName,$message, List(${args(0)}, ${args(1)}): _*)
            slogging.LoggerConfig.onError(slogging.LogLevel.ERROR,loggerName,$message)
          }"""
    else q"""if ($underlying.isErrorEnabled) {
               $underlying.error(loggerName,$message, ..$args)
               slogging.LoggerConfig.onError(slogging.LogLevel.ERROR,loggerName,$message)
             }"""
  }

  // Warn

  def warnMessage(message: c.Expr[String]) : c.Tree = {
    val underlying = q"${c.prefix}.underlying"
    if(disabled) q"()" else
    q"if ($underlying.isWarnEnabled) $underlying.warn(loggerName,$message)"
  }

  def warnMessageCause(message: c.Expr[String], cause: c.Expr[Throwable]) : c.Tree = {
    val underlying = q"${c.prefix}.underlying"
    if(disabled) q"()" else
    q"if ($underlying.isWarnEnabled) $underlying.warn(loggerName,$message, $cause)"
  }

  def warnMessageArgs(message: c.Expr[String], args: c.Expr[Any]*) : c.Tree = {
    val underlying = q"${c.prefix}.underlying"
    if(disabled) q"()"
    else if (args.length == 2)
      q"if ($underlying.isWarnEnabled) $underlying.warn(loggerName,$message, List(${args(0)}, ${args(1)}): _*)"
    else
      q"if ($underlying.isWarnEnabled) $underlying.warn(loggerName,$message, ..$args)"
  }

  // Info

  def infoMessage(message: c.Expr[String]) : c.Tree = {
    val underlying = q"${c.prefix}.underlying"
    if(disabled) q"()" else
    q"if ($underlying.isInfoEnabled) $underlying.info(loggerName,$message)"
  }

  def infoMessageCause(message: c.Expr[String], cause: c.Expr[Throwable]) : c.Tree = {
    val underlying = q"${c.prefix}.underlying"
    if(disabled) q"()" else
    q"if ($underlying.isInfoEnabled) $underlying.info(loggerName,$message, $cause)"
  }

  def infoMessageArgs(message: c.Expr[String], args: c.Expr[Any]*) : c.Tree = {
    val underlying = q"${c.prefix}.underlying"
    if(disabled) q"()"
    else if (args.length == 2)
      q"if ($underlying.isInfoEnabled) $underlying.info(loggerName,$message, List(${args(0)}, ${args(1)}): _*)"
    else
      q"if ($underlying.isInfoEnabled) $underlying.info(loggerName,$message, ..$args)"
  }

  // Debug

  def debugMessage(message: c.Expr[String]) : c.Tree = {
    val underlying = q"${c.prefix}.underlying"
    if(disabled) q"()" else
    q"if ($underlying.isDebugEnabled) $underlying.debug(loggerName,$message)"
  }

  def debugMessageCause(message: c.Expr[String], cause: c.Expr[Throwable]) : c.Tree = {
    val underlying = q"${c.prefix}.underlying"
    if(disabled) q"()" else
    q"if ($underlying.isDebugEnabled) $underlying.debug(loggerName,$message, $cause)"
  }

  def debugMessageArgs(message: c.Expr[String], args: c.Expr[Any]*) : c.Tree = {
    val underlying = q"${c.prefix}.underlying"
    if(disabled) q"()"
    else if (args.length == 2)
      q"if ($underlying.isDebugEnabled) $underlying.debug(loggerName,$message, List(${args(0)}, ${args(1)}): _*)"
    else
      q"if ($underlying.isDebugEnabled) $underlying.debug(loggerName,$message, ..$args)"
  }

  // Trace

  def traceMessage(message: c.Expr[String]) : c.Tree = {
    val underlying = q"${c.prefix}.underlying"
    if(disabled) q"()" else
    q"if ($underlying.isTraceEnabled) $underlying.trace(loggerName,$message)"
  }

  def traceMessageCause(message: c.Expr[String], cause: c.Expr[Throwable]) : c.Tree = {
    val underlying = q"${c.prefix}.underlying"
    if(disabled) q"()" else
    q"if ($underlying.isTraceEnabled) $underlying.trace(loggerName,$message, $cause)"
  }

  def traceMessageArgs(message: c.Expr[String], args: c.Expr[Any]*) : c.Tree = {
    val underlying = q"${c.prefix}.underlying"
    if(disabled) q"()"
    else if (args.length == 2)
      q"if ($underlying.isTraceEnabled) $underlying.trace(loggerName,$message, List(${args(0)}, ${args(1)}): _*)"
    else
      q"if ($underlying.isTraceEnabled) $underlying.trace(loggerName,$message, ..$args)"
  }
}
