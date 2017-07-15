import scalajs.js
import slogging._

object App extends LazyLogging {
  
  def main(args: Array[String]): Unit = {
    LoggerConfig.factory = HttpLoggerFactory("log")
    //LoggerConfig.factory = PrintLoggerFactory()
    logger.info("hello")
  }
}
