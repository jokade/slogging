//     Project: slogging
//      Module:
// Description:

// Distributed under the MIT License (see included file LICENSE)
package slogging

import scalanative.native._
import scalanative.native.stdio.{fprintf, stderr}

object TerminalLogger extends LoggerTemplate {
  import TerminalLoggerFactory._

  override def logMessage(level: MessageLevel, name: String, message: String, cause: Option[Throwable]): Unit = Zone{ implicit z =>
    val msg = toCString( TerminalLoggerFactory.formatter.formatMessage(level,name,message,cause) )
    level match {
      case MessageLevel.`error` =>
        fprintf(stderr,c"%s%s%s\n",errorCode,msg,defaultCode)
      case MessageLevel.`warn` =>
        fprintf(stderr,c"%s%s%s\n",warnCode,msg,defaultCode)
      case MessageLevel.`info` =>
        fprintf(stderr,c"%s%s%s\n",infoCode,msg,defaultCode)
      case MessageLevel.debug =>
        fprintf(stderr,c"%s%s%s\n",debugCode,msg,defaultCode)
      case MessageLevel.trace =>
        fprintf(stderr,c"%s%s%s\n",traceCode,msg,defaultCode)
    }
  }
}

object TerminalLoggerFactory extends UnderlyingLoggerFactory {
  type TerminalControlCode = CString

  private var _defaultCode: TerminalControlCode = TerminalControlCode.default
  private var _errorCode: TerminalControlCode   = TerminalControlCode.red
  private var _warnCode: TerminalControlCode    = TerminalControlCode.yellow
  private var _infoCode: TerminalControlCode    = TerminalControlCode.default
  private var _debugCode: TerminalControlCode   = TerminalControlCode.default
  private var _traceCode: TerminalControlCode   = TerminalControlCode.default

  private var _formatter: MessageFormatter = MessageFormatter.default

  object TerminalControlCode {
    val default: TerminalControlCode = c"\33[39m"
    val red: TerminalControlCode     = c"\33[31m"
    val green: TerminalControlCode   = c"\33[32m"
    val yellow: TerminalControlCode  = c"\33[33m"
    val blue: TerminalControlCode    = c"\33[34m"
    val magenta: TerminalControlCode = c"\33[35m"
    val cyan: TerminalControlCode    = c"\33[36m"
    val white: TerminalControlCode   = c"\33[37m"
  }

  @inline
  final def defaultCode: TerminalControlCode = _defaultCode
  final def defaultCode_=(c: TerminalControlCode): Unit = this.synchronized{ _defaultCode = c }

  @inline
  final def errorCode: TerminalControlCode = _errorCode
  final def errorCode_=(c: TerminalControlCode): Unit = this.synchronized{ _errorCode = c }

  @inline
  final def warnCode: TerminalControlCode = _warnCode
  final def warnCode_=(c: TerminalControlCode): Unit = this.synchronized{ _warnCode = c }

  @inline
  final def infoCode: TerminalControlCode = _infoCode
  final def infoCode_=(c: TerminalControlCode): Unit = this.synchronized{ _infoCode = c }

  @inline
  final def debugCode: TerminalControlCode = _debugCode
  final def debugCode_=(c: TerminalControlCode): Unit = this.synchronized{ _debugCode = c }

  @inline
  final def traceCode: TerminalControlCode = _traceCode
  final def traceCode_=(c: TerminalControlCode): Unit = this.synchronized{ _traceCode = c }

  @inline
  final def formatter: MessageFormatter = _formatter
  final def formatter_=(f: MessageFormatter): Unit = this.synchronized{ _formatter = f }

  @inline
  override def getUnderlyingLogger(name: String): UnderlyingLogger = TerminalLogger
}
