//     Project: Default (Template) Project
//      Module:
// Description:

// Distributed under the MIT License (see included file LICENSE)
package slogging

/** Level of a given logging message */
sealed trait MessageLevel

object MessageLevel {
  case object trace extends MessageLevel
  case object debug extends MessageLevel
  case object info extends MessageLevel
  case object warn extends MessageLevel
  case object error extends MessageLevel
}

