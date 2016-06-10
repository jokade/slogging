import scalajs.js
import slogging._
import js.Dynamic.literal

object Main extends js.JSApp {
  def main(): Unit = {
    PrintLogger.printTimestamp = true
    val tests = Seq(
      new LazyLoggingTest(NullLoggerFactory()),
      new LazyLoggingTest(PrintLoggerFactory()),
      //new LazyLoggingTest(ConsoleLoggerFactory()),
      // winston / default logger
      new LazyLoggingTest(WinstonLoggerFactory()),
      // winston / custom logger
      {
        import WinstonLoggerFactory.{WinstonLogger, winston}
        new LazyLoggingTest(WinstonLoggerFactory(WinstonLogger(literal(
          levels = js.Dictionary(
            "trace" -> 0,
            "debug" -> 1,
            "info"  -> 2,
            "warn"  -> 3,
            "error" -> 4
          ),
          transports = js.Array(js.Dynamic.newInstance(winston.transports.Console)(
            literal(level = "trace")
          ))
      ))))
      }
      // http
      //httpLoggerTest()
    )

    tests.foreach( _.run )

  }


  private def httpLoggerTest() = {
    js.Dynamic.global.XMLHttpRequest = () => {
      var method: String = null
      var url: String = null
      val xhr = literal()
      xhr.open = (m:String, u: String) => {
        method = m
        url = u
      }
      xhr.send = (data: String) => println(s"SENDING '$data' TO $url")
      xhr
    }
    new LazyLoggingTest(HttpLoggerFactory("http://test.de", "clientId"))
  }
}
