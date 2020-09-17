import slogging._

object Main extends LazyLogging {
  LoggerConfig.factory = PrintLoggerFactory()

  def main(args: Array[String]): Unit = {
    Seq(LogLevel.OFF, LogLevel.ERROR, LogLevel.WARN, LogLevel.INFO, LogLevel.DEBUG, LogLevel.TRACE).foreach(logWithLevel)
  }

  private def logWithLevel(level: LogLevel) {
    LoggerConfig.level = level
    logger.error("error")
    logger.warn("warn")
    logger.info("info") 
    logger.debug("debug") 
    logger.trace("trace") 
    println("===")
  }
}
