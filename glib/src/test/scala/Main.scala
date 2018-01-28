import slogging.{GLibLoggerFactory, LazyLogging, LogLevel, LoggerConfig}

object Main extends LazyLogging {
  def main(args: Array[String]): Unit = {
    LoggerConfig.factory = GLibLoggerFactory()
    LoggerConfig.level = LogLevel.TRACE
    logger.error("ERROR")
    logger.warn("WARN")
    logger.info("INFO")
    logger.debug("DEBUG")
    logger.trace("TRACE")
  }
}
