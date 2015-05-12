import slogging._

object Main {

  def main(args: Array[String]) : Unit = {
    val tests = Seq(
      new LazyLoggingTest(NullLoggerFactory),
      new LazyLoggingTest(PrintLoggerFactory)
    )

    tests.foreach( _.run )
  }
}
