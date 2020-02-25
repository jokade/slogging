// -   Project: slogging (https://github.com/jokade/slogging)
//      Module: shared
// Description: Test cases for functions defined in LoggingUtils
//
// Distributed under the MIT License (see included file LICENSE)
package slogging

import utest._

object LoggingUtilsTest extends TestSuite {
  import LoggingUtils._

  val tests = Tests {
    test("formatMessage") {
      assert( argsBracketFormat("simple message",Nil) == "simple message" )
      assert( argsBracketFormat("int {}",Seq(42).asInstanceOf[Seq[AnyRef]]) == "int 42" )
      assert( argsBracketFormat("int {}: {}",Seq(42,true).asInstanceOf[Seq[AnyRef]]) == "int 42: true" )
      assert( argsBracketFormat("{}",Seq(42).asInstanceOf[Seq[AnyRef]]) == "42" )
      assert( argsBracketFormat("{}",Nil) == "{}" )
      assert( argsBracketFormat("{}, {}",Seq(42).asInstanceOf[Seq[AnyRef]]) == "42, {}" )
      assert( argsBracketFormat("message: {},{},{}",Seq(1,2,3)) == "message: 1,2,3")
    }
  }
}
