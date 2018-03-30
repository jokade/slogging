//     Project: Default (Template) Project
//      Module:
// Description:

// Distributed under the MIT License (see included file LICENSE)
package slogging

object LoggingUtils {

  // TODO: optimize?
  /**
   * Replaces `{}` in `msg` with the args.
   *
   * @param msg message to be filled with arguments
   * @param args arguments
   * @return
   */
  def argsBracketFormat(msg: String, args: Seq[Any]) : String =
    if(args.isEmpty) msg
    else {
      @annotation.tailrec
      def loop(acc: String, chars: List[Char], args: Seq[Any]): String =
        if(chars.isEmpty) acc
        else if(args.isEmpty) acc + chars.mkString
        else chars match {
          case '{' :: '}' :: xs  =>
            loop( acc + args.head, xs, args.tail )
          case a :: xs => loop( acc + a, xs, args )
          case Nil => acc
        }

      loop("",msg.toList,args)
    }

//  @inline
  def argsStringFormat(msg: String, args: Seq[Any]): String = String.format(msg,args.asInstanceOf[Seq[Object]]:_*)
}
