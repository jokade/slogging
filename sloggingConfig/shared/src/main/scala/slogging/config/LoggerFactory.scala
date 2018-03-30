//     Project: slogging
//      Module:
// Description:

// Distributed under the MIT License (see included file LICENSE)
package slogging.config
import com.typesafe.config.{Config, ConfigFactory}
import slogging.{LogLevel, Logger, LoggerConfig}

object LoggerFactory extends slogging.LoggerFactory {
  private var initialized = false

  private var registeredProviders: Map[String,UnderlyingLoggerFactoryProvider] = Map(
    NullLoggerProvider.name -> NullLoggerProvider,
    PrintLoggerProvider.name -> PrintLoggerProvider
  )

  override def getLogger(name: String): Logger = {
    if(!initialized) this.synchronized{
      if(!initialized)
        loadDefaultConfig()
    }
    slogging.LoggerFactory.getLogger(name)
  }

  def loadConfig(config: Config): Unit = {
    val providerName = config.getString("slogging.factory")
    registeredProviders.get( providerName ) match {
      case Some(provider) => LoggerConfig.factory = provider.create(config)
      case None => throw new RuntimeException(s"No UnderlyingLoggerFactoryProvider registered for name '$providerName'")
    }

    val level = config.getString("slogging.level")
    LoggerConfig.level = level.toUpperCase.trim match {
      case "OFF" => LogLevel.OFF
      case "ERROR" => LogLevel.ERROR
      case "WARN" => LogLevel.WARN
      case "DEBUG" => LogLevel.DEBUG
      case "TRACE" => LogLevel.TRACE
      case x => throw new RuntimeException(s"Invalid value for slogging.level: $x")
    }
  }

  def loadDefaultConfig(): Config = {
    val config = ConfigFactory.load()
    loadConfig(config)
    config
  }

  def registerProvider(provider: UnderlyingLoggerFactoryProvider): Unit = this.synchronized{
    if( provider == null )
      throw new RuntimeException("invalid UnderlyingLoggerFactoryProvider: null")
    if( registeredProviders.contains(provider.name) )
      throw new RuntimeException(s"another UnderlyingLoggerFactoryProvider already registered for name '${provider.name}")
    registeredProviders += (provider.name -> provider)
  }
}
