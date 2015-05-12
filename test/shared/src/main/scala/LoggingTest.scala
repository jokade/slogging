import slogging._

trait LoggingTest extends Test {
  this: LoggerHolder =>

  def testName: String
  def factory: UnderlyingLoggerFactory

  final override def run() : Unit = {
    factory()
    println()
    println(s"===== $testName =====")
    Seq(LogLevel.OFF, LogLevel.ERROR, LogLevel.WARN, LogLevel.INFO, LogLevel.DEBUG, LogLevel.TRACE).foreach{ l =>
      println(s"LEVEL $l:")
      LoggerConfig.level = l
      println("- error message")
      logger.error("ERROR")
      println("- warning")
      logger.warn("WARNING")
      println("- info")
      logger.info("INFO")
      println("- debug")
      logger.debug("DEBUG")
      println("- trace")
      logger.trace("TRACE")
      println()
    }
  }
}  
