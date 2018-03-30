// -   Project: slogging (https://github.com/jokade/slogging)
//      Module: shared
// Description: Provides implementations for LoggerS and LoggerFactoryS
//
// Copyright (c) 2015 Johannes Kastner <jkspam@karchedon.de>
//               Distributed under the MIT License (see included file LICENSE)
package slogging


trait UnderlyingLoggerFactory {
  def getUnderlyingLogger(name: String) : UnderlyingLogger
  def apply() : UnderlyingLoggerFactory = this
}


trait LoggerFactory {
  def getLogger(name: String) : Logger
}

object LoggerFactory extends LoggerFactory {

  def getLogger(name: String) : Logger = Logger( LoggerConfig.factory.getUnderlyingLogger(name) )


}

