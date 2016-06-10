import slogging._

trait LoggingTest extends Test {
  this: LoggerHolder =>

  def testName: String
  def factory: UnderlyingLoggerFactory

  final override def run() : Unit = {
    LoggerConfig.factory = factory()
    println()
    println(s"===== $testName =====")
    Seq(LogLevel.OFF, LogLevel.ERROR, LogLevel.WARN, LogLevel.INFO, LogLevel.DEBUG, LogLevel.TRACE).foreach{ l =>
      println(s"LEVEL $l:")
      LoggerConfig.level = l
      println("- error message")
      logger.error("ERROR")
      println("- error message with params (42,true, hello)")
      logger.error("ERROR: {},{},{}",42,true,"hello")
      println("- warning")
      logger.warn("WARNING")
      println("- warning with params (42,true, hello)")
      logger.warn("WARNING: {},{},{}",42,true,"hello")
      println("- info")
      logger.info("INFO")
      println("- info with params (42,true, hello)")
      logger.info("INFO: {},{},{}",42,true,"hello")
      println("- debug")
      logger.debug("DEBUG")
      println("- debug with params (42,true, hello)")
      logger.debug("DEBUG: {},{},{}",42,true,"hello")
      println("- trace")
      logger.trace("TRACE")
      println("- trace with params (42,true, hello)")
      logger.trace("TRACE: {},{},{}",42,true,"hello")
      println()
    }
  }
}  
