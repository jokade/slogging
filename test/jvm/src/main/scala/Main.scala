import slogging._

object Main {

  def main(args: Array[String]) : Unit = {
    PrintLogger.printTimestamp = true
    FilterLogger.filter = {
      case (LogLevel.TRACE | LogLevel.WARN, _ ) => NullLogger
      case _ => MultiLogger(PrintLogger,PrintLogger) 
    }
    val tests = Seq(
      new LazyLoggingTest(NullLoggerFactory),
      new LazyLoggingTest(PrintLoggerFactory),
      new LazyLoggingTest(SLF4JLoggerFactory),
      new LazyLoggingTest(FilterLoggerFactory)
    )

    tests.foreach( _.run )
  }
}
