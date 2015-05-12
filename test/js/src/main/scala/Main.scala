import scalajs.js
import slogging._

object Main extends js.JSApp {
  def main(): Unit = {
    val tests = Seq(
      new LazyLoggingTest(NullLoggerFactory),
      new LazyLoggingTest(PrintLoggerFactory),
      new LazyLoggingTest(ConsoleLoggerFactory)
    )

    tests.foreach( _.run )
  }
}
