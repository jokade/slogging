// -   Project: slogging (https://github.com/jokade/slogging)
//      Module: shared
// Description: Test cases for functions defined in LoggingUtils
//
// Distributed under the MIT License (see included file LICENSE)
package slogging

import utest._

object LoggingUtilsTest extends TestSuite {
  import LoggingUtils._

  val tests = TestSuite{

    'formatMessage-{
      assert( formatMessage("simple message",Nil) == "simple message" )
      assert( formatMessage("int {}",Seq(42).asInstanceOf[Seq[AnyRef]]) == "int 42" )
      assert( formatMessage("int {}: {}",Seq(42,true).asInstanceOf[Seq[AnyRef]]) == "int 42: true" )
      assert( formatMessage("{}",Seq(42).asInstanceOf[Seq[AnyRef]]) == "42" )
      assert( formatMessage("{}",Nil) == "{}" )
      assert( formatMessage("{}, {}",Seq(42).asInstanceOf[Seq[AnyRef]]) == "42, {}" )
      assert( formatMessage("message: {},{},{}",Seq(1.0,2.0,3.0)) == "message: 1.0,2.0,3.0")
    }
  }
}
