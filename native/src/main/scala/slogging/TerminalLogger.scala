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

  private var _defaultCode: CString = c"\33[39m"
  private var _errorCode: CString   = c"\33[31m"
  private var _warnCode: CString    = c"\33[33m"
  private var _infoCode: CString    = c"\33[39m"
  private var _debugCode: CString   = c"\33[39m"
  private var _traceCode: CString   = c"\33[39m"

  private var _formatter: MessageFormatter = MessageFormatter.default

  @inline
  final def defaultCode: CString = _defaultCode
  final def defaultCode_(c: CString): Unit = this.synchronized{ _defaultCode = c }

  @inline
  final def errorCode: CString = _errorCode
  final def errorCode_(c: CString): Unit = this.synchronized{ _errorCode = c }

  @inline
  final def warnCode: CString = _warnCode
  final def warnCode_(c: CString): Unit = this.synchronized{ _warnCode = c }

  @inline
  final def infoCode: CString = _infoCode
  final def infoCode_(c: CString): Unit = this.synchronized{ _infoCode = c }

  @inline
  final def debugCode: CString = _debugCode
  final def debugCode_(c: CString): Unit = this.synchronized{ _debugCode = c }

  @inline
  final def traceCode: CString = _traceCode
  final def traceCode_(c: CString): Unit = this.synchronized{ _traceCode = c }

  @inline
  final def formatter: MessageFormatter = _formatter
  final def formatter_=(f: MessageFormatter): Unit = this.synchronized{ _formatter = f }

  @inline
  override def getUnderlyingLogger(name: String): UnderlyingLogger = TerminalLogger
}
