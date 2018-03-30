//     Project: slogging
//      Module:
// Description:

// Distributed under the MIT License (see included file LICENSE)
package slogging.config

import slogging.Logger

/**
 * Marks trait for objects that provide a Logger.
 */
trait LoggerHolder {
  protected final val loggerName: String = getClass.getName
  protected def logger: Logger
}

/**
 * Defines `logger` as a lazy value initialized with an [[UnderlyingLogger]]
 * named according to the class into which this trait is mixed
 */
trait LazyLogging extends LoggerHolder {
  protected lazy val logger = LoggerFactory.getLogger(loggerName)
}


/**
 * Defines `logger` as a value initialized with an [[UnderlyingLogger]]
 * named according to the class into which this trait is mixed
 */
trait StrictLogging extends LoggerHolder {
  protected val logger : Logger = LoggerFactory.getLogger(loggerName)
}
