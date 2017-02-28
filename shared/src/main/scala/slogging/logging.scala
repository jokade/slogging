// -   Project: slogging (https://github.com/jokade/slogging)
//      Module: shared
// Description: Defines mixins that provide access to a logger instance
//
// Copyright (c) 2015 Johannes Kastner <jkspam@karchedon.de>
//               Distributed under the MIT License (see included file LICENSE)
package slogging

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
