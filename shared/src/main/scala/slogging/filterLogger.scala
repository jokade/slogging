// -   Project: slogging (https://github.com/jokade/slogging)
//      Module: shared
// Description: Provides a FilterLogger + factory.
//
// Distributed under the MIT License (see included file LICENSE)
package slogging

/**
 * Configuration object for all FilterLogger instances.
 */
object FilterLogger {
  /**
   * A PartialFunction that receives the log level and source of the logging message
   * and returns the Logger to be used. __Must be defined for all inputs__!
   */
  type Filter = PartialFunction[(LogLevel,String),UnderlyingLogger]
  val nullFilter: Filter = { case _ => NullLogger }
  private var _filter: Filter = nullFilter

  /**
   * The filter function to be used by all [[FilterLogger]] instances.
   * Defaults to a PartialFunction that returns the [[NullLogger]] for all inputs.
   */
  @inline
  def filter: Filter = _filter
  def filter_=(f: Filter) = this.synchronized { _filter = f }
}

final class FilterLogger(name: String) extends AbstractUnderlyingLogger {
  override def error(source: String, message: String): Unit = FilterLogger.filter((LogLevel.ERROR,name)).error(source,message)
  override def error(source: String, message: String, cause: Throwable): Unit = FilterLogger.filter((LogLevel.ERROR,name)).error(source,message,cause)
  override def error(source: String, message: String, args: Any*): Unit = FilterLogger.filter((LogLevel.ERROR,name)).error(source,message,args:_*)

  override def warn(source: String, message: String): Unit = FilterLogger.filter((LogLevel.WARN,name)).warn(source,message)
  override def warn(source: String, message: String, cause: Throwable): Unit = FilterLogger.filter((LogLevel.WARN,name)).warn(source,message,cause)
  override def warn(source: String, message: String, args: Any*): Unit = FilterLogger.filter((LogLevel.WARN,name)).warn(source,message,args:_*)

  override def info(source: String, message: String): Unit = FilterLogger.filter((LogLevel.INFO,name)).info(source,message)
  override def info(source: String, message: String, cause: Throwable): Unit = FilterLogger.filter((LogLevel.INFO,name)).info(source,message,cause)
  override def info(source: String, message: String, args: Any*): Unit = FilterLogger.filter((LogLevel.INFO,name)).info(source,message,args:_*)

  override def debug(source: String, message: String): Unit = FilterLogger.filter((LogLevel.DEBUG,name)).debug(source,message)
  override def debug(source: String, message: String, cause: Throwable): Unit = FilterLogger.filter((LogLevel.DEBUG,name)).debug(source,message,cause)
  override def debug(source: String, message: String, args: Any*): Unit = FilterLogger.filter((LogLevel.DEBUG,name)).debug(source,message,args:_*)

  override def trace(source: String, message: String): Unit = FilterLogger.filter((LogLevel.TRACE,name)).trace(source,message)
  override def trace(source: String, message: String, cause: Throwable): Unit = FilterLogger.filter((LogLevel.TRACE,name)).trace(source,message,cause)
  override def trace(source: String, message: String, args: Any*): Unit = FilterLogger.filter((LogLevel.TRACE,name)).trace(source,message,args:_*)

}

object FilterLoggerFactory extends UnderlyingLoggerFactory {
  override def getUnderlyingLogger(name: String): UnderlyingLogger = new FilterLogger(name)
}
