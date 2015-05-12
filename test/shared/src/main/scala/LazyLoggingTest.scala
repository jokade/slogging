import slogging._

class LazyLoggingTest(val factory: UnderlyingLoggerFactory) extends LoggingTest with LazyLogging {
  val eutName = factory.getClass.getName
  val testName = s"LazyLoggingTest with $eutName"
}
