//     Project: slogging
//      Module:
// Description:

// Distributed under the MIT License (see included file LICENSE)
package slogging.config

import com.typesafe.config.Config
import slogging.{NullLoggerFactory, PrintLoggerFactory, UnderlyingLoggerFactory}

trait UnderlyingLoggerFactoryProvider {
  def name: String
  def create(config: Config): UnderlyingLoggerFactory
}

object NullLoggerProvider extends UnderlyingLoggerFactoryProvider {
  override def name: String = "NullLogger"

  override def create(config: Config): UnderlyingLoggerFactory = NullLoggerFactory
}

object PrintLoggerProvider extends UnderlyingLoggerFactoryProvider {
  override def name: String = "PrintLogger"

  override def create(config: Config): UnderlyingLoggerFactory = PrintLoggerFactory
}
