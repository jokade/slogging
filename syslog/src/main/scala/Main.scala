import slogging._
//     Project: slogging
//      Module: 
// Description: 

// Distributed under the MIT License (see included file LICENSE)
object Main extends LazyLogging {
  def main(args: Array[String]): Unit = {
    LoggerConfig.factory = SyslogLoggerFactory()
    LoggerConfig.level = LogLevel.TRACE
    logger.error("ERROR!")
    logger.warn("warn...")
    logger.info("why?")
  }
}
