//     Project: slogging
//      Module:
// Description:

// Distributed under the MIT License (see included file LICENSE)
package slogging

import java.io.PrintStream

object PrintLogger extends LoggerTemplate {
  import PrintLoggerFactory._

  final override def logMessage(level: MessageLevel, name: String, message: String, cause: Option[Throwable]): Unit = (level match {
    case MessageLevel.`error` => errorStream
    case MessageLevel.`warn` => warnStream
    case MessageLevel.`info` => infoStream
    case MessageLevel.`debug` => debugStream
    case MessageLevel.`trace` => traceStream
  }).println(formatter.formatMessage(level,name,message,cause))
}

object PrintLoggerFactory extends UnderlyingLoggerFactory {

  private var _errorStream: PrintStream = System.err
  private var _warnStream: PrintStream = System.err
  private var _infoStream: PrintStream = System.err
  private var _debugStream: PrintStream = System.err
  private var _traceStream: PrintStream = System.err
  private var _formatter: MessageFormatter = MessageFormatter.default

  @inline
  final def errorStream: PrintStream = _errorStream
  final def errorStream_=(s: PrintStream): Unit = this.synchronized{ _errorStream = s }

  @inline
  final def warnStream: PrintStream = _warnStream
  final def warnStream_=(s: PrintStream): Unit = this.synchronized{ _warnStream = s }

  @inline
  final def infoStream: PrintStream = _infoStream
  final def infoStream_=(s: PrintStream): Unit = this.synchronized{ _infoStream = s }

  @inline
  final def debugStream: PrintStream = _debugStream
  final def debugStream_=(s: PrintStream): Unit = this.synchronized{ _debugStream = s }

  @inline
  final def traceStream: PrintStream = _traceStream
  final def traceStream_=(s: PrintStream): Unit = this.synchronized{ _traceStream = s }

  @inline
  final def formatter: MessageFormatter = _formatter
  final def formatter_=(f: MessageFormatter): Unit = this.synchronized{ _formatter = f }

  @inline
  override def getUnderlyingLogger(name: String): UnderlyingLogger = PrintLogger

}

